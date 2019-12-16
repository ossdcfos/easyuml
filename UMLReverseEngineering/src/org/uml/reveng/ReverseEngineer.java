package org.uml.reveng;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.Name;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import org.openide.util.Exceptions;
import org.uml.model.ClassDiagram;
import org.uml.model.Visibility;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.EnumComponent;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.members.Literal;
import org.uml.model.members.MemberBase;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;
import org.uml.model.relations.AggregationRelation;
import org.uml.model.relations.CardinalityEnum;
import org.uml.model.relations.HasBaseRelation;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.RelationBase;
import org.uml.model.relations.UseRelation;

/**
 *
 * @author Boris Perović
 */
public class ReverseEngineer {

    @SuppressWarnings("unchecked")
    public static ClassDiagram createClassDiagramFromFiles(Collection<File> files, String diagramName) {
        ClassDiagram classDiagram = new ClassDiagram();
        classDiagram.setName(diagramName);

        // First pass - generate components
        for (File file : files) {
            List<ComponentBase> components = createComponents(file);
            for (ComponentBase component : components) {
                classDiagram.addComponent(component);
            }
        }

        // Second pass - generate relationships
        for (File file : files) {
            List<RelationBase> relations = createRelations(file, classDiagram);
            for (RelationBase relation : relations) {
                classDiagram.addRelation(relation);
                // Remove a field from the source which corresponds to the relation
                // Relation fields are exclusively represented by relations in the class diagram
                if (relation instanceof HasBaseRelation) {
                    HasBaseRelation hasRelation = (HasBaseRelation) relation;
                    ComponentBase source = hasRelation.getSource();
                    for (MemberBase member : source.getMembers()) {
                        if (member instanceof Field) {
                            Field field = (Field) member;
                            if (hasRelation.getFieldSignature().equals(field.getSignature())) {
                                source.removeMember(field);
                                // One field per relation
                                break;
                            }
                        }
                    }
                }
            }
        }

        resetNextComponentPosition();
        return classDiagram;
    }

    private static List<ComponentBase> createComponents(File file) {
        List<ComponentBase> components = new LinkedList<>();
        try {
            CompilationUnit cu = JavaParser.parse(file);
            NodeList<TypeDeclaration<?>> types = cu.getTypes();
            for (TypeDeclaration type : types) {
                ComponentBase component = null;
                if (type instanceof ClassOrInterfaceDeclaration) {
                    ClassOrInterfaceDeclaration cidecl = (ClassOrInterfaceDeclaration) type;
                    if (!cidecl.isInterface()) {
                        component = createClass(cidecl);
                    } else {
                        component = createInterface(cidecl);
                    }
                } else if (type instanceof EnumDeclaration) {
                    EnumDeclaration edecl = (EnumDeclaration) type;
                    component = createEnum(edecl);
                }
                if (component != null) {
                    components.add(component);
                }
            }
        } catch (ParseProblemException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        return components;
    }

    private static ClassComponent createClass(ClassOrInterfaceDeclaration declaration) {
        ClassComponent clazz = new ClassComponent(declaration.getName().asString());
        clazz.setAbstract(declaration.isAbstract());
        clazz.setFinal(declaration.isFinal());
        clazz.setStatic(declaration.isStatic());
        
        // TODO fix for inner classes
        Optional<Node> optionalNode = declaration.getParentNode();
        Node parentNode = optionalNode.get();
        Optional<CompilationUnit> optionalCU = parentNode.findCompilationUnit();
        CompilationUnit cu = optionalCU.get();
        Optional<PackageDeclaration> parentPackage = cu.getPackageDeclaration();
        if (parentPackage.isPresent()) {
            clazz.setParentPackage(parentPackage.get().getName().asString());
        } else {
            clazz.setParentPackage("");
        }

        for (BodyDeclaration member : safe(declaration.getMembers())) {
            if (member instanceof FieldDeclaration) {
                Field field = createField((FieldDeclaration) member);
                clazz.addField(field);
            } else if (member instanceof ConstructorDeclaration) {
                Constructor constructorMember = createConstructor((ConstructorDeclaration) member);
                clazz.addConstructor(constructorMember);
            } else if (member instanceof MethodDeclaration) {
                Method methodMember = createMethod((MethodDeclaration) member);
                clazz.addMethod(methodMember);
            }
        }
        clazz.setLocation(getNextComponentPosition());

        return clazz;
    }

    private static ComponentBase createInterface(ClassOrInterfaceDeclaration declaration) {
        InterfaceComponent interfaze = new InterfaceComponent(declaration.getName().asString());
        interfaze.setStatic(declaration.isStatic());
        
        Optional<Node> optionalNode = declaration.getParentNode();
        Node parentNode = optionalNode.get();
        Optional<CompilationUnit> optionalCU = parentNode.findCompilationUnit();
        CompilationUnit cu = optionalCU.get();
        Optional<PackageDeclaration> parentPackage = cu.getPackageDeclaration();
        if (parentPackage.isPresent()) {
            interfaze.setParentPackage(parentPackage.get().getName().asString());
        } else {
            interfaze.setParentPackage("");
        }

        for (BodyDeclaration member : safe(declaration.getMembers())) {
            if (member instanceof MethodDeclaration) {
                Method methodMember = createMethod((MethodDeclaration) member);
                interfaze.addMethod(methodMember);
            }
        }
        interfaze.setLocation(getNextComponentPosition());

        return interfaze;
    }

    private static ComponentBase createEnum(EnumDeclaration declaration) {
        EnumComponent enumm = new EnumComponent(declaration.getName().asString());
        
        Optional<Node> optionalNode = declaration.getParentNode();
        Node parentNode = optionalNode.get();
        Optional<CompilationUnit> optionalCU = parentNode.findCompilationUnit();
        CompilationUnit cu = optionalCU.get();
        Optional<PackageDeclaration> parentPackage = cu.getPackageDeclaration();
        if (parentPackage.isPresent()) {
            enumm.setParentPackage(parentPackage.get().getName().asString());
        } else {
            enumm.setParentPackage("");
        }        

        for (BodyDeclaration entry : safe(declaration.getEntries())) {
            if (entry instanceof EnumConstantDeclaration) {
                Literal literalMember = createLiteral((EnumConstantDeclaration) entry);
                enumm.addLiteral(literalMember);
            }
        }
        enumm.setLocation(getNextComponentPosition());

        return enumm;
    }

    private static Field createField(FieldDeclaration declaration) {
        VariableDeclarator variable = declaration.getVariables().get(0);
        Field field = new Field(variable.getName().asString(), variable.getType().asString(), getVisibility(declaration));

        field.setStatic(declaration.isStatic());
        field.setFinal(declaration.isFinal());
        field.setVolatile(declaration.isVolatile());
        field.setTransient(declaration.isTransient());

        return field;
    }

    private static Visibility getVisibility(FieldDeclaration declaration) {
        if (declaration.isPublic()) {
            return Visibility.PUBLIC;
        } else if (declaration.isProtected()) {
            return Visibility.PROTECTED;
        } else if (declaration.isPrivate()) {
            return Visibility.PRIVATE;
        } else {
            return Visibility.PACKAGE;
        }
    }

    private static Method createMethod(MethodDeclaration declaration) {
        Method method = new Method(declaration.getName().asString(), declaration.getType().asString());
        method.setVisibility(getVisibility(declaration));

        method.setAbstract(declaration.isAbstract());
        method.setStatic(declaration.isStatic());
        method.setFinal(declaration.isFinal());
        method.setSynchronized(declaration.isSynchronized());
        
        LinkedHashSet<MethodArgument> arguments = method.getArguments();
        for (Parameter parameter : safe(declaration.getParameters())) {
            MethodArgument arg = new MethodArgument(parameter.getType().asString(), parameter.getName().asString());
            arguments.add(arg);
        }

        return method;
    }

    private static Visibility getVisibility(MethodDeclaration declaration) {
        if (declaration.isPublic()) {
            return Visibility.PUBLIC;
        } else if (declaration.isProtected()) {
            return Visibility.PROTECTED;
        } else if (declaration.isPrivate()) {
            return Visibility.PRIVATE;
        } else {
            return Visibility.PACKAGE;
        }
    }

    private static Constructor createConstructor(ConstructorDeclaration declaration) {
        Constructor constructor = new Constructor(declaration.getName().asString());
        constructor.setVisibility(getVisibility(declaration));

        LinkedHashSet<MethodArgument> arguments = constructor.getArguments();

        for (Parameter parameter : safe(declaration.getParameters())) {
            MethodArgument arg = new MethodArgument(parameter.getType().asString(), parameter.getName().asString());
            arguments.add(arg);
        }

        return constructor;
    }

    private static Visibility getVisibility(ConstructorDeclaration declaration) {
        if (declaration.isPublic()) {
            return Visibility.PUBLIC;
        } else if (declaration.isProtected()) {
            return Visibility.PROTECTED;
        } else if (declaration.isPrivate()) {
            return Visibility.PRIVATE;
        } else {
            return Visibility.PACKAGE;
        }
    }

    private static Literal createLiteral(EnumConstantDeclaration declaration) {
        Literal literal = new Literal(declaration.getName().asString());
        return literal;
    }

    private static List<RelationBase> createRelations(File file, ClassDiagram classDiagram) {
        List<RelationBase> relations = new LinkedList<>();
        //!TODO: Update to JavaParser 9
        /*try {
            CompilationUnit cu = JavaParser.parse(file);
            for (TypeDeclaration typedecl : safe(cu.getTypes())) {
                if (typedecl instanceof ClassOrInterfaceDeclaration) {
                    ClassOrInterfaceDeclaration cidecl = (ClassOrInterfaceDeclaration) typedecl;

                    // Is Relation
                    if (cidecl.getExtends() != null) {
                        ClassOrInterfaceType parent = cidecl.getExtends().get(0);
                        ComponentBase childComponent = null;
                        ComponentBase parentComponent = null;
                        for (ComponentBase component : classDiagram.getComponents()) {
                            if (component.getName().equals(cidecl.getName())) {
                                childComponent = component;
                            } else if (component.getName().equals(parent.getName())) {
                                parentComponent = component;
                            }
                            if (childComponent != null && parentComponent != null) {
                                break;
                            }
                        }
                        if (childComponent != null && parentComponent != null) {
                            IsRelation relation = new IsRelation();
                            relation.setSource(childComponent);
                            relation.setTarget(parentComponent);
                            relations.add(relation);
                        }
                    }

                    // Implements and Has Relation
                    if (!cidecl.isInterface()) {
                        // Implements Relation
                        for (ClassOrInterfaceType parent : safe(cidecl.getImplements())) {
                            ComponentBase childComponent = null;
                            ComponentBase parentComponent = null;
                            for (ComponentBase component : classDiagram.getComponents()) {
                                if (component.getName().equals(cidecl.getName())) {
                                    childComponent = component;
                                } else if (component.getName().equals(parent.getName())) {
                                    parentComponent = component;
                                }
                                if (childComponent != null && parentComponent != null) {
                                    break;
                                }
                            }
                            if (childComponent != null && parentComponent != null) {
                                ImplementsRelation relation = new ImplementsRelation();
                                relation.setSource(childComponent);
                                relation.setTarget(parentComponent);
                                relations.add(relation);
                            }
                        }

                        // Has Relation
                        for (BodyDeclaration member : safe(cidecl.getMembers())) {
                            if (member instanceof FieldDeclaration) {
                                ComponentBase containerComponent = null;
                                ComponentBase containedComponent = null;
                                FieldDeclaration declaration = (FieldDeclaration) member;
                                for (ComponentBase component : classDiagram.getComponents()) {
                                    if (cidecl.getName().equals(component.getName())) {
                                        containerComponent = component;
                                    }
                                    String fieldType = declaration.getType().toString();
                                    if (fieldType.equals(component.getName()) || fieldType.matches(".*[<\\,\\s]" + component.getName() + "[>\\,\\s].*")) {
                                        containedComponent = component;
                                    }
                                }
                                if (containerComponent != null && containedComponent != null) {
                                    AggregationRelation relation = new AggregationRelation();
                                    relation.setName(declaration.getVariables().get(0).getId().getName());
                                    relation.setSource(containerComponent);
                                    relation.setTarget(containedComponent);
                                    relation.setCardinalitySource(CardinalityEnum.One2One);
                                    String type = declaration.getType().toString();
                                    if (type.contains("List") || type.contains("Set") || type.contains("Map") || type.contains("Queue") || type.contains("Deque")) {
                                        relation.setCardinalityTarget(CardinalityEnum.Zero2Many);
                                        relation.setCollectionType(type);
                                    } else {
                                        relation.setCardinalityTarget(CardinalityEnum.One2One);
                                    }
                                    relations.add(relation);
                                }
                            }
                        }
                    }

                    // Use Relation
                    for (BodyDeclaration member : safe(cidecl.getMembers())) {
                        if (member instanceof MethodDeclaration) {
                            ComponentBase containerComponent = null;
                            ComponentBase usedComponent = null;
                            MethodDeclaration declaration = (MethodDeclaration) member;
                            for (ComponentBase component : classDiagram.getComponents()) {
                                if (cidecl.getName().equals(component.getName())) {
                                    containerComponent = component;
                                }
                                if (declaration.getType().toString().contains(component.getName())) {
                                    usedComponent = component;
                                } else {
                                    for (Parameter parameter : safe(declaration.getParameters())) {
                                        if (parameter.getType().toString().contains(component.getName())) {
                                            usedComponent = component;
                                        }
                                    }
                                }
                            }
                            if (containerComponent != null && usedComponent != null) {
                                UseRelation relation = new UseRelation();
                                relation.setSource(containerComponent);
                                relation.setTarget(usedComponent);
                                relation.setCardinalitySource(CardinalityEnum.One2One);
                                relation.setCardinalityTarget(CardinalityEnum.One2One);
                                relations.add(relation);
                            }
                        }
                    }
                }
            }
        } catch (ParseException | IOException ex) {
            Exceptions.printStackTrace(ex);
        }*/
        return relations;
    }

    static Point nextComponentPosition = new Point(20, 20);

    private static Point getNextComponentPosition() {
        Point oldPoint = new Point();
        if (nextComponentPosition.getX() > 2120) {
            nextComponentPosition.move(20, (int) nextComponentPosition.getY() + 400);
        }
        oldPoint.x = (int) nextComponentPosition.getX();
        oldPoint.y = (int) nextComponentPosition.getY();
        nextComponentPosition.translate(300, 0);
        return oldPoint;
    }

    private static void resetNextComponentPosition() {
        nextComponentPosition = new Point(20, 20);
    }

    @SuppressWarnings("unchecked")
    private static <T> List<T> safe(List<T> other) {
        return other == null ? Collections.EMPTY_LIST : other;
    }
}
