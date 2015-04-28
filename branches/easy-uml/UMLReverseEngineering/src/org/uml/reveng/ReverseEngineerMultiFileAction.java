package org.uml.reveng;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import org.openide.loaders.DataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.filesystems.FileUtil;
import org.openide.util.NbBundle.Messages;
import org.uml.model.ClassDiagram;

@ActionID(
        category = "Source",
        id = "org.uml.reveng.ReverseEngineerMultiFileAction"
)
@ActionRegistration(
        displayName = "#CTL_ReverseEngineerMultiFileAction"
)
@ActionReference(path = "Loaders/text/x-java/Actions", position = 2550)
@Messages("CTL_ReverseEngineerMultiFileAction=easyUML Generate Class Diagram")
/**
 * Class that responds to Reverse Engineer command from multi Java file selection
 *
 * @author Boris Perović Perović
 */
public final class ReverseEngineerMultiFileAction extends ReverseEngineerActionBase implements ActionListener {

    private final List<DataObject> context;

    public ReverseEngineerMultiFileAction(List<DataObject> context) {
        this.context = context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        List<File> files = new LinkedList<>();
        for (DataObject dataObject : context) {
            files.add(FileUtil.toFile(dataObject.getPrimaryFile()));
        }

        String diagramName;
        if(context.size() == 1) {
            diagramName = context.get(0).getName();
        } else {
            diagramName = "Multi-file class diagram";
        }
        ClassDiagram classDiagram = ReverseEngineer.createClassDiagramFromFiles(files, diagramName);
        
        openReverseEngineerDialog(classDiagram);
    }
}
