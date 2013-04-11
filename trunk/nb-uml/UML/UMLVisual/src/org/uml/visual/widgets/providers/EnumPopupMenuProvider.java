/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

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
import org.uml.model.Constructor;
import org.uml.model.Field;
import org.uml.model.Method;
import org.uml.model.Visibility;
import org.uml.visual.widgets.providers.MouseAdapterZaView;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.ConstructorWidget;
import org.uml.visual.widgets.EnumWidget;
import org.uml.visual.widgets.FieldWidget;
import org.uml.visual.widgets.MethodWidget;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;
import org.uml.visual.widgets.actions.NameEditorAction;

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
            Constructor c = new Constructor(enumWidget.getName());
            //enumComponent.addMethod(c);
            ConstructorWidget w = new ConstructorWidget(enumWidget.getClassDiagramScene(), c);
            enumWidget.addConstructorWidget(w);
            enumWidget.getScene().validate();
        }
    };
    
    ActionListener addLiteralListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            
            Widget w = enumWidget.createLiteralWidget("LITERAL");
            //enumComponent.add...
            enumWidget.addLiteralWidget(w);
            enumWidget.getScene().validate();
            
            ActionFactory.getInplaceEditorController (editorAction).openEditor(w.getChildren().get(0));
            w.getScene().getView().addMouseListener(mouseListener);
        }
    };
    
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            enumWidget.getComponent().getParentDiagram().removeComponent(enumWidget.getName());
           enumWidget.removeFromParent();
        }
    };
    ActionListener addAtributeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Field f = new Field("untitledField", null, Visibility.PRIVATE);
            enumWidget.getComponent().addField(f);
            FieldWidget w = new FieldWidget(enumWidget.getClassDiagramScene(), f);
            enumWidget.addFieldWidget(w);
            enumWidget.getScene().validate();
            
            WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(w));
            ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(w.getNameLabel());
            MouseListener mouseListener = new MouseAdapterZaView(nameEditorAction);
            enumWidget.getScene().getView().addMouseListener(mouseListener);
        }
    };
    ActionListener addMethodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            
            Method m = new Method("untitledMethod", null, null);
            enumWidget.getComponent().addMethod(m);
            MethodWidget w = new MethodWidget(enumWidget.getClassDiagramScene(), m);
            enumWidget.addMethodWidget(w);
            enumWidget.getScene().validate();
            
            WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(w));
            ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(w.getNameLabel());
            MouseListener mouseListener = new MouseAdapterZaView(nameEditorAction);
            enumWidget.getScene().getView().addMouseListener(mouseListener);
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
