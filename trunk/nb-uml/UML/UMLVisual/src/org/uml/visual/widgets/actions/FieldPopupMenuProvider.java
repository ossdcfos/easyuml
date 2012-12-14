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
import org.uml.visual.dialogs.ChangeModifierDialog;
import org.uml.visual.dialogs.ChangeVisibilityDalog;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.UMLWidget;

/**
 *
 * @author Jelena
 */
public class FieldPopupMenuProvider implements PopupMenuProvider{

    private Widget fieldWidget;
    private JPopupMenu menu;
    private JMenuItem deleteField;
    private JMenuItem visibility;
    private JMenuItem modifiers;

    
    public FieldPopupMenuProvider(Widget fieldWidget) {
        this.fieldWidget = fieldWidget;
        menu = new JPopupMenu("Class Menu");

        (visibility = new JMenuItem("Visibility")).addActionListener(changeVisibility);
        menu.add(visibility);
        (modifiers = new JMenuItem("Modifiers")).addActionListener(changeModifier);
        menu.add(modifiers);
        menu.addSeparator();
        (deleteField = new JMenuItem("Delete Field")).addActionListener(removeWidgetListener);
        menu.add(deleteField);
    }
    
    ActionListener changeModifier= new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog dialog = new ChangeModifierDialog(null,fieldWidget,true);
            dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            dialog.setPreferredSize(new Dimension(100,100));
            dialog.setResizable(false);
            dialog.setVisible(true);
            fieldWidget.getScene().validate();
            
        }
    };
    
    ActionListener changeVisibility = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          
            JDialog dialog = new ChangeVisibilityDalog(null,fieldWidget,true);
            dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            dialog.setPreferredSize(new Dimension(100,100));
            dialog.setResizable(false);
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
