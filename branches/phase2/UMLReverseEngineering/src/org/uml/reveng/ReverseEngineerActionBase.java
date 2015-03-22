package org.uml.reveng;

import org.openide.filesystems.FileObject;
import org.openide.windows.WindowManager;
import org.uml.model.ClassDiagram;

/**
 *
 * @author Boris Perović
 */
class ReverseEngineerActionBase {

    public void openReverseEngineerDialog(FileObject projectFolder, ClassDiagram classDiagram) {

        ReverseEngineerDialog dialog = new ReverseEngineerDialog(projectFolder, classDiagram);
        dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
        dialog.setVisible(true);
    }
}
