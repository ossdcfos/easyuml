/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.openide.windows.WindowManager;
import org.uml.visual.dialogs.ChangeVisibilityDalog;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ClassWidget;

/**
 *
 * @author Jelena
 */
public class FieldPopupMenuProvider implements PopupMenuProvider{

    private Widget fieldWidget;
    private JPopupMenu menu;
    private JMenuItem deleteField;
    private JMenuItem visibility;
    JDialog dialog;
    
    public FieldPopupMenuProvider(Widget fieldWidget) {
        this.fieldWidget = fieldWidget;
        menu = new JPopupMenu("Class Menu");
        (visibility = new JMenuItem("Visibility")).addActionListener(changeVisibility);
        menu.add(visibility);
        (deleteField = new JMenuItem("Delete Field")).addActionListener(removeWidgetListener);
        menu.add(deleteField);
    }
    
    ActionListener changeVisibility = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          
            dialog = new ChangeVisibilityDalog(null,(ClassDiagramScene)fieldWidget.getScene(),true);
            dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            dialog.setPreferredSize(new Dimension(100,100));
            dialog.setVisible(true);
            fieldWidget.getScene().validate();
            
        }
    };
    
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           fieldWidget.removeFromParent();
        }
    };
    
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
    
}
