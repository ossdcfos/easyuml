package org.uml.visual.widgets;

import java.awt.Dimension;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Lookup;
import org.openide.util.lookup.Lookups;
import org.uml.model.ClassComponent;
import org.uml.model.Constructor;
import org.uml.model.Field;
import org.uml.model.Method;
import org.uml.model.Visibility;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.popups.ClassPopupMenuProvider;
import org.uml.visual.widgets.providers.ClassWidgetAcceptProvider;

/**
 *
 * @author NUGS
 */
public class ClassWidget extends ComponentWidgetBase implements NameableWidget {

    //TODO Zoki da li si razmisljao da napravimo domen neki UmlElement pa da ovi nasledjuju to? 
    ClassComponent classComponent;
    private Widget fieldsContainer;
    private Widget methodsContainer;
    //private WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
    // koristi se na vise mesta
    private WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    private static final Border BORDER_6 = BorderFactory.createEmptyBorder(6);
//    private static final Border RESIZE_BORDER =
//            BorderFactory.createResizeBorder(4, Color.black, true);
//    private static final Border DEFAULT_BORDER =
//            BorderFactory.createLineBorder();
    private final Lookup lookup;

    public ClassWidget(ClassDiagramScene scene, ClassComponent classComponent) {
        super(scene);
        this.classComponent = classComponent;
        lookup = Lookups.fixed(classComponent, this);
        //lookup = Lookups.singleton(classComponent);
//        setChildConstraint(this, 1); // getImageWidget()
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setBorder(BorderFactory.createLineBorder());
        setOpaque(true);
        setCheckClipping(true);

        // header widget
        Widget headerWidget = new Widget(scene); // mora ovako zbog layouta ne moze this 
        headerWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        headerWidget.setBorder(BORDER_6);
        if (classComponent.isIsAbstract()) {
            LabelWidget abstractLabel = new LabelWidget(headerWidget.getScene(), "<<abstract>>");
            abstractLabel.setFont(headerWidget.getScene().getDefaultFont().deriveFont(Font.ITALIC));
            abstractLabel.setAlignment(LabelWidget.Alignment.CENTER);
            headerWidget.addChild(abstractLabel);
        }
        
        nameWidget.setLabel(classComponent.getName());
        nameWidget.getActions().addAction(nameEditorAction);
        headerWidget.addChild(nameWidget);
        addChild(headerWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        // fields
        fieldsContainer = new Widget(scene);
        fieldsContainer.setMinimumSize(new Dimension(110, 50));
        fieldsContainer.setLayout(LayoutFactory.createVerticalFlowLayout());
        fieldsContainer.setOpaque(false);
        fieldsContainer.setBorder(BORDER_6);
        LabelWidget fieldName = new LabelWidget(scene);
        fieldsContainer.addChild(fieldName);
        addChild(fieldsContainer);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        // methods
        methodsContainer = new Widget(scene);
        methodsContainer.setMinimumSize(new Dimension(110, 50));
        methodsContainer.setLayout(LayoutFactory.createVerticalFlowLayout());
        methodsContainer.setOpaque(false);
        methodsContainer.setBorder(BORDER_6);
        LabelWidget methodName = new LabelWidget(scene);
        methodsContainer.addChild(methodName);
        addChild(methodsContainer);

        // actions
        getActions().addAction(ActionFactory.createAcceptAction(new ClassWidgetAcceptProvider()));
        getActions().addAction(ActionFactory.createPopupMenuAction(new ClassPopupMenuProvider(this)));
//        getActions().addAction(ActionFactory.createMoveAction());
//        getActions().addAction(ActionFactory.createHoverAction(new ClassHoverProvider()));
        
        // Fill the widget when loading an existing diagram
        for (Constructor c : classComponent.getConstructors().values()) {
            ConstructorWidget w = new ConstructorWidget(scene, c);
            this.addConstructorWidget(w);
        }

        for (Field fieldComp : classComponent.getFields().values()) {
            FieldWidget w = new FieldWidget(this.getClassDiagramScene(), fieldComp);
            this.addFieldWidget(w);
        }

        for (Method methodComp : classComponent.getMethods().values()) {
            MethodWidget mw = new MethodWidget(this.getClassDiagramScene(), methodComp);
            this.addMethodWidget(mw);
        }
        //this.getScene().validate();
    }

    @Override
    public String getName() {
        return nameWidget.getLabel();
    }

    public String getClassName() {
        return nameWidget.getLabel();
    }

    public void setClassName(String className) {
        this.nameWidget.setLabel(className);
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
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createHorizontalFlowLayout());

        //widget.addChild(createMethodModifierPicker(scene));
        LabelWidget visibilityLabel = new LabelWidget(scene);
        visibilityLabel.setLabel("+");
        widget.addChild(visibilityLabel);

        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(methodName);
        widget.addChild(labelWidget);
        labelWidget.getActions().addAction(nameEditorAction);
        //labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new MethodPopupMenuProvider(widget)));

        return widget;
    }

    public void addFieldWidget(FieldWidget fieldWidget) {
        fieldsContainer.addChild(fieldWidget);
    }

    public void removeField(Widget memberWidget) {
        fieldsContainer.removeChild(memberWidget);
    }

    public void addMethodWidget(MethodWidget operationWidget) {
        methodsContainer.addChild(operationWidget);
    }

    public void addConstructorWidget(ConstructorWidget operationWidget) {
        methodsContainer.addChild(operationWidget);
    }

    public void removeMethod(Widget operationWidget) {
        methodsContainer.removeChild(operationWidget);
    }

    @Override
    public String toString() {
        return nameWidget.getLabel();
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

    @Override
    public LabelWidget getNameLabel() {
        return nameWidget;
    }

    @Override
    public void setName(String newName) {
        if (getNameLabel().getLabel().equals(newName)) {
            return;
        }

        String oldName = classComponent.getName();
        if (!classComponent.getParentDiagram().nameExists(newName)) {
            this.nameWidget.setLabel(newName);
            classComponent.setName(newName);
            classComponent.getParentDiagram().componentNameChanged(classComponent, oldName);
        } else {
            //WidgetAction editor = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
            //ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(getNameLabel());
            JOptionPane.showMessageDialog(this.getScene().getView(), "Greska, ime vec postoji.");
        }

        for (Widget w : methodsContainer.getChildren()) {
            if (w instanceof ConstructorWidget) {
                ((ConstructorWidget) w).propertyChange(new PropertyChangeEvent(nameWidget, "name", oldName, newName));
            }
        }
    }

    @Override
    public Lookup getLookup() {
        return lookup;
    }

    @Override
    public void setAttributes(String attributes) {
        String[] keyWords = attributes.split(" ");
        for (String keyWord : keyWords) {
            if (keyWord.equals("public")) {
                classComponent.setVisibility(Visibility.PUBLIC);
            }
            if (keyWord.equals("protected")) {
                classComponent.setVisibility(Visibility.PUBLIC);
            }
            if (keyWord.equals("private")) {
                classComponent.setVisibility(Visibility.PRIVATE);
            }
            if (keyWord.equals("abstract")) {
                classComponent.setIsAbstract(true);
            }
        }
    }

}
