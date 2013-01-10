/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.Font;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.EnumComponent;
import org.uml.visual.providers.ClassPopupMenuProvider;
import org.uml.visual.widgets.actions.EditFieldNameAction;
import org.uml.visual.widgets.actions.EnumPopupMenuProvider;
import org.uml.visual.widgets.actions.FieldPopupMenuProvider;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;

/**
 *
 * @author "NUGS"
 */
public class EnumWidget extends UMLWidget {

    ClassDiagramScene scene;
    EnumComponent enumComponent;
    private LabelWidget enumNameWidget;
    private Widget literalsWidget;
    private Widget fieldsWidget;
    private Widget methodsWidget;
    private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
    private static final Border BORDER_4 = BorderFactory.createEmptyBorder(6);

    public EnumWidget(ClassDiagramScene scene, EnumComponent enumComponent) {
        super(scene);
        this.scene = scene;
        this.enumComponent = enumComponent;

        setChildConstraint(getImageWidget(), 1);
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setBorder(org.netbeans.api.visual.border.BorderFactory.createLineBorder());
        setOpaque(true);
        setCheckClipping(true);

        Widget enumWidget = new Widget(scene);
        enumWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        enumWidget.setBorder(BORDER_4);

        LabelWidget stereotip = new LabelWidget(scene, "<<enumeration>>");
        stereotip.setAlignment(LabelWidget.Alignment.CENTER);
        enumWidget.addChild(stereotip);

        enumNameWidget = new LabelWidget(scene);
        enumNameWidget.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        enumNameWidget.setAlignment(LabelWidget.Alignment.CENTER);
        enumWidget.addChild(enumNameWidget);
        addChild(enumWidget);
        enumNameWidget.getActions().addAction(editorAction);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        literalsWidget = new Widget(scene);
        literalsWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        literalsWidget.setOpaque(false);
        literalsWidget.setBorder(BORDER_4);
        LabelWidget literalName = new LabelWidget(scene);
        literalsWidget.addChild(literalName);
        addChild(literalsWidget);

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
        methodsWidget.addChild(operationName);
        addChild(methodsWidget);
             
        this.enumNameWidget.setLabel(enumComponent.getName());
        
        getActions().addAction(ActionFactory.createPopupMenuAction(new EnumPopupMenuProvider(this)));
    }

    public ClassDiagramScene getClassDiagramScene() {
        return scene;
    }
        @Override
        public EnumComponent getComponent () {
            return enumComponent;
        }

    @Override
    public String toString() {
        return enumComponent.getName();
    }

    public Widget createLiteralWidget(String literalName) {
        Scene scene = getScene();

        Widget literalWidget = new Widget(scene);
        literalWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());
        
        LabelWidget labelWidget = new LabelWidget(scene); 
        labelWidget.setLabel(literalName);
        labelWidget.getActions().addAction(editorAction);
        labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(literalWidget)));
        
        literalWidget.addChild(labelWidget);
        
        return literalWidget;
    }
    
    public Widget createFieldWidget(String fieldName) {
        Scene scene = getScene();

        Widget fieldWidget = new Widget(scene);
        fieldWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());
        
        LabelWidget visibilityLabel = new LabelWidget(scene);
        visibilityLabel.setLabel("+");
        fieldWidget.addChild(visibilityLabel);
        
        LabelWidget labelWidget = new LabelWidget(scene); 
        labelWidget.setLabel(fieldName);
        labelWidget.getActions().addAction(editorAction);
        labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(fieldWidget)));
        
        fieldWidget.addChild(labelWidget);
        
        return fieldWidget;
    }

    public Widget createMethodWidget(String methodName) {
        Scene scene = getScene();
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createHorizontalFlowLayout());
        
        LabelWidget visibilityLabel = new LabelWidget(scene);
        visibilityLabel.setLabel("+");
        widget.addChild(visibilityLabel);
        
        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(methodName);
        widget.addChild(labelWidget);
        labelWidget.getActions().addAction(editorAction);
        labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(widget)));
        
        return widget;
    }
    
    public void createFieldAction(Widget fieldWidget) {
        fieldsWidget.addChild(fieldWidget);
    }
        
    public void createMethodAction(Widget operationWidget) {
        methodsWidget.addChild(operationWidget);
    }

    public void createLiteralAction(Widget literalWidget) {
        literalsWidget.addChild(literalWidget);
    }
    
    public String getEnumName() {
        return enumComponent.getName();
    }

    @Override
    public LabelWidget getNameLabel() {
        return enumNameWidget;
    }
}
