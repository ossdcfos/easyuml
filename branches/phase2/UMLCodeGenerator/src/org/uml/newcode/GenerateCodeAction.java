package org.uml.newcode;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.WindowManager;
import org.uml.model.ClassDiagram;

@ActionID(
        category = "Source",
        id = "org.uml.newcode.GenerateCodeAction"
)
@ActionRegistration(
        displayName = "#CTL_GenerateCodeAction"
)
@ActionReference(path = "Menu/Source", position = -50)
@Messages("CTL_GenerateCodeAction=easyUML Generate Code")
public final class GenerateCodeAction implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent ev) {
        ClassDiagram classDiagram = Utilities.actionsGlobalContext().lookup(ClassDiagram.class);
        Dialog dialog = new GenerateCodeDialog(classDiagram);
        dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
        dialog.setVisible(true);
    }
}
