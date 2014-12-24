package org.uml.visual.widgets.components;

import org.uml.visual.widgets.members.LiteralWidget;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.components.EnumComponent;
import org.uml.model.members.Literal;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.popups.EnumPopupMenuProvider;

/**
 *
 * @author "NUGS"
 */
public class EnumWidget extends ComponentWidgetBase {

    private final Widget literalsContainer;

    public EnumWidget(ClassDiagramScene scene, EnumComponent enumComponent) {
        super(scene, enumComponent);

        Widget headerWidget = new Widget(scene);
        headerWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        headerWidget.setBorder(EMPTY_BORDER_4);

        LabelWidget stereotype = new LabelWidget(scene, "<<enumeration>>");
        stereotype.setAlignment(LabelWidget.Alignment.CENTER);
        headerWidget.addChild(stereotype);

        nameLabel.setLabel(component.getName());
        headerWidget.addChild(nameLabel);
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

        this.nameLabel.setLabel(component.getName());

        getActions().addAction(ActionFactory.createPopupMenuAction(new EnumPopupMenuProvider(this)));

        for (Literal l : getComponent().getLiterals()) {
            LiteralWidget w = new LiteralWidget(scene, l);
            this.addLiteralWidget(w);
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

//    public Widget createLiteralWidget(String literalName) {
//        Scene scene = getScene();
//
//        Widget literalWidget = new Widget(scene);
//        literalWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());
//
//        LabelWidget labelWidget = new LabelWidget(scene);
//        labelWidget.setLabel(literalName);
//        labelWidget.getActions().addAction(nameEditorAction);
//        //labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(literalWidget)));
//
//        literalWidget.addChild(labelWidget);
//
//        return literalWidget;
//    }

//    public Widget createFieldWidget(String fieldName) {
//        Scene scene = getScene();
//
//        Widget fieldWidget = new Widget(scene);
//        fieldWidget.setLayout(LayoutFactory.createHorizontalFlowLayout());
//
//        LabelWidget visibilityLabel = new LabelWidget(scene);
//        visibilityLabel.setLabel("+");
//        fieldWidget.addChild(visibilityLabel);
//
//        LabelWidget labelWidget = new LabelWidget(scene);
//        labelWidget.setLabel(fieldName);
//        labelWidget.getActions().addAction(nameEditorAction);
//        //labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(fieldWidget)));
//
//        fieldWidget.addChild(labelWidget);
//
//        return fieldWidget;
//    }

//    public Widget createMethodWidget(String methodName) {
//        Scene scene = getScene();
//        Widget widget = new Widget(scene);
//        widget.setLayout(LayoutFactory.createHorizontalFlowLayout());
//
//        LabelWidget visibilityLabel = new LabelWidget(scene);
//        visibilityLabel.setLabel("+");
//        widget.addChild(visibilityLabel);
//
//        LabelWidget labelWidget = new LabelWidget(scene);
//        labelWidget.setLabel(methodName);
//        widget.addChild(labelWidget);
//        labelWidget.getActions().addAction(nameEditorAction);
//        //labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(widget)));
//
//        return widget;
//    }

//    public final void addFieldWidget(FieldWidget fieldWidget) {
//        addMember(fieldsContainer, fieldWidget);
//    }
//
//    public final void addMethodWidget(MethodWidget operationWidget) {
//        addMember(methodsContainer, operationWidget);
//    }
//
//    public final void addConstructorWidget(ConstructorWidget operationWidget) {
//        addMember(methodsContainer, operationWidget);
//    }

    public final void addLiteralWidget(LiteralWidget literalWidget) {
        addMember(literalsContainer, literalWidget);
    }

}
