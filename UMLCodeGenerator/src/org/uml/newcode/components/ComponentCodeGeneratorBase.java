package org.uml.newcode.components;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ParseProblemException;
import com.github.javaparser.Token;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.openide.util.Exceptions;
import org.uml.filetype.cdg.renaming.MyClassDiagramRenameTable;
import org.uml.model.components.ComponentBase;

/**
 * Base component code generation class. Implements common logic about whether
 * the source should be generated or updated.
 * @author Boris Perović
 * @param <T> type of the component to generate the code for
 */
public abstract class ComponentCodeGeneratorBase<T extends ComponentBase> {
    
    protected abstract CompilationUnit generateCode(T component);
    protected abstract CompilationUnit updateCode(T component, MyClassDiagramRenameTable renames, CompilationUnit cu);

    /**
     * Generates the code of the component to the given path.
     *
     * @param component to generate code from
     * @param renames
     * @param sourcePath to save code to
     */
    public void generateOrUpdateCode(T component, MyClassDiagramRenameTable renames, String sourcePath) {

        File sourceFile;
        // If the component has been renamed, the source file should have the old name
        if (renames.getComponentRenames().contains(component)) {  //-> new to old
            String oldFullQualifiedName = renames.getComponentRenames().getOriginalSignature(component);
            String pathToOldFile = sourcePath + oldFullQualifiedName.replace(".", File.separator) + ".java";
            sourceFile = new File(pathToOldFile);
        } // If the component has not been renamed, the source should have the new name
        else {
            String fullQualifiedName = component.getSignature();
            String pathToFile = sourcePath + fullQualifiedName.replace(".", File.separator) + ".java";
            sourceFile = new File(pathToFile);
        }

        // If source exists, update code
        CompilationUnit code = null;
        if (sourceFile.exists()) {
            try {
                CompilationUnit cu;
                try (FileReader fileReader = new FileReader(sourceFile)) {
                    cu = JavaParser.parse(fileReader);
                    LexicalPreservingPrinter.setup(cu);      
                }
                code = updateCode(component, renames, cu);
            } catch (ParseProblemException ex) {
                JOptionPane.showMessageDialog(null, "Parse problem. Cannot update!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FileNotFoundException ex) {
                // Already checked for file existance, but if file is somehow deleted, generate code.
                code = generateCode(component);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "IOException!", "Error", JOptionPane.ERROR_MESSAGE);
                Exceptions.printStackTrace(ex);
            }
        } // If source does not exist, generate code from scratch
        else {
            code = generateCode(component);
        }
        
        if (code == null) {
            return;
        }

        // Add package to the path
        String packag = component.getFullParentPackage();
        String fullPath;
        if (!packag.equals("")) {
            String packagePath = packag.replace(".", File.separator);
            fullPath = sourcePath + packagePath + File.separator;
        } else {
            fullPath = sourcePath;
        }
        
        String name = component.getName();
        // Try to generate while preserving presentation
        String output;
        try {
            output = LexicalPreservingPrinter.print(code);
        }
        catch(Exception ex) {
            int result = JOptionPane.showConfirmDialog(null, 
                "Code for "+name+" cannot be generated with lexical preservation.\n"
              + "Many spaces and line returnes will diseapear.\n"
              + "Generate code anyway ?",
                "Problem in code generation",
                JOptionPane.YES_NO_OPTION);
            if (result != JOptionPane.YES_OPTION)
                return;
            output = code.toString();
        }

        // Create path folder structure
        new File(fullPath).mkdirs();

        // Write-out the source file
        File outSourceFile = new File(fullPath + name + ".java");
        try {
            if (sourceFile.exists()) {
                sourceFile.delete();
            }
            FileUtils.writeStringToFile(outSourceFile, output);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cannot write file " + outSourceFile.getName() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // TODO Format the source file
//        try {
//            Convention.getInstance().put(ConventionKeys.SORT, "false");
//            Jalopy jlp = new Jalopy();
//            jlp.setInput(outSourceFile);
//            jlp.setOutput(outSourceFile);
//            jlp.format();
//        } catch (FileNotFoundException ex) {
//        }
    }
}
