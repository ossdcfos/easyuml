/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author "NUGS"
 */
public class MethodPopupMenuProvider implements PopupMenuProvider{
    
        private Widget methodWidget;
        private JPopupMenu menu;
        private JMenuItem deleteMethod;

    public MethodPopupMenuProvider(Widget methodWidget) {
        this.methodWidget = methodWidget;
        menu= new JPopupMenu ("Class Menu");
        (deleteMethod= new JMenuItem ("Delete Method")).addActionListener(removeWidgetListener);
        menu.add(deleteMethod);
    }
        ActionListener removeWidgetListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            methodWidget.removeFromParent();
        }
    };
                

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
        
}
