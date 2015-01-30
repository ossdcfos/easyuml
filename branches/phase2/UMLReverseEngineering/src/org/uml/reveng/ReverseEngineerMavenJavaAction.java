package org.uml.reveng;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static java.io.File.separator;
import org.netbeans.api.project.Project;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.uml.model.ClassDiagram;
import org.uml.visual.UMLTopComponent;
import org.openide.filesystems.FileObject;
import org.openide.awt.ActionReferences;

@ActionID(
        category = "Source",
        id = "org.uml.reveng.ReverseEngineerMavenJavaAction")
@ActionRegistration(
        displayName = "#CTL_ReverseEngineerMavenJavaAction")
@ActionReferences({
//    @ActionReference(path = "Menu/Source", position = 50),
    @ActionReference(path = "Projects/org-netbeans-modules-maven/Actions", position = 1050)
})
@Messages("CTL_ReverseEngineerMavenJavaAction=easyUML Create Class Diagram")
/**
 * Class that responds to Reverse Engineer command from Java Project context menu
 *
 * @author Boris
 */
public final class ReverseEngineerMavenJavaAction extends ReverseEngineerActionBase implements ActionListener {

    private final Project context;

    /**
     * Default constructor
     *
     * @param context in which the command is issued
     */
    public ReverseEngineerMavenJavaAction(Project context) {
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
        String path = context.getProjectDirectory().getPath() + separator + "src" + separator + "main";
        String name = context.getProjectDirectory().getName();        
        ClassDiagram classDiagram = ReverseEngineer.createClassDiagramFromPath(path, name);
        UMLTopComponent umlTopComponent = new UMLTopComponent(classDiagram);
//        arrangeDiagram(umlTopComponent);
        
        //Save the diagram into project's path
        FileObject projectFolder = context.getProjectDirectory();
        saveDiagramAndOpenTopComponent(projectFolder, umlTopComponent);
    }
}
