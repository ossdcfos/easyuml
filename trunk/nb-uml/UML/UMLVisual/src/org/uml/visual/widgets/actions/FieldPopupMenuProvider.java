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
import org.uml.visual.widgets.ClassWidget;

/**
 *
 * @author Jelena
 */
public class FieldPopupMenuProvider implements PopupMenuProvider{

    private Widget fieldWidget;
    private JPopupMenu menu;
    private JMenuItem deleteField;
    
    public FieldPopupMenuProvider(Widget fieldWidget) {
        this.fieldWidget = fieldWidget;
        menu = new JPopupMenu("Class Menu");
        (deleteField = new JMenuItem("Delete Field")).addActionListener(removeWidgetListener);
        menu.add(deleteField);
    }
    
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
