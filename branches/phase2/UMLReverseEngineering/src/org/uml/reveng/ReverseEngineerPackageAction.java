package org.uml.reveng;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.uml.model.ClassDiagram;
import org.uml.visual.UMLTopComponent;
import org.openide.filesystems.FileObject;
import org.openide.awt.ActionReferences;
import org.openide.loaders.DataFolder;

@ActionID(
        category = "Source",
        id = "org.uml.reveng.ReverseEngineerPackageAction")
@ActionRegistration(
        displayName = "#CTL_ReverseEngineerPackageAction")
@ActionReferences({
    @ActionReference(path = "Projects/package/Actions", position = 1050)
})
@Messages("CTL_ReverseEngineerPackageAction=easyUML Create Class Diagram")
/**
 * Class that responds to Reverse Engineer command from package context menu
 *
 * @author Boris
 */
public final class ReverseEngineerPackageAction extends ReverseEngineerActionBase implements ActionListener {

    private final DataFolder context;

    /**
     * Default constructor
     *
     * @param context in which the command is issued
     */
    public ReverseEngineerPackageAction(DataFolder context) {
        this.context = context;
    }

    /**
     * Main method for starting the Reverse engineering process.
     * <p>
     * It finds the source folder and starts compilation upon that path and also
     * defines a file system separator based on Operating system. When The
     * compilation finishes, it checks if any .class fines have been generated
     * and if there are any, proceeds to making proceeds to saving the diagram
     * into a specified folder in the root of the project as a .cdg file.
     *
     * @param ev action event that occurred (button pressed into main window)
     */
    @Override
    public void actionPerformed(ActionEvent ev) {
        String path = context.getPrimaryFile().getPath();  // folders are separated with forward slashes (/), system separator is backslash \
        String name = path.substring(path.lastIndexOf("src") + 4).replace("/", ".");
        ClassDiagram classDiagram = ReverseEngineer.createClassDiagramFromPath(path, name);
        UMLTopComponent umlTopComponent = new UMLTopComponent(classDiagram);
//        arrangeDiagram(umlTopComponent);
        
        // Find root folder
        FileObject parent = context.getPrimaryFile().getParent();
        while(!parent.getName().equals("src")){
            parent = parent.getParent();
        }
        
        //Save the diagram into project's path
        FileObject projectFolder = parent.getParent();
        saveDiagramAndOpenTopComponent(projectFolder, umlTopComponent);
    }
}
