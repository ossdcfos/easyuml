/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.providers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ClassWidget;

/**
 *
 * @author hrza
 */
public class ClassPopupMenuProvider implements PopupMenuProvider {

    private ClassWidget classWidget;
    private JPopupMenu menu;
    private JMenuItem deleteWidget;
    private JMenuItem addAtrinute;
    private JMenuItem addMethod;

    public ClassPopupMenuProvider(ClassWidget classWidget) {
        this.classWidget = classWidget;
        menu = new JPopupMenu("Class Menu");
        (deleteWidget = new JMenuItem("Delete Class")).addActionListener(removeWidgetListener);
        menu.add(deleteWidget);
        (addAtrinute = new JMenuItem("Add Atribute")).addActionListener(addAtributeListener);
        menu.add(addAtrinute);
        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);
    }
    /**
     * Remove Widget Listener
     *
     *
     */
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           classWidget.removeFromParent();
        }
    };
    ActionListener addAtributeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Widget w = classWidget.createFieldWidget(" Atribute");
            classWidget.createAddFieldAction(w);
            classWidget.getScene().validate();
        }
    };
    ActionListener addMethodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            classWidget.createAddMethodAction(classWidget.createMethodWidget(" Method()"));
            classWidget.getScene().validate();
        }
    };

    // TODO Dodati jos listenera za ClassWidgetMeni
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
