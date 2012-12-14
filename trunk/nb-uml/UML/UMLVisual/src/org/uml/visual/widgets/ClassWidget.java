package org.uml.visual.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Utilities;
import org.uml.model.ClassComponent;
import org.uml.visual.providers.ClassHoverProvider;
import org.uml.visual.providers.ClassPopupMenuProvider;
import org.uml.visual.widgets.actions.ClassWidgetAcceptProvider;
import org.uml.visual.widgets.actions.FieldPopupMenuProvider;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;
import org.uml.visual.widgets.actions.PickAttributeModifierAction;
import org.uml.visual.widgets.actions.PickMethodModifierAction;

/**
 *
 * @author NUGS
 */
public class ClassWidget extends UMLWidget{

    //TODO Zoki da li si razmisljao da napravimo domen neki UmlElement pa da ovi nasledjuju to? 
    ClassComponent classComponent;
    ClassDiagramScene scene;   
    private static final Image MethodDefaultImage = Utilities.loadImage("org/uml/visual/icons/MethodDefault.jpg"); // NOI18N
    private static final Image AtributeDefaultImage = Utilities.loadImage("org/uml/visual/icons/AtributeDefault.jpg"); // NOI18N
    
    
    private static final Border RESIZE_BORDER = 
        BorderFactory.createResizeBorder(4, Color.black, true);
    private static final Border DEFAULT_BORDER = 
        BorderFactory.createLineBorder();
    
    private LabelWidget classNameWidget;
    private Widget fieldsWidget;
    private Widget methodsWidget;
    
    private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());

    private WidgetAction pickMethodModifier = ActionFactory.createSelectAction(new PickMethodModifierAction(this));
    private WidgetAction pickAtributeModifier = ActionFactory.createSelectAction(new PickAttributeModifierAction(this));
    
    private static final Border BORDER_4 = BorderFactory.createEmptyBorder(6);
    public ClassWidget(ClassDiagramScene scene, ClassComponent classComponent) {
        super(scene);
        this.classComponent = classComponent;
        this.scene=scene;
        setChildConstraint(getImageWidget(), 1);
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setBorder(BorderFactory.createLineBorder());
        setOpaque(true);
        setCheckClipping(true);
       
        
        Widget classWidget = new Widget(scene); // mora ovako zbog layouta ne moze this 
        classWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        classWidget.setBorder(BORDER_4);
        
        //ImageWidget classImage= new ImageWidget(scene);
        //classImage.setImage(this.classComponent.getImage());
        classNameWidget = new LabelWidget(scene);
        classNameWidget.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        classNameWidget.setAlignment(LabelWidget.Alignment.CENTER);
        classWidget.addChild(classNameWidget);
        addChild(classWidget);
        
        classNameWidget.getActions().addAction(editorAction);                
        
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
        
        this.classNameWidget.setLabel(classComponent.getName());
         
        getActions().addAction(ActionFactory.createAcceptAction(new ClassWidgetAcceptProvider(this)));
        getActions().addAction(ActionFactory.createPopupMenuAction(new ClassPopupMenuProvider(this)));
        getActions().addAction(ActionFactory.createResizeAction());
        //getActions().addAction(ActionFactory.createHoverAction(new ClassHoverProvider()));
    }
     public String getClassName() {
        return classNameWidget.getLabel();
    }

    public void setClassName(String className) {
        this.classNameWidget.setLabel(className);
    }

    public Widget createFieldWidget(String fieldName) {
        Scene scene = getScene();

        Widget fieldWidget = new Widget(scene);
        fieldWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());
        
        //fieldWidget.addChild(createAtributeModifierPicker(scene));

        LabelWidget labelWidget = new LabelWidget(scene); 
        labelWidget.setLabel(fieldName);
        labelWidget.getActions().addAction(editorAction);
        labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(fieldWidget)));
        
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

        //widget.addChild(createMethodModifierPicker(scene));

        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(methodName);
        widget.addChild(labelWidget);
        labelWidget.getActions().addAction(editorAction);
        labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(widget)));
        
        return widget;
    }

    public void createFieldAction(Widget memberWidget) {
        fieldsWidget.addChild(memberWidget);
    }

    public void removeField(Widget memberWidget) {
        fieldsWidget.removeChild(memberWidget);
    }

    public void createMethodAction(Widget operationWidget) {
        methodsWidget.addChild(operationWidget);
    }

    public void removeMethod(Widget operationWidget) {
        methodsWidget.removeChild(operationWidget);
    }

    public ClassDiagramScene getClassDiagramScene() {
        return scene;
    }

    @Override
    public String toString() {
        return classNameWidget.getLabel();
    }
    
//     @Override
//    public void notifyStateChanged(ObjectState previousState, ObjectState newState) {
//        super.notifyStateChanged(previousState, newState);
//        setBorder(newState.isSelected() ? (newState.isHovered() ? RESIZE_BORDER : DEFAULT_BORDER) : DEFAULT_BORDER);
//    }

    @Override
    public ClassComponent getComponent() {
        return classComponent;
    }
        
    
}
