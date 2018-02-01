package org.uml.newcode;

import org.uml.newcode.components.InterfaceCodeGenerator;
import org.uml.newcode.components.ClassCodeGenerator;
import org.uml.newcode.components.EnumCodeGenerator;
import java.io.File;
import org.uml.filetype.cdg.renaming.MyClassDiagramRenameTable;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.EnumComponent;
import org.uml.model.components.InterfaceComponent;

/**
 * Code generator for the class diagram.
 *
 * @author Boris Perović
 */
public class ClassDiagramCodeGenerator {

    /**
     * Generates class diagram code if it does not exist in the selected project
     * or updates the existing code.
     *
     * @param classDiagram to generate code from
     * @param renames table of renamed components for updating of the existing code
     * @param srcPath to save generated code to
     */
    public static void generateOrUpdateCode(ClassDiagram classDiagram, MyClassDiagramRenameTable renames, String srcPath) {
        classDiagram.updateComponentPackages();
        for (ComponentBase component : classDiagram.getComponents()) {
            // Generate code
            if (component instanceof ClassComponent) {
                ClassCodeGenerator.getInstance().generateOrUpdateCode((ClassComponent) component, renames, srcPath);
            } else if (component instanceof InterfaceComponent) {
                InterfaceCodeGenerator.getInstance().generateOrUpdateCode((InterfaceComponent) component, renames, srcPath);
            } else if (component instanceof EnumComponent) {
                EnumCodeGenerator.getInstance().generateOrUpdateCode((EnumComponent) component, renames, srcPath);
            }
        }
        // Clear tables after everything has been generated, because the data in them has already been applied to code
        renames.clear();
    }
}
