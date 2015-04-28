package org.uml.reveng;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.loaders.DataFolder;
import org.openide.loaders.DataObject;
import org.uml.model.ClassDiagram;

@ActionID(
        category = "Source",
        id = "org.uml.reveng.ReverseEngineerPackageAction"
)
@ActionRegistration(
        displayName = "#CTL_ReverseEngineerPackageAction"
)
@ActionReference(path = "Projects/package/Actions", position = 1050)
@Messages("CTL_ReverseEngineerPackageAction=easyUML Create Class Diagram")
/**
 * Class that responds to Reverse Engineer command from package context menu
 *
 * @author Boris Perović
 */
public final class ReverseEngineerPackageAction extends ReverseEngineerActionBase implements ActionListener {

    private final List<DataFolder> context;

    /**
     * Default constructor
     *
     * @param context in which the command is issued
     */
    public ReverseEngineerPackageAction(List<DataFolder> context) {
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
        List<File> files = new LinkedList<>();
        for (DataObject dataObject : context) {
            String path = dataObject.getPrimaryFile().getPath();  // folders are separated with forward slashes (/), system separator is backslash \
            String extension = "java";
            files.addAll(FileUtils.listFiles(new File(path), new String[]{extension}, true));
        }

        String diagramName;
        if (context.size() == 1) {
            String path = context.get(0).getPrimaryFile().getPath();
            diagramName = path.substring(path.lastIndexOf("src") + 4).replace("/", ".");;
        } else {
            diagramName = "Multi-package class diagram";
        }
        ClassDiagram classDiagram = ReverseEngineer.createClassDiagramFromFiles(files, diagramName);
        
        openReverseEngineerDialog(classDiagram);
    }
}
