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

/**
 *
 * @author "NUGS"
 */
public class MethodPopupMenuProvider implements PopupMenuProvider{
    
        private Widget methodWidget;
        private JPopupMenu menu;
        private JMenuItem deleteMethod;
        private JMenuItem visibility;
        private JMenuItem modifiers;

    public MethodPopupMenuProvider(Widget methodWidget) {
        this.methodWidget = methodWidget;
        menu= new JPopupMenu ("Class Menu");
        
        (visibility = new JMenuItem("Visibility")).addActionListener(changeVisibility);
        menu.add(visibility);
        
        (modifiers = new JMenuItem("Modifiers")).addActionListener(changeModifier);
        menu.add(modifiers);
        
        menu.addSeparator();
        
        (deleteMethod= new JMenuItem ("Delete Method")).addActionListener(removeWidgetListener);
        menu.add(deleteMethod);
    }
        ActionListener removeWidgetListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            methodWidget.removeFromParent();
        }
    };
                
         ActionListener changeModifier= new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            JDialog dialog = new ChangeModifierDialog(null,methodWidget,true);
            dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            dialog.setPreferredSize(new Dimension(100,100));
            dialog.setResizable(false);
            dialog.setVisible(true);
            methodWidget.getScene().validate();
            
        }
    };
    
    ActionListener changeVisibility = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
          
            JDialog dialog = new ChangeVisibilityDalog(null,methodWidget,true);
            dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            dialog.setPreferredSize(new Dimension(100,100));
            dialog.setResizable(false);
            dialog.setVisible(true);
            methodWidget.getScene().validate();
            
        }
    };
        
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
        
}
