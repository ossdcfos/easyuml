package org.uml.visual.widgets.popups;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.LinkedHashSet;
import javax.swing.*;
import org.netbeans.api.visual.widget.*;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ClassComponent;
import org.uml.model.members.Field;
import org.uml.model.members.Method;
import org.uml.visual.widgets.components.ClassWidget;

/**
 *
 * @author hrza
 */
public class ClassPopupMenuProvider extends ComponentPopupMenuProvider {

    private ClassWidget classWidget;
    private JPopupMenu menu;
    private JMenuItem deleteClass;
    private JMenuItem addField,copyFields,pasteFields;
    private JMenuItem addMethod,copyMethods,pasteMethods;
    private JMenuItem addConstructor;
    private JMenuItem addUnimplementedMethods;


    public ClassPopupMenuProvider(ClassWidget classWidget) {
        super(classWidget);
        this.classWidget = classWidget;
        menu = new JPopupMenu("Class Menu");

        (addConstructor = new JMenuItem("Add Constructor")).addActionListener(addConstructorListener);
        menu.add(addConstructor);
        (addField = new JMenuItem("Add Field")).addActionListener(addFieldListener);
        menu.add(addField);
        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);
        (addUnimplementedMethods = new JMenuItem("Add Unimplemented Method")).addActionListener(addUnimplementedMethodsListener);
        menu.add(addUnimplementedMethods);
        (pasteFields = new JMenuItem("Paste fields from clipboard")).addActionListener(pasteFieldsListener);
        menu.add(pasteFields);
        (pasteMethods = new JMenuItem("Paste methods from clipboard")).addActionListener(pasteMethodsListener);
        menu.add(pasteMethods);

        menu.addSeparator();

        (copyFields = new JMenuItem("Copy fields to clipboard")).addActionListener(copyFieldsListener);
        menu.add(copyFields);
        (copyMethods = new JMenuItem("Copy methods to clipboard")).addActionListener(copyMethodsListener);
        menu.add(copyMethods);
        
        menu.addSeparator();

        addZMenuItems(menu);
        
        menu.addSeparator();
        
        (deleteClass = new JMenuItem("Delete Class")).addActionListener(removeWidgetListener);
        menu.add(deleteClass);
        
    }

    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClassDiagram classDiagram = classWidget.getComponent().getParentDiagram();
            classDiagram.removeComponentFromContainer(classWidget.getComponent());
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
            classWidget.addDefaultConstructorWidget();
        }
    };
    ActionListener addUnimplementedMethodsListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClassComponent classComponent = classWidget.getComponent();
            LinkedHashSet<Method> methods = classComponent.getUnimplementedMethods();
            /*for (Method method : methods) {
                System.out.println(method.getSignature());
            }*/
            classWidget.addMethodWidgets(methods);
        }
    };    
    ActionListener pasteMethodsListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String clipboard = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                classWidget.addMethods(clipboard);
            } catch (Exception ex) {
            }
        }
    };      
    ActionListener copyMethodsListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClassComponent classComponent = classWidget.getComponent();
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos, true, "utf-8");
                for (Method method : classComponent.getMethods()) {
                    ps.println(method.getSignature());
                }
                StringSelection selection = new StringSelection(baos.toString());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection,selection);
            } catch (UnsupportedEncodingException ex) {
            }
        }
    };   
   ActionListener pasteFieldsListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String clipboard = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                classWidget.addFields(clipboard);
            } catch (Exception ex) {
            }
        }
    };      
    ActionListener copyFieldsListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClassComponent classComponent = classWidget.getComponent();
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos, true, "utf-8");
                for (Field field : classComponent.getFields()) {
                    ps.println(field.getSignature());
                }
                StringSelection selection = new StringSelection(baos.toString());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection,selection);
            } catch (UnsupportedEncodingException ex) {
            }
        }
    };   

    
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
