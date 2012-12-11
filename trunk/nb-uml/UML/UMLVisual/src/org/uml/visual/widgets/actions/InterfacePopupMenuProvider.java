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
import org.netbeans.modules.visual.action.PopupMenuAction;
import org.uml.visual.widgets.InterfaceWidget;

/**
 *
 * @author Jelena
 */
public class InterfacePopupMenuProvider implements PopupMenuProvider{
    private InterfaceWidget interfaceWidget;
    private JPopupMenu menu;
    private JMenuItem deleteInterface;
    private JMenuItem addMethod;

    public InterfacePopupMenuProvider(InterfaceWidget interfaceWidget) {
        this.interfaceWidget = interfaceWidget;
        menu = new JPopupMenu("Interface Menu");       
        
        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);
        
        (deleteInterface= new JMenuItem("Delete Interface")).addActionListener(removeWidgetListener);
        menu.add(deleteInterface);
    }
    
    ActionListener addMethodListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            interfaceWidget.createMethodAction(interfaceWidget.createMethodWidget("Method()"));
            interfaceWidget.getScene().validate();
        }
    };
    
    ActionListener removeWidgetListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            interfaceWidget.removeFromParent();
            interfaceWidget.getScene().validate();
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
    
}
