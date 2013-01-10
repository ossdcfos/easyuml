/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.actions;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.providers.MouseAdapterZaView;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.EnumWidget;

/**
 *
 * @author Jelena
 */
public class EnumPopupMenuProvider implements PopupMenuProvider{
    
    private EnumWidget enumWidget;
    private JPopupMenu menu;
    private JMenuItem deleteClass;
    private JMenuItem addLiteral;
    private JMenuItem addField;
    private JMenuItem addMethod;
    private JMenuItem addConstructor;
    WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
    MouseListener mouseListener = new MouseAdapterZaView(editorAction);

    public EnumPopupMenuProvider(EnumWidget enumWidget) {
        this.enumWidget = enumWidget;
        menu = new JPopupMenu("Enum Menu");
        
        (addConstructor = new JMenuItem("Add Constructor")).addActionListener(addConstructorListener);
        menu.add(addConstructor);
        (addLiteral = new JMenuItem("Add Literal")).addActionListener(addLiteralListener);
        menu.add(addLiteral);
        (addField = new JMenuItem("Add Field")).addActionListener(addAtributeListener);
        menu.add(addField);
        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);
        (deleteClass = new JMenuItem("Delete Enum")).addActionListener(removeWidgetListener);
        menu.add(deleteClass);        
        
    }
    
    ActionListener addConstructorListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            enumWidget.createMethodAction(enumWidget.createMethodWidget(enumWidget.getEnumName()+"()"));
            enumWidget.getScene().validate();
        }
    };
    
    ActionListener addLiteralListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            Widget w = enumWidget.createLiteralWidget("LITERAL");
            enumWidget.createLiteralAction(w);
            enumWidget.getScene().validate();
            
            ActionFactory.getInplaceEditorController (editorAction).openEditor(w.getChildren().get(0));
            w.getScene().getView().addMouseListener(mouseListener);
        }
    };
    
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           enumWidget.removeFromParent();
        }
    };
    ActionListener addAtributeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Widget w = enumWidget.createFieldWidget("Field");
            enumWidget.createFieldAction(w);
            enumWidget.getScene().validate();
            
            ActionFactory.getInplaceEditorController (editorAction).openEditor(w.getChildren().get(1));
            w.getScene().getView().addMouseListener(mouseListener);
        }
    };
    ActionListener addMethodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            Widget w = enumWidget.createMethodWidget("Method()");
            enumWidget.createMethodAction(w);
            enumWidget.getScene().validate();
            
            ActionFactory.getInplaceEditorController (editorAction).openEditor(w.getChildren().get(1));
            w.getScene().getView().addMouseListener(mouseListener);
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
