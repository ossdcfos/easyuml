package org.uml.newcode;

import java.io.File;
import java.util.HashMap;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.EnumComponent;
import org.uml.model.components.InterfaceComponent;
import org.uml.newcode.renaming.ComponentRenameTable;
import org.uml.newcode.renaming.MemberRenameTable;

/**
 *
 * @author Boris
 */
public class ClassDiagramCodeGenerator {

    public static void generateOrUpdateCode(ClassDiagram classDiagram, String projectPath) {
        String sourcePath = projectPath + "src" + File.separator;
        for (ComponentBase component : classDiagram.getComponents()) {
            // add listener to component, if non-existant
            if(!component.listenerTypeExists(ComponentRenameTable.class)){
                component.addPropertyChangeListener(new ComponentRenameTable());
            }
            
            // add member rename table for component, if non-existant
            String signature = component.getSignature();
            if (!MemberRenameTable.members.containsKey(signature)) {
                MemberRenameTable.members.put(signature, new HashMap<String, String>());
            }

            // generate code
            if (component instanceof ClassComponent) {
                ClassCodeGenerator.generateOrUpdateCode((ClassComponent) component, sourcePath);
            } else if (component instanceof InterfaceComponent) {
                InterfaceCodeGenerator.generateOrUpdateCode((InterfaceComponent) component, sourcePath);
            } else if (component instanceof EnumComponent) {
                EnumCodeGenerator.generateOrUpdateCode((EnumComponent) component);
            }
        }
        // clear tables after everything has been generated, because the data in them has already been applied to code
        ComponentRenameTable.clear();
        MemberRenameTable.clear();
    }
}
