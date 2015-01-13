package org.uml.newcode.components;

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
import org.uml.newcode.CodeGeneratorUtils;
import org.uml.newcode.renaming.ComponentRenameTable;

/**
 *
 * @author Boris
 */
public class InterfaceCodeGenerator {

    public static void generateOrUpdateCode(InterfaceComponent component, String sourcePath) {
        
        String code = "";
        File sourceFile;
        // if component has been renamed, the source file should have the old name
        if (ComponentRenameTable.components.containsKey(component.getSignature())) {  //-> new to old
            String newSignature = component.getSignature();
            String oldFullQualifiedName = ComponentRenameTable.components.get(newSignature);
            String pathToOldFile = sourcePath + oldFullQualifiedName.replace(".", File.separator) + ".java";
            sourceFile = new File(pathToOldFile);
        } 
        // if the component has not been renamed, the source should have the new name
        else {
            String fullQualifiedName = component.getSignature();
            String pathToFile = sourcePath + fullQualifiedName.replace(".", File.separator) + ".java";
            sourceFile = new File(pathToFile);
        }

        // if source exists, update code
        if (sourceFile.exists()) {
//            try {
//                FileReader fileReader = new FileReader(sourceFile);
//                CompilationUnit cu = JavaParser.parse(fileReader, true);
//                fileReader.close();
//                code = updateCode(component, cu);
//                sourceFile.delete();
//            } catch (ParseException ex) {
//                Token tok = ex.currentToken;
//                JOptionPane.showMessageDialog(null, "Malformed code at line " + tok.beginLine + " column " + tok.beginColumn + ". Cannot update!", "Error", JOptionPane.ERROR_MESSAGE);
//            } catch (FileNotFoundException ex) {
//                // Already checked for file existance, but if file is somehow deleted, generate code.
                code = generateCode(component);
//            } catch (IOException ex) {
//                JOptionPane.showMessageDialog(null, "IOException!", "Error", JOptionPane.ERROR_MESSAGE);
//                Exceptions.printStackTrace(ex);
//            }
        } 
        // if source does not exist, generate code from scratch
        else {
            code = generateCode(component);
        }

        // Add package to path
        String pack = component.getParentPackage();
        String fullPackagePath;
        if (!pack.equals("")) {
            String packagePath = pack.replace(".", File.separator);
            fullPackagePath = sourcePath + packagePath + File.separator;
        } else {
            fullPackagePath = sourcePath;
        }

        // Create path folder structure
        new File(fullPackagePath).mkdirs();

        String name = component.getName();
        // Write-out the source file
        File outSourceFile = new File(fullPackagePath + name + ".java");
        try {
            FileUtils.writeStringToFile(outSourceFile, code);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cannot write file " + outSourceFile.getName() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Format the source file
        try {
            Jalopy jlp = new Jalopy();
            jlp.setInput(outSourceFile);
            jlp.setOutput(outSourceFile);
            jlp.format();
        } catch (FileNotFoundException ex) {
        }
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

        ComponentBase extendedComponent = getInterfaceThatIsExtended(component);
        if (extendedComponent != null) {
            List<ClassOrInterfaceType> extended = new LinkedList<>();
            extended.add(new ClassOrInterfaceType(extendedComponent.getName()));
            declaration.setExtends(extended);
        }

        declaration.setMembers(new LinkedList<BodyDeclaration>());

        cu.getTypes().add(declaration);
    }

    private static ComponentBase getInterfaceThatIsExtended(InterfaceComponent component) {
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
