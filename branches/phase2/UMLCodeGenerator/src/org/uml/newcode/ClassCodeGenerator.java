package org.uml.newcode;

import de.hunsicker.jalopy.Jalopy;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import java.io.File;
import static java.io.File.separator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.openide.util.Exceptions;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;
import org.uml.model.relations.CardinalityEnum;
import org.uml.model.relations.HasBaseRelation;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.RelationBase;

/**
 *
 * @author Boris
 */
class ClassCodeGenerator {

    public static void generateOrUpdateCode(ClassComponent component) {
        String folderPath = ClassDiagramCodeGenerator.SOURCE_PATH;
        String pack = component.getParentPackage();
        if (!pack.equals("")) {
            String packagePath = pack.replace(".", separator);
            folderPath += packagePath + separator;
        }
        new File(folderPath).mkdirs();
        String name = component.getName();
        File sourceFile = new File(folderPath + name + ".java");

        String code = "";
        if (sourceFile.exists()) {
//            CompilationUnit cu;
//            try {
//                cu = JavaParser.parse(new FileReader(sourceFile), true);
//                code = updateCode(component, cu);
//            } catch (ParseException ex) {
//                Token tok = ex.currentToken;
//                int line = tok.beginLine;
//                int column = tok.beginColumn;
//                JOptionPane.showMessageDialog(null, "Malformed code at line " + line + " column " + column + ". Cannot update!", "Error", JOptionPane.ERROR_MESSAGE);
//            } catch (FileNotFoundException ex) {
//                // Already checked for file existance, but if file is somehow deleted, generate code.
//                code = generateCode(component);
//            }
            // temp
            code = generateCode(component);
        } else {
            code = generateCode(component);
        }

        try {
            FileUtils.writeStringToFile(sourceFile, code);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cannot write file " + sourceFile.getName() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        try {
            Jalopy jlp = new Jalopy();
            jlp.setInput(sourceFile);
            jlp.setOutput(sourceFile);
            jlp.format();
        } catch (FileNotFoundException ex) {
        }
    }

    private static String generateCode(ClassComponent component) {
        CompilationUnit cu = new CompilationUnit();
        cu.setTypes(new LinkedList<TypeDeclaration>());
        if (!component.getParentPackage().equals("")) cu.setPackage(new PackageDeclaration(new NameExpr(component.getParentPackage())));
        createHeader(component, cu);
        createFields(component, cu);
        createConstructors(component, cu);
        createMethods(component, cu);

        return cu.toString();

////        fields += getFieldsFromUseRelations(relevantRelations);
    }

    private static ComponentBase getClassThatIsExtended(ClassComponent component) {
        for (RelationBase relation : component.getParentDiagram().getRelations()) {
            if (relation instanceof IsRelation) {
                return relation.getTarget();
            }
        }
        return null;
    }

    private static List<ComponentBase> getClassesThatAreImplemented(ClassComponent component) {
        List<ComponentBase> implemented = new LinkedList<>();
        for (RelationBase relation : component.getParentDiagram().getRelations()) {
            if (relation instanceof ImplementsRelation) {
                implemented.add(relation.getTarget());
            }
        }
        return implemented;
    }

    public static List<RelationBase> getRelevantRelations(ComponentBase component) {
        List<RelationBase> relevantRelations = new LinkedList<>();
        for (RelationBase rc : component.getParentDiagram().getRelations()) {
            if (rc.getSource().equals(component)) {
                relevantRelations.add(rc);
            }
        }
        return relevantRelations;
    }

    private static void createHeader(ClassComponent component, CompilationUnit cu) {
        ClassOrInterfaceDeclaration declaration = new ClassOrInterfaceDeclaration();
        declaration.setName(component.getName());
        switch (component.getVisibility()) {
            case PUBLIC:
                declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PUBLIC));
                break;
            case PROTECTED:
                declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PROTECTED));
                break;
            case PRIVATE:
                declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PRIVATE));
                break;
        }
        if (component.isAbstract()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.ABSTRACT));
        if (component.isStatic()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
        if (component.isFinal()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.FINAL));

        ComponentBase extendedComponent = getClassThatIsExtended(component);
        if (extendedComponent != null) {
            List<ClassOrInterfaceType> extended = new LinkedList<>();
            extended.add(new ClassOrInterfaceType(extendedComponent.getName()));
            declaration.setExtends(extended);
        }
        List<ComponentBase> implementedComponents = getClassesThatAreImplemented(component);
        if (!implementedComponents.isEmpty()) {
            List<ClassOrInterfaceType> implemented = new LinkedList<>();
            for (ComponentBase implementedComponent : implementedComponents) {
                implemented.add(new ClassOrInterfaceType(implementedComponent.getName()));
            }
            declaration.setImplements(implemented);
        }

        declaration.setMembers(new LinkedList<BodyDeclaration>());

        cu.getTypes().add(declaration);
    }

    private static void createFields(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        for (Field field : component.getFields()) {
            FieldDeclaration declaration = new FieldDeclaration();
            VariableDeclarator variable = new VariableDeclarator(new VariableDeclaratorId(field.getName()));
            List<VariableDeclarator> variables = new LinkedList<>();
            variables.add(variable);
            declaration.setVariables(variables);
            String type = field.getType();
            declaration.setType(parseType(type));
            if (field.isStatic()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
            if (field.isFinal()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.FINAL));
            if (field.isVolatile()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.VOLATILE));
            if (field.isTransient()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.TRANSIENT));
            members.add(declaration);
        }

        for (RelationBase relation : getRelevantRelations(component)) {
            if (relation instanceof HasBaseRelation) {
                HasBaseRelation hasRelation = (HasBaseRelation) relation;
                String fieldType = hasRelation.getTarget().getName();
                String fieldName;
                if (!hasRelation.getName().equals("")) fieldName = hasRelation.getName();
                else fieldName = (fieldType.substring(0, 1)).toLowerCase() + fieldType.substring(1);

                StringBuilder fieldSignature = new StringBuilder();
                if (hasRelation.getCardinalityTarget().equals(CardinalityEnum.One2Many) || hasRelation.getCardinalityTarget().equals(CardinalityEnum.Zero2Many)) {
                    fieldSignature.append(hasRelation.getCollectionType()).append("<").append(fieldType).append("> ").append(fieldName);
                    if (!hasRelation.getName().equals("")) fieldSignature.append("s");
                    fieldSignature.append(";");
                } else {
                    fieldSignature.append(fieldType).append(" ").append(fieldName).append(";");
                }
                String signature = fieldSignature.toString();

                try {
                    BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature);
                    FieldDeclaration declaration = (FieldDeclaration) bd;
                    members.add(declaration);
                } catch (ParseException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
    }

    private static void createConstructors(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        for (Constructor constructor : component.getConstructors()) {
            ConstructorDeclaration declaration = new ConstructorDeclaration();
            declaration.setName(component.getName());
            switch (constructor.getVisibility()) {
                case PUBLIC:
                    declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PUBLIC));
                    break;
                case PROTECTED:
                    declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PROTECTED));
                    break;
                case PRIVATE:
                    declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PRIVATE));
                    break;
            }
            if (!constructor.getArguments().isEmpty()) {
                List<Parameter> parameters = new LinkedList<>();
                for (MethodArgument argument : constructor.getArguments()) {
                    Parameter parameter = new Parameter();
                    String type = argument.getType();
                    parameter.setType(parseType(type));
                    parameter.setId(new VariableDeclaratorId(argument.getName()));
                    parameters.add(parameter);
                }
                declaration.setParameters(parameters);
            }
            members.add(declaration);
        }

    }

    private static void createMethods(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        for (Method method : component.getMethods()) {
            MethodDeclaration declaration = new MethodDeclaration();
            declaration.setName(method.getName());
            String returnType = method.getType();
            declaration.setType(parseType(returnType));
            switch (method.getVisibility()) {
                case PUBLIC:
                    declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PUBLIC));
                    break;
                case PROTECTED:
                    declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PROTECTED));
                    break;
                case PRIVATE:
                    declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PRIVATE));
                    break;
            }
            if (method.isStatic()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
            if (method.isFinal()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.FINAL));
            if (method.isAbstract()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.ABSTRACT));
            if (method.isSynchronized()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.SYNCHRONIZED));

            if (!method.getArguments().isEmpty()) {
                List<Parameter> parameters = new LinkedList<>();
                for (MethodArgument argument : method.getArguments()) {
                    Parameter parameter = new Parameter();
                    String type = argument.getType();
                    parameter.setType(parseType(type));
                    parameter.setId(new VariableDeclaratorId(argument.getName()));
                    parameters.add(parameter);
                }
                declaration.setParameters(parameters);
            }
            members.add(declaration);
        }

        for (RelationBase relation : getRelevantRelations(component)) {
            if (relation instanceof ImplementsRelation) {
                InterfaceComponent target = (InterfaceComponent) relation.getTarget();
                for (Method method : target.getMethods()) {
                    MethodDeclaration declaration = new MethodDeclaration();
                    declaration.setName(method.getName());
                    String returnType = method.getType();
                    declaration.setType(parseType(returnType));
                    switch (method.getVisibility()) {
                        case PUBLIC:
                            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PUBLIC));
                            break;
                        case PROTECTED:
                            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PROTECTED));
                            break;
                        case PRIVATE:
                            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PRIVATE));
                            break;
                    }
                    if (method.isStatic()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
                    if (method.isFinal()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.FINAL));
                    if (method.isAbstract()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.ABSTRACT));
                    if (method.isSynchronized()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.SYNCHRONIZED));

                    if (!method.getArguments().isEmpty()) {
                        List<Parameter> parameters = new LinkedList<>();
                        for (MethodArgument argument : method.getArguments()) {
                            Parameter parameter = new Parameter();
                            String type = argument.getType();
                            parameter.setType(parseType(type));
                            parameter.setId(new VariableDeclaratorId(argument.getName()));
                            parameters.add(parameter);
                        }
                        declaration.setParameters(parameters);
                    }
                    if (!members.contains(declaration)) {
                        members.add(declaration);
                    }
                }
            }
        }
    }

    private static Type parseType(String typeString) {
        Type type = null;

        if (typeString.contains("void")) type = new VoidType();
        else {
            try {
                BodyDeclaration bd = JavaParser.parseBodyDeclaration(typeString + " field;");
                FieldDeclaration declaration = (FieldDeclaration) bd;
                type = declaration.getType();
            } catch (ParseException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return type;
    }

//    private static String updateCode(ClassComponent component, CompilationUnit cu) {
//        updateHeader(component, cu);
//        updateFields(component, cu);
////        updateConstructors(cu);
////        updateMethods(cu);
//
//        return cu.toString();
//    }
//    private static void updateHeader(ClassComponent component, CompilationUnit cu) {
//        List<TypeDeclaration> types = cu.getTypes();
//        for (TypeDeclaration type : types) {
//            if (type instanceof ClassOrInterfaceDeclaration) {
//                ClassOrInterfaceDeclaration cidecl = (ClassOrInterfaceDeclaration) type;
//                cidecl.setName(component.getName());
//
//                if (component.isAbstract()) cidecl.setModifiers(ModifierSet.addModifier(cidecl.getModifiers(), ModifierSet.ABSTRACT));
//                else cidecl.setModifiers(ModifierSet.removeModifier(cidecl.getModifiers(), ModifierSet.ABSTRACT));
//                if (component.isFinal()) cidecl.setModifiers(ModifierSet.addModifier(cidecl.getModifiers(), ModifierSet.FINAL));
//                else cidecl.setModifiers(ModifierSet.removeModifier(cidecl.getModifiers(), ModifierSet.FINAL));
//                if (component.isStatic()) cidecl.setModifiers(ModifierSet.addModifier(cidecl.getModifiers(), ModifierSet.STATIC));
//                else cidecl.setModifiers(ModifierSet.removeModifier(cidecl.getModifiers(), ModifierSet.STATIC));
//
//                ComponentBase extendedComponent = getClassThatIsExtended(component);
//                if (extendedComponent != null) {
//                    List<ClassOrInterfaceType> extended = new LinkedList<>();
//                    extended.add(new ClassOrInterfaceType(extendedComponent.getName()));
//                    cidecl.setExtends(extended);
//                }
//                List<ComponentBase> implementedComponents = getClassesThatAreImplemented(component);
//                if (!implementedComponents.isEmpty()) {
//                    List<ClassOrInterfaceType> implemented = new LinkedList<>();
//                    for (ComponentBase implementedComponent : implementedComponents) {
//                        implemented.add(new ClassOrInterfaceType(implementedComponent.getName()));
//                    }
//                    cidecl.setImplements(implemented);
//                }
//            }
//        }
//    }
//    private static void updateFields(ClassComponent component, CompilationUnit cu) {
//        List<TypeDeclaration> types = cu.getTypes();
//        for (TypeDeclaration type : types) {
//            if (type instanceof ClassOrInterfaceDeclaration) {
//                List<BodyDeclaration> members = type.getMembers();
//                for (BodyDeclaration member : members) {
//                    if (member instanceof FieldDeclaration) {
//                        FieldDeclaration field = (FieldDeclaration) member;
//                        changeField(component, field);
//                    }
//                }
//            }
//        }
//    }
//
//    private static void changeField(ClassComponent component, FieldDeclaration field) {
//
//    }
}
