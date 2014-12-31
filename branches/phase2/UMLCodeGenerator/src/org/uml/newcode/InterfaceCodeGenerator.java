package org.uml.newcode;

import de.hunsicker.jalopy.Jalopy;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import java.io.File;
import static java.io.File.separator;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.RelationBase;

/**
 *
 * @author Boris
 */
class InterfaceCodeGenerator {

    public static void generateOrUpdateCode(InterfaceComponent component, String sourcePath) {
//        String pack = component.getParentPackage();
//        if (!pack.equals("")) {
//            String packagePath = pack.replace(".", separator);
//            sourcePath += packagePath + separator;
//        }
//        new File(sourcePath).mkdirs();
//        String name = component.getName();
//        File sourceFile = new File(sourcePath + name + ".java");
//
//        String code = "";
//        if (sourceFile.exists()) {
////            CompilationUnit cu;
////            try {
////                cu = JavaParser.parse(new FileReader(sourceFile), true);
////                code = updateCode(component, cu);
////            } catch (ParseException ex) {
////                Token tok = ex.currentToken;
////                int line = tok.beginLine;
////                int column = tok.beginColumn;
////                JOptionPane.showMessageDialog(null, "Malformed code at line " + line + " column " + column + ". Cannot update!", "Error", JOptionPane.ERROR_MESSAGE);
////            } catch (FileNotFoundException ex) {
////                // Already checked for file existance, but if file is somehow deleted, generate code.
////                code = generateCode(component);
////            }
//            // temp
//            code = generateCode(component);
//        } else {
//            code = generateCode(component);
//        }
//
//        try {
//            FileUtils.writeStringToFile(sourceFile, code);
//        } catch (IOException ex) {
//            JOptionPane.showMessageDialog(null, "Cannot write file " + sourceFile.getName() + "!", "Error", JOptionPane.ERROR_MESSAGE);
//        }
//
//        try {
//            Jalopy jlp = new Jalopy();
//            jlp.setInput(sourceFile);
//            jlp.setOutput(sourceFile);
//            jlp.format();
//        } catch (FileNotFoundException ex) {
//        }
    }

    private static String generateCode(InterfaceComponent component) {
        CompilationUnit cu = new CompilationUnit();
        cu.setTypes(new LinkedList<TypeDeclaration>());
        if (!component.getParentPackage().equals("")) cu.setPackage(new PackageDeclaration(new NameExpr(component.getParentPackage())));
        createHeader(component, cu);
        createMethods(component, cu);

        return cu.toString();
    }

    private static void createHeader(InterfaceComponent component, CompilationUnit cu) {
        ClassOrInterfaceDeclaration declaration = new ClassOrInterfaceDeclaration();
        declaration.setInterface(true);
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
        if (component.isStatic()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));

        ComponentBase extendedComponent = getClassThatIsExtended(component);
        if (extendedComponent != null) {
            List<ClassOrInterfaceType> extended = new LinkedList<>();
            extended.add(new ClassOrInterfaceType(extendedComponent.getName()));
            declaration.setExtends(extended);
        }

        declaration.setMembers(new LinkedList<BodyDeclaration>());

        cu.getTypes().add(declaration);
    }

    private static ComponentBase getClassThatIsExtended(InterfaceComponent component) {
        for (RelationBase relation : component.getParentDiagram().getRelations()) {
            if (relation instanceof IsRelation && relation.getSource() == component) {
                return relation.getTarget();
            }
        }
        return null;
    }

//    public static List<RelationBase> getRelevantRelations(ComponentBase component) {
//        List<RelationBase> relevantRelations = new LinkedList<>();
//        for (RelationBase rc : component.getParentDiagram().getRelations()) {
//            if (rc.getSource().equals(component)) {
//                relevantRelations.add(rc);
//            }
//        }
//        return relevantRelations;
//    }

    private static void createMethods(InterfaceComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        for (Method method : component.getMethods()) {
            MethodDeclaration declaration = new MethodDeclaration();
            declaration.setName(method.getName());
            String returnType = method.getType();
            declaration.setType(CodeGeneratorUtils.parseType(returnType));
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
                    parameter.setType(CodeGeneratorUtils.parseType(type));
                    parameter.setId(new VariableDeclaratorId(argument.getName()));
                    parameters.add(parameter);
                }
                declaration.setParameters(parameters);
            }
            members.add(declaration);
        }

//        for (RelationBase relation : getRelevantRelations(component)) {
//            if (relation instanceof IsRelation) {
//                InterfaceComponent target = (InterfaceComponent) relation.getTarget();
//                for (Method method : target.getMethods()) {
//                    MethodDeclaration declaration = new MethodDeclaration();
//                    declaration.setName(method.getName());
//                    String returnType = method.getType();
//                    declaration.setType(parseType(returnType));
//                    switch (method.getVisibility()) {
//                        case PUBLIC:
//                            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PUBLIC));
//                            break;
//                        case PROTECTED:
//                            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PROTECTED));
//                            break;
//                        case PRIVATE:
//                            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PRIVATE));
//                            break;
//                    }
//                    if (method.isStatic()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
//                    if (method.isFinal()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.FINAL));
//                    if (method.isAbstract()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.ABSTRACT));
//                    if (method.isSynchronized()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.SYNCHRONIZED));
//
//                    if (!method.getArguments().isEmpty()) {
//                        List<Parameter> parameters = new LinkedList<>();
//                        for (MethodArgument argument : method.getArguments()) {
//                            Parameter parameter = new Parameter();
//                            String type = argument.getType();
//                            parameter.setType(parseType(type));
//                            parameter.setId(new VariableDeclaratorId(argument.getName()));
//                            parameters.add(parameter);
//                        }
//                        declaration.setParameters(parameters);
//                    }
//                    if (!members.contains(declaration)) {
//                        members.add(declaration);
//                    }
//                }
//            }
//        }
    }
}
