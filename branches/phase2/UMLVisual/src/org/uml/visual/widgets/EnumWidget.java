package org.uml.visual.widgets;

import org.uml.visual.widgets.members.LiteralWidget;
import org.uml.visual.widgets.members.ConstructorWidget;
import org.uml.visual.widgets.members.MethodWidget;
import org.uml.visual.widgets.members.FieldWidget;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.members.Constructor;
import org.uml.model.EnumComponent;
import org.uml.model.members.Field;
import org.uml.model.members.Literal;
import org.uml.model.members.Method;
import org.uml.visual.widgets.providers.popups.EnumPopupMenuProvider;

/**
 *
 * @author "NUGS"
 */
public class EnumWidget extends ComponentWidgetBase implements INameableWidget {

    private final Widget literalsContainer;
    private final Widget fieldsContainer;
    private final Widget methodsContainer;

    public EnumWidget(ClassDiagramScene scene, EnumComponent enumComponent) {
        super(scene);
        this.component = enumComponent;

        Widget headerWidget = new Widget(scene);
        headerWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        headerWidget.setBorder(EMPTY_BORDER_4);

        LabelWidget stereotype = new LabelWidget(scene, "<<enumeration>>");
        stereotype.setAlignment(LabelWidget.Alignment.CENTER);
        headerWidget.addChild(stereotype);

        nameWidget.setLabel(component.getName());
        headerWidget.addChild(nameWidget);
        addChild(headerWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        literalsContainer = new Widget(scene);
        literalsContainer.setLayout(LayoutFactory.createVerticalFlowLayout());
        literalsContainer.setMinimumSize(CONTAINER_MIN_DIMENSION);
        literalsContainer.setOpaque(false);
        literalsContainer.setBorder(EMPTY_BORDER_4);
        LabelWidget literalName = new LabelWidget(scene);
        literalsContainer.addChild(literalName);
        addChild(literalsContainer);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        fieldsContainer = new Widget(scene);
        fieldsContainer.setLayout(LayoutFactory.createVerticalFlowLayout());
        fieldsContainer.setMinimumSize(CONTAINER_MIN_DIMENSION);
        fieldsContainer.setOpaque(false);
        fieldsContainer.setBorder(EMPTY_BORDER_4);
        LabelWidget memberName = new LabelWidget(scene);
        fieldsContainer.addChild(memberName);
        addChild(fieldsContainer);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        methodsContainer = new Widget(scene);
        methodsContainer.setLayout(LayoutFactory.createVerticalFlowLayout());
        methodsContainer.setMinimumSize(CONTAINER_MIN_DIMENSION);
        methodsContainer.setOpaque(false);
        methodsContainer.setBorder(EMPTY_BORDER_4);
        LabelWidget operationName = new LabelWidget(scene);
        methodsContainer.addChild(operationName);
        addChild(methodsContainer);

        this.nameWidget.setLabel(component.getName());

        getActions().addAction(ActionFactory.createPopupMenuAction(new EnumPopupMenuProvider(this)));

        for (Literal l : getComponent().getLiterals().values()) {
            LiteralWidget w = new LiteralWidget(scene, l);
            this.addLiteralWidget(w);
        }

        for (Constructor c : getComponent().getConstructors().values()) {
            ConstructorWidget w = new ConstructorWidget(scene, c);
            this.addConstructorWidget(w);
        }

        for (Field fieldComp : getComponent().getFields().values()) {

            FieldWidget w = new FieldWidget(this.getClassDiagramScene(), fieldComp);
            this.addFieldWidget(w);
        }

        for (Method methodComp : getComponent().getMethods().values()) {
            MethodWidget mw = new MethodWidget(this.getClassDiagramScene(), methodComp);
            this.addMethodWidget(mw);
        }

        // scene.validate();
    }

    @Override
    public final EnumComponent getComponent() {
        return (EnumComponent) component;
    }

    @Override
    public String toString() {
        return component.getName();
    }

    public Widget createLiteralWidget(String literalName) {
        Scene scene = getScene();

        Widget literalWidget = new Widget(scene);
        literalWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());

        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(literalName);
        labelWidget.getActions().addAction(nameEditorAction);
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
        labelWidget.getActions().addAction(nameEditorAction);
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
        labelWidget.getActions().addAction(nameEditorAction);
        //labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(widget)));

        return widget;
    }

    public final void addFieldWidget(FieldWidget fieldWidget) {
        addMember(fieldsContainer, fieldWidget);
    }

    public final void addMethodWidget(MethodWidget operationWidget) {
        addMember(methodsContainer, operationWidget);
    }

    public final void addConstructorWidget(ConstructorWidget operationWidget) {
        addMember(methodsContainer, operationWidget);
    }

    public final void addLiteralWidget(LiteralWidget literalWidget) {
        addMember(literalsContainer, literalWidget);
    }

    @Override
    public void setAttributes(String attributes) {
    }

}
