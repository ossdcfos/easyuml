package org.uml.visual.widgets;

import java.awt.Dimension;
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
import org.uml.model.Constructor;
import org.uml.model.EnumComponent;
import org.uml.model.Field;
import org.uml.model.Literal;
import org.uml.model.Method;
import org.uml.visual.widgets.providers.popups.EnumPopupMenuProvider;
import org.uml.visual.widgets.providers.popups.FieldPopupMenuProvider;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;
import org.uml.visual.widgets.actions.NameEditorAction;

/**
 *
 * @author "NUGS"
 */
public class EnumWidget extends ComponentWidgetBase implements NameableWidget{

    ClassDiagramScene scene;
    EnumComponent enumComponent;
    private LabelWidget enumNameWidget;
    private Widget literalsWidget;
    private Widget fieldsWidget;
    private Widget methodsWidget;
    private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
    private WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    private static final Border BORDER_4 = BorderFactory.createEmptyBorder(6);

    public EnumWidget(ClassDiagramScene scene, EnumComponent enumComponent) {
        super(scene);
        this.scene = scene;
        this.enumComponent = enumComponent;

 //       setChildConstraint(this, 1); // getImageWidget()
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
        enumNameWidget.getActions().addAction(nameEditorAction);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        literalsWidget = new Widget(scene);
        literalsWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        literalsWidget.setMinimumSize(new Dimension(110, 30));
        literalsWidget.setOpaque(false);
        literalsWidget.setBorder(BORDER_4);
        LabelWidget literalName = new LabelWidget(scene);
        literalsWidget.addChild(literalName);
        addChild(literalsWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));
        
        fieldsWidget = new Widget(scene);
        fieldsWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        fieldsWidget.setMinimumSize(new Dimension(110, 30));
        fieldsWidget.setOpaque(false);
        fieldsWidget.setBorder(BORDER_4);
        LabelWidget memberName = new LabelWidget(scene);
        fieldsWidget.addChild(memberName);
        addChild(fieldsWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        methodsWidget = new Widget(scene);
        methodsWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        methodsWidget.setMinimumSize(new Dimension(110, 30));
        methodsWidget.setOpaque(false);
        methodsWidget.setBorder(BORDER_4);
        LabelWidget operationName = new LabelWidget(scene);
        methodsWidget.addChild(operationName);
        addChild(methodsWidget);
             
        this.enumNameWidget.setLabel(enumComponent.getName());
        
        getActions().addAction(ActionFactory.createPopupMenuAction(new EnumPopupMenuProvider(this)));
        
        for (Literal l : enumComponent.getLiterals().values()) {
            LiteralWidget w = new LiteralWidget(scene, l);
            this.addLiteralWidget(w);
        }
        
        for (Constructor c : enumComponent.getConstructors().values()) {
            ConstructorWidget w = new ConstructorWidget(scene, c);
            this.addConstructorWidget(w);
        }
        
        for (Field fieldComp : enumComponent.getFields().values()) {
            
            FieldWidget w = new FieldWidget(this.getClassDiagramScene(), fieldComp);
            this.addFieldWidget(w);
        }
        
        for (Method methodComp : enumComponent.getMethods().values()) {
            MethodWidget mw = new MethodWidget(this.getClassDiagramScene(), methodComp);
            this.addMethodWidget(mw);
        }
        
       // scene.validate();
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
        //labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(literalWidget)));
        
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
        //labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(fieldWidget)));
        
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
        //labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(widget)));
        
        return widget;
    }
    
    public void addFieldWidget(FieldWidget fieldWidget) {
        fieldsWidget.addChild(fieldWidget);
    }
        
    public void addMethodWidget(MethodWidget operationWidget) {
        methodsWidget.addChild(operationWidget);
    }
    
    public void addConstructorWidget(ConstructorWidget operationWidget) {
        methodsWidget.addChild(operationWidget);
    }

    public void addLiteralWidget(Widget literalWidget) {
        literalsWidget.addChild(literalWidget);
    }
    
    @Override
    public String getName() {
        return enumNameWidget.getLabel();
    }

    @Override
    public LabelWidget getNameLabel() {
        return enumNameWidget;
    }

    @Override
    public void setName(String newName) {
        if (getNameLabel().getLabel().equals(newName)) {
            return;
        }
        String oldName = enumComponent.getName();
        if (!enumComponent.getParentDiagram().nameExists(newName)) {
            this.enumNameWidget.setLabel(newName);
            enumComponent.setName(newName);
            enumComponent.getParentDiagram().componentNameChanged(enumComponent, oldName);
        }
    // else
    //    ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(getNameLabel());
    }

    @Override
    public void setAttributes(String attributes) {
        
    }
    
    
}
