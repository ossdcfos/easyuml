/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.InputEvent;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.SelectProvider;
import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.Exceptions;
import org.openide.util.Utilities;
import org.uml.model.Field;
import org.uml.model.UmlClassElement;
import org.uml.model.Visibility;
import org.uml.visual.widgets.actions.AddFieldAction;
import org.uml.visual.widgets.actions.AddMethodAction;
import org.uml.visual.widgets.actions.DeleteFieldAction;
import org.uml.visual.widgets.actions.DeleteMethodAction;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;
import org.uml.visual.widgets.actions.PickAttributeModifierAction;
import org.uml.visual.widgets.actions.PickFinalKeywordForAttributeAction;
import org.uml.visual.widgets.actions.PickFinalKeywordForMethodAction;
import org.uml.visual.widgets.actions.PickMethodModifierAction;
import org.uml.visual.widgets.actions.PickStaticKeywordForAttributeAction;
import org.uml.visual.widgets.actions.PickStaticKeywordForMethodAction;

/**
 *
 * @author NUGS
 */
public class ClassWidget extends IconNodeWidget {

    //TODO Zoki da li si razmisljao da napravimo domen neki UmlElement pa da ovi nasledjuju to? 
    UmlClassElement classElement;
    
    private static final Image MethodDefaultImage = Utilities.loadImage("org/uml/visual/icons/MethodDefault.jpg"); // NOI18N
    private static final Image AtributeDefaultImage = Utilities.loadImage("org/uml/visual/icons/AtributeDefault.jpg"); // NOI18N
    
    private LabelWidget classNameWidget;
    private Widget fieldsWidget;
    private Widget methodsWidget;
    
    private WidgetAction addFieldAction = ActionFactory.createSelectAction(new AddFieldAction(this));
    private WidgetAction addMethodAction = ActionFactory.createSelectAction(new AddMethodAction(this));
    private WidgetAction deleteFieldAction = ActionFactory.createSelectAction(new DeleteFieldAction(this));
    private WidgetAction deleteMethodAction = ActionFactory.createSelectAction(new DeleteMethodAction(this));
    private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction(this));

    private WidgetAction pickMethodModifier = ActionFactory.createSelectAction(new PickMethodModifierAction(this));
    private WidgetAction pickAtributeModifier = ActionFactory.createSelectAction(new PickAttributeModifierAction(this));
    private WidgetAction pickStaticKeywordForAtribute = ActionFactory.createSelectAction(new PickStaticKeywordForAttributeAction());
    private WidgetAction pickFinalKeywordForAtribute = ActionFactory.createSelectAction(new PickFinalKeywordForAttributeAction());
    private WidgetAction pickStaticKeywordForMethod = ActionFactory.createSelectAction(new PickStaticKeywordForMethodAction());
    private WidgetAction pickFinalKeywordForMethod = ActionFactory.createSelectAction(new PickFinalKeywordForMethodAction());
    
    private static final Border BORDER_4 = BorderFactory.createEmptyBorder(6);

    public ClassWidget(ClassDiagramScene scene, UmlClassElement umlClassElement) {
        super(scene);
        this.classElement = umlClassElement;
        
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setBorder(BorderFactory.createLineBorder());
        setOpaque(true);
        setCheckClipping(true);
        
        Widget classWidget = new Widget(scene); // mora ovako zbog layouta ne moze this 
        classWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());
        classWidget.setBorder(BORDER_4);
        
        //getActions().addAction(ActionFactory.createResizeAction());
        //getActions().addAction(ActionFactory.createMoveAction(ActionFactory.createSnapToGridMoveStrategy(16, 16), null));
        
        ImageWidget classImage= new ImageWidget(scene);
        classImage.setImage(classElement.getImage());
        
        classNameWidget = new LabelWidget(scene);
        classNameWidget.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        classWidget.addChild(classNameWidget);
        addChild(classWidget);
        classNameWidget.getActions().addAction(ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction(this)));
        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));
        
        fieldsWidget = new Widget(scene);
        fieldsWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        fieldsWidget.setOpaque(false);
        fieldsWidget.setBorder(BORDER_4);
        LabelWidget memberName = new LabelWidget(scene);
        fieldsWidget.addChild(memberName);
        addChild(fieldsWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        methodsWidget = new Widget(scene);
        methodsWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        methodsWidget.setOpaque(false);
        methodsWidget.setBorder(BORDER_4);
        LabelWidget operationName = new LabelWidget(scene);
        fieldsWidget.addChild(operationName);
        addChild(methodsWidget);
        
        createAddFieldAction(createAddFieldActionWidget());
        createAddMethodAction(createAddMethodActionWidget());
    }
     public String getClassName() {
        return classNameWidget.getLabel();
    }

    public void setClassName(String className) {
        this.classNameWidget.setLabel(className);

    }

    public Widget createAddFieldActionWidget() {
        Scene scene = getScene();
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createAbsoluteLayout());
        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel("+ add field");
        labelWidget.setForeground(Color.GRAY);
        labelWidget.setAlignment(LabelWidget.Alignment.CENTER);
        labelWidget.getActions().addAction(addFieldAction);       
        widget.addChild(labelWidget);
        return widget;
    }


    public Widget createAddMethodActionWidget() {
        Scene scene = getScene();
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createAbsoluteLayout());
        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel("+ add method");
        labelWidget.setForeground(Color.GRAY);
        labelWidget.setAlignment(LabelWidget.Alignment.CENTER);
        labelWidget.getActions().addAction(addMethodAction);
        widget.addChild(labelWidget);
        return widget;
    }

    public Widget createFieldWidget(String fieldName) {
        Scene scene = getScene();
       // FieldWidget fieldWidget = new FieldWidget(fieldName, scene);

        Widget fieldWidget = new Widget(scene);
        fieldWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());
        
        
        LabelWidget labelMinus = new LabelWidget(scene);
        labelMinus.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        labelMinus.setLabel("-");
        labelMinus.getActions().addAction(deleteFieldAction);
        fieldWidget.addChild(labelMinus);

        fieldWidget.addChild(createAtributeModifierPicker(scene));

        LabelWidget staticKeyword = new LabelWidget(scene);
        staticKeyword.setLabel(" _");
        staticKeyword.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
        fieldWidget.addChild(staticKeyword);
        staticKeyword.getActions().addAction(pickStaticKeywordForAtribute);

        LabelWidget finalKeyword = new LabelWidget(scene);
        finalKeyword.setLabel(" _ ");
        finalKeyword.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
        fieldWidget.addChild(finalKeyword);
        finalKeyword.getActions().addAction(pickFinalKeywordForAtribute);

        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(fieldName);
        labelWidget.getActions().addAction(editorAction);
        //dodato polje u classElement

        fieldWidget.addChild(labelWidget);
        
        return fieldWidget;
    }

    private Widget createAtributeModifierPicker(Scene scene) {
        Widget w = new Widget(scene);
        w.setLayout(LayoutFactory.createVerticalFlowLayout());

        ImageWidget DefaultImage = new ImageWidget(scene);
        DefaultImage.setImage(AtributeDefaultImage);
        DefaultImage.getActions().addAction(pickAtributeModifier);
        w.addChild(DefaultImage);
        return w;
    }

    private Widget createMethodModifierPicker(Scene scene) {
        Widget w = new Widget(scene);
        w.setLayout(LayoutFactory.createVerticalFlowLayout());
        ImageWidget DefaultImage = new ImageWidget(scene);
        DefaultImage.setImage(MethodDefaultImage);
        DefaultImage.getActions().addAction(pickMethodModifier);
        w.addChild(DefaultImage);
        return w;
    }

    public Widget createMethodWidget(String methodName) {
        Scene scene = getScene();
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createHorizontalFlowLayout());

        LabelWidget labelMinus = new LabelWidget(scene);
        labelMinus.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        labelMinus.setLabel("-");
        labelMinus.getActions().addAction(deleteMethodAction);
        widget.addChild(labelMinus);

        widget.addChild(createMethodModifierPicker(scene));

        LabelWidget staticKeyword = new LabelWidget(scene);
        staticKeyword.setLabel(" _");
        staticKeyword.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
        widget.addChild(staticKeyword);
        staticKeyword.getActions().addAction(pickStaticKeywordForMethod);

        LabelWidget finalKeyword = new LabelWidget(scene);
        finalKeyword.setLabel(" _ ");
        finalKeyword.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
        widget.addChild(finalKeyword);
        finalKeyword.getActions().addAction(pickFinalKeywordForMethod);

        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(methodName);
        widget.addChild(labelWidget);
        labelWidget.getActions().addAction(editorAction);


        return widget;
    }

    public void createAddFieldAction(Widget memberWidget) {
        fieldsWidget.addChild(memberWidget);
    }

    public void removeField(Widget memberWidget) {
        fieldsWidget.removeChild(memberWidget);
    }

    public void createAddMethodAction(Widget operationWidget) {
        methodsWidget.addChild(operationWidget);
    }

    public void removeMethod(Widget operationWidget) {
        methodsWidget.removeChild(operationWidget);
    }
        
    
}
