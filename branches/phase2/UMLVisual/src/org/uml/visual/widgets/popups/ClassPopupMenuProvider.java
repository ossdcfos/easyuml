package org.uml.visual.widgets.popups;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.widget.*;
import org.uml.model.ClassDiagram;
import org.uml.visual.widgets.components.ClassWidget;

/**
 *
 * @author hrza
 */
public class ClassPopupMenuProvider implements PopupMenuProvider {

    private ClassWidget classWidget;
    private JPopupMenu menu;
    private JMenuItem deleteClass;
    private JMenuItem addField;
    private JMenuItem addMethod;
    private JMenuItem addConstructor;

    public ClassPopupMenuProvider(ClassWidget classWidget) {
        this.classWidget = classWidget;
        menu = new JPopupMenu("Class Menu");

        (addConstructor = new JMenuItem("Add Constructor")).addActionListener(addConstructorListener);
        menu.add(addConstructor);
        (addField = new JMenuItem("Add Field")).addActionListener(addFieldListener);
        menu.add(addField);
        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);

        menu.addSeparator();
        
        (deleteClass = new JMenuItem("Delete Class")).addActionListener(removeWidgetListener);
        menu.add(deleteClass);
    }
    
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClassDiagram classDiagram = classWidget.getComponent().getParentDiagram();
            classDiagram.removePartFromContainer(classWidget.getComponent());
        }
    };
    
    ActionListener addFieldListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.addFieldWidget();
        }
    };
    
    ActionListener addMethodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.addMethodWidget();
        }
    };
    ActionListener addConstructorListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.addConstructorWidget();
        }
    };
    
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
    
}
