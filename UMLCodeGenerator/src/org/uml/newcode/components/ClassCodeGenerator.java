package org.uml.newcode.components;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import java.util.LinkedList;
import java.util.List;
import org.uml.filetype.cdg.renaming.MyClassDiagramRenameTable;
import org.uml.model.Visibility;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.RelationBase;
import org.uml.newcode.members.ConstructorCodeGenerator;
import org.uml.newcode.members.FieldCodeGenerator;
import org.uml.newcode.members.MethodCodeGenerator;

/**
 * Code generator for classes, produces one .java file.
 *
 * @author Boris Perović
 */
public class ClassCodeGenerator extends ComponentCodeGeneratorBase<ClassComponent>{

    private static ClassCodeGenerator instance;

    public static ClassCodeGenerator getInstance() {
        if (instance == null) instance = new ClassCodeGenerator();
        return instance;
    }
    
    @Override
    protected CompilationUnit generateCode(ClassComponent component) {
        System.out.println("Generate class "+component.getSignature());
        CompilationUnit cu = new CompilationUnit();
        cu.setTypes(new NodeList<TypeDeclaration<?>>());
        String parentPackage = component.getFullParentPackage();
        if (!parentPackage.isEmpty()) {
            cu.setPackageDeclaration(parentPackage);
        }
        createSkeleton(component, cu);
        FieldCodeGenerator.createFields(component, cu);
        ConstructorCodeGenerator.createConstructors(component, cu);
        MethodCodeGenerator.createMethods(component, cu);

        return cu;
    }

    private static void createSkeleton(ClassComponent component, CompilationUnit cu) {
        ClassOrInterfaceDeclaration declaration = new ClassOrInterfaceDeclaration();
        declaration.setName(component.getName());
        switch (component.getVisibility()) {
            case PUBLIC:
                declaration.addModifier(Modifier.PUBLIC);
                break;
            case PROTECTED:
                declaration.addModifier(Modifier.PROTECTED);
                break;
            case PRIVATE:
                declaration.addModifier(Modifier.PRIVATE);
                break;
        }
        if (component.isAbstract()) 
            declaration.addModifier(Modifier.ABSTRACT);
        if (component.isStatic()) 
            declaration.addModifier(Modifier.STATIC);
        if (component.isFinal()) 
            declaration.addModifier(Modifier.FINAL);

        ComponentBase extendedClass = getExtendedClass(component);
        if (extendedClass != null) {
            NodeList<ClassOrInterfaceType> extended = new NodeList();
            extended.add(JavaParser.parseClassOrInterfaceType(extendedClass.getName()));
            declaration.setExtendedTypes(extended);
        }
        List<ComponentBase> implementedInterfaces = getImplementedInterfaces(component);
        if (!implementedInterfaces.isEmpty()) {
            NodeList<ClassOrInterfaceType> implemented = new NodeList();
            for (ComponentBase implementedComponent : implementedInterfaces) {
                implemented.add(JavaParser.parseClassOrInterfaceType(implementedComponent.getName()));
            }
            declaration.setImplementedTypes(implemented);
        }

        declaration.setMembers(new NodeList<BodyDeclaration<?>>());

        cu.getTypes().add(declaration);
    }

    private static ComponentBase getExtendedClass(ClassComponent component) {
        for (RelationBase relation : component.getParentDiagram().getRelations()) {
            if (relation instanceof IsRelation && relation.getSource() == component) {
                return relation.getTarget();
            }
        }
        return null;
    }

    private static List<ComponentBase> getImplementedInterfaces(ClassComponent component) {
        List<ComponentBase> implemented = new LinkedList<>();
        for (RelationBase relation : component.getParentDiagram().getRelations()) {
            if (relation instanceof ImplementsRelation && relation.getSource() == component) {
                implemented.add(relation.getTarget());
            }
        }
        return implemented;
    }

    @Override
    protected CompilationUnit updateCode(ClassComponent component, MyClassDiagramRenameTable renames, CompilationUnit cu) {
        System.out.println("Update class "+component.getSignature());
        String parentPackage = component.getFullParentPackage();
        if (!parentPackage.isEmpty()) {
            cu.setPackageDeclaration(parentPackage);
        }
        updateSkeleton(component, cu);
        FieldCodeGenerator.updateFields(component, renames.getComponentRenames().getMembersRenameTable(component), renames.getRelationRenames(), cu);
        ConstructorCodeGenerator.updateConstructors(component, renames.getComponentRenames().getMembersRenameTable(component), cu);
        MethodCodeGenerator.updateMethods(component, renames.getComponentRenames().getMembersRenameTable(component), cu);

        return cu;
    }
    
    private static void updateSkeleton(ClassComponent component, CompilationUnit cu) {
        NodeList<TypeDeclaration<?>> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            if (type instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration declaration = (ClassOrInterfaceDeclaration) type;
                declaration.setName(component.getName());
                
                updateVisibility(declaration, component.getVisibility());
                
                declaration.setModifier(Modifier.ABSTRACT, component.isAbstract());
                declaration.setModifier(Modifier.FINAL, component.isFinal());
                declaration.setModifier(Modifier.STATIC, component.isStatic());
                
                ComponentBase extendedClass = getExtendedClass(component);
                if (extendedClass != null) {
                    NodeList<ClassOrInterfaceType> extended = new NodeList();
                    extended.add(JavaParser.parseClassOrInterfaceType(extendedClass.getName()));
                    declaration.setExtendedTypes(extended);
                }
                
                updateImplementedInterfaces(component,declaration);
            }
            // Process only the first one
            // TODO what if there are more classes in one file
            break;
        }
    }
    
    /**
     * Update implemented inferfaces
     * @param component
     * @param declaration
     * @return true if changes were made
     */
    private static boolean updateImplementedInterfaces(ClassComponent component,ClassOrInterfaceDeclaration declaration) 
    {
        NodeList<ClassOrInterfaceType> implementedTypes = declaration.getImplementedTypes();
        List<ComponentBase> implementedInterfaces = getImplementedInterfaces(component);
        if(implementedTypes.size() == implementedInterfaces.size()) {
            boolean similar = true;
            for (ComponentBase implementedComponent : implementedInterfaces) {
                ClassOrInterfaceType type = JavaParser.parseClassOrInterfaceType(implementedComponent.getName());
                boolean found = false;
                for(ClassOrInterfaceType implementedType : implementedTypes) {
                    if (implementedType.equals(type)) {
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    similar = false;
                    break;
                }
            }
            if (similar) {
                return false;
            }
        }
        NodeList<ClassOrInterfaceType> implemented = new NodeList();
        if (!implementedInterfaces.isEmpty()) {
            for (ComponentBase implementedComponent : implementedInterfaces) {
                implemented.add(JavaParser.parseClassOrInterfaceType(implementedComponent.getName()));
            }
        }
        declaration.setImplementedTypes(implemented);
        return true;
    }
    
    private static void updateVisibility(TypeDeclaration declaration, Visibility visibility) {
        declaration.removeModifier(Modifier.PUBLIC,Modifier.PROTECTED,Modifier.PRIVATE);
        switch (visibility) {
            case PUBLIC:
                declaration.setModifier(Modifier.PUBLIC, true);
                break;
            case PROTECTED:
                declaration.setModifier(Modifier.PROTECTED, true);
                break;
            case PRIVATE:
                declaration.setModifier(Modifier.PRIVATE, true);
                break;
        }
    }
}
