package org.uml.reveng;

import javax.swing.JOptionPane;
import org.openide.windows.WindowManager;
import org.uml.model.ClassDiagram;

/**
 *
 * @author Boris Perović
 */
class ReverseEngineerActionBase {

    public void openReverseEngineerDialog(ClassDiagram classDiagram) {
        try (ReverseEngineerDialog dialog = new ReverseEngineerDialog(classDiagram)) {
            dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            dialog.setVisible(true);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex.getMessage(), "Warning", JOptionPane.WARNING_MESSAGE);
        }
    }
}
