package org.uml.newcode.components;

import org.uml.newcode.members.LiteralCodeGenerator;
import de.hunsicker.jalopy.Jalopy;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.NameExpr;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.uml.model.components.EnumComponent;
import org.uml.newcode.renaming.ComponentRenameTable;

/**
 *
 * @author Boris
 */
public class EnumCodeGenerator {

    public static void generateOrUpdateCode(EnumComponent component, String sourcePath) {

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

    private static String generateCode(EnumComponent component) {
        CompilationUnit cu = new CompilationUnit();
        cu.setTypes(new LinkedList<TypeDeclaration>());
        if (!component.getParentPackage().equals("")) cu.setPackage(new PackageDeclaration(new NameExpr(component.getParentPackage())));
        createHeader(component, cu);
        LiteralCodeGenerator.createLiterals(component, cu);

        return cu.toString();
    }

    private static void createHeader(EnumComponent component, CompilationUnit cu) {
        EnumDeclaration declaration = new EnumDeclaration();
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
        
        declaration.setEntries(new LinkedList<EnumConstantDeclaration>());
        cu.getTypes().add(declaration);
    }
}
