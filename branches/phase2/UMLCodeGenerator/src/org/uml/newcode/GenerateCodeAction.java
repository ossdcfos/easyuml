package org.uml.newcode;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.loaders.DataObject;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.WindowManager;
import org.uml.filetype.cdg.ClassDiagramDataObject;

@ActionID(
        category = "Source",
        id = "org.uml.newcode.GenerateCodeAction"
)
@ActionRegistration(
        displayName = "#CTL_GenerateCodeAction"
)
@ActionReference(path = "Loaders/text/x-cdg/Actions", position = 1150)
@Messages("CTL_GenerateCodeAction=easyUML generate code")
public final class GenerateCodeAction implements ActionListener {

    private final ClassDiagramDataObject context;

    public GenerateCodeAction(DataObject context) {
        this.context = (ClassDiagramDataObject) context;
    }

    @Override
    public void actionPerformed(ActionEvent ev) {
        GenerateCodeDialog dialog = new GenerateCodeDialog(context.getClassDiagram(), context.getRenames());
        dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
        dialog.setVisible(true);
    }
}
