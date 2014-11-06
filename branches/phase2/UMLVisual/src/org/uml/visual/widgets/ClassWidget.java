package org.uml.visual.widgets;

import org.uml.visual.widgets.members.ConstructorWidget;
import org.uml.visual.widgets.members.MethodWidget;
import org.uml.visual.widgets.members.FieldWidget;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.uml.model.ClassComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.members.Method;
import org.uml.model.members.Visibility;
import org.uml.visual.widgets.providers.popups.ClassPopupMenuProvider;
import org.uml.visual.widgets.providers.ComponentWidgetAcceptProvider;

/**
 *
 * @author NUGS
 */
public class ClassWidget extends ComponentWidgetBase implements INameableWidget {
    
    private final Widget fieldsContainer;
    private final Widget methodsContainer;
    
    //private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
    
    private final Lookup lookup;

    public ClassWidget(ClassDiagramScene scene, ClassComponent classComponent) {
        super(scene);
        this.component = classComponent;
        
        lookup = Lookups.fixed(classComponent, this);
        //lookup = Lookups.singleton(classComponent);

        // Header
        Widget headerWidget = new Widget(scene); // mora ovako zbog layouta ne moze this 
        headerWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        headerWidget.setBorder(EMPTY_BORDER_6);
        if (classComponent.isIsAbstract()) {
            LabelWidget abstractLabel = new LabelWidget(headerWidget.getScene(), "<<abstract>>");
            abstractLabel.setFont(headerWidget.getScene().getDefaultFont().deriveFont(Font.ITALIC));
            abstractLabel.setAlignment(LabelWidget.Alignment.CENTER);
            headerWidget.addChild(abstractLabel);
        }
        
        nameWidget.setLabel(component.getName());
        headerWidget.addChild(nameWidget);
        addChild(headerWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));
        
        // Fields
        fieldsContainer = new Widget(scene);
        fieldsContainer.setMinimumSize(CONTAINER_MIN_DIMENSION);
        fieldsContainer.setLayout(LayoutFactory.createVerticalFlowLayout());
        fieldsContainer.setOpaque(false);
        fieldsContainer.setBorder(EMPTY_BORDER_6);
        LabelWidget fieldName = new LabelWidget(scene);
        fieldsContainer.addChild(fieldName);
        addChild(fieldsContainer);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));
        
        // Methods
        methodsContainer = new Widget(scene);
        methodsContainer.setMinimumSize(CONTAINER_MIN_DIMENSION);
        methodsContainer.setLayout(LayoutFactory.createVerticalFlowLayout());
        methodsContainer.setOpaque(false);
        methodsContainer.setBorder(EMPTY_BORDER_6);
        LabelWidget methodName = new LabelWidget(scene);
        methodsContainer.addChild(methodName);
        addChild(methodsContainer);

        
        // Actions
        getActions().addAction(ActionFactory.createAcceptAction(new ComponentWidgetAcceptProvider()));
        getActions().addAction(ActionFactory.createPopupMenuAction(new ClassPopupMenuProvider(this)));
//        getActions().addAction(ActionFactory.createMoveAction());
//        getActions().addAction(ActionFactory.createHoverAction(new ClassHoverProvider()));
        
        // Fill the widget when loading an existing diagram
        for (Constructor c : classComponent.getConstructors().values()) {
            ConstructorWidget w = new ConstructorWidget(scene, c);
            addConstructorWidget(w);
        }

        for (Field fieldComp : classComponent.getFields().values()) {
            FieldWidget w = new FieldWidget(getClassDiagramScene(), fieldComp);
            addMember(fieldsContainer, w);
        }

        for (Method methodComp : classComponent.getMethods().values()) {
            MethodWidget mw = new MethodWidget(getClassDiagramScene(), methodComp);
            addMember(methodsContainer, mw);
        }
        //this.getScene().validate();
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    public Widget createFieldWidget(String fieldName) {
        Scene scene = getScene();

        Widget fieldWidget = new Widget(scene);
        fieldWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());

        //fieldWidget.addChild(createAtributeModifierPicker(scene));
        LabelWidget visibilityLabel = new LabelWidget(scene);
        visibilityLabel.setLabel("+");
        fieldWidget.addChild(visibilityLabel);

        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(fieldName);
        labelWidget.getActions().addAction(nameEditorAction);
        //labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(fieldWidget)));
        //dodato polje u classElement
        fieldWidget.addChild(labelWidget);

        return fieldWidget;
    }

    public Widget createMethodWidget(String methodName) {
        Scene scene = getScene();
        
        Widget methodWidget = new Widget(scene);
        methodWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());

        //widget.addChild(createMethodModifierPicker(scene));
        LabelWidget visibilityLabel = new LabelWidget(scene);
        visibilityLabel.setLabel("+");
        methodWidget.addChild(visibilityLabel);

        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(methodName);
        labelWidget.getActions().addAction(nameEditorAction);
        methodWidget.addChild(labelWidget);
        //labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new MethodPopupMenuProvider(widget)));

        return methodWidget;
    }
    
    public final void addFieldWidget(FieldWidget fieldWidget) {
        addMember(fieldsContainer, fieldWidget);
    }

    public void removeField(FieldWidget field) {
        removeMember(fieldsContainer, field);
    }

    public final void addMethodWidget(MethodWidget methodWidget) {
        addMember(methodsContainer, methodWidget);
    }

    public final void addConstructorWidget(ConstructorWidget constructorWidget) {
        addMember(methodsContainer, constructorWidget);
    }

    public void removeMethod(MethodWidget methodWidget) {
        removeMember(methodsContainer, methodWidget);
    }

//     @Override
//    public void notifyStateChanged(ObjectState previousState, ObjectState newState) {
//        super.notifyStateChanged(previousState, newState);
//        setBorder(newState.isSelected() ? (newState.isHovered() ? RESIZE_BORDER : DEFAULT_BORDER) : DEFAULT_BORDER);
//    }
    
    @Override
    public ClassComponent getComponent() {
        return (ClassComponent) component;
    }

    @Override
    public void setName(String newName) {
        String oldName = component.getName();
        
        super.setName(newName);

        for (Widget w : methodsContainer.getChildren()) {
            if (w instanceof ConstructorWidget) {
                ((ConstructorWidget) w).propertyChange(new PropertyChangeEvent(nameWidget, "name", oldName, newName));
            }
        }
    }

    @Override
    public void setAttributes(String attributes) {
        String[] keyWords = attributes.split(" ");
        for (String keyWord : keyWords) {
            if (keyWord.equals("public")) {
                component.setVisibility(Visibility.PUBLIC);
            } else if (keyWord.equals("protected")) {
                component.setVisibility(Visibility.PUBLIC);
            } else if (keyWord.equals("private")) {
                component.setVisibility(Visibility.PRIVATE);
            }
            if (keyWord.equals("abstract")) {
                getComponent().setIsAbstract(true);
            }
        }
    }

}
