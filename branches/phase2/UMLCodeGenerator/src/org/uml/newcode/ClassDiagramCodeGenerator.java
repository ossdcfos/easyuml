package org.uml.newcode;

import static java.io.File.separator;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.EnumComponent;
import org.uml.model.components.InterfaceComponent;
import org.uml.newcode.renaming.ComponentRenameTable;

/**
 *
 * @author Boris
 */
public class ClassDiagramCodeGenerator {

    public static String SOURCE_PATH = "C:" + separator + "tempProj" + separator + "src" + separator;

    public static void generateOrUpdateCode(ClassDiagram classDiagram) {
        for (ComponentBase component : classDiagram.getComponents()) {
            component.addPropertyChangeListener(new ComponentRenameTable());
            
            if (component instanceof ClassComponent) {
                ClassCodeGenerator.generateOrUpdateCode((ClassComponent) component);
            } else if (component instanceof InterfaceComponent) {
                InterfaceCodeGenerator.generateOrUpdateCode((InterfaceComponent) component);
            } else if (component instanceof EnumComponent) {
                EnumCodeGenerator.generateCode((EnumComponent) component);
            }
        }

    }
}
