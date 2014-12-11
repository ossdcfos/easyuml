package org.uml.visual.widgets.components;

import org.uml.visual.widgets.members.MethodWidget;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.members.Method;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.providers.ComponentWidgetAcceptProvider;
import org.uml.visual.widgets.providers.popups.InterfacePopupMenuProvider;

/**
 *
 * @author hrza
 */
public class InterfaceWidget extends ComponentWidgetBase {

    private final Widget methodsContainer;

    public InterfaceWidget(ClassDiagramScene scene, InterfaceComponent interfaceComponent) {
        super(scene, interfaceComponent);

        Widget headerWidget = new Widget(scene);
        headerWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        headerWidget.setBorder(EMPTY_BORDER_4);

        LabelWidget stereotip = new LabelWidget(scene, "<<interface>>");
        stereotip.setAlignment(LabelWidget.Alignment.CENTER);
        headerWidget.addChild(stereotip);

        nameWidget.setLabel(component.getName());
        headerWidget.addChild(nameWidget);
        addChild(headerWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        methodsContainer = new Widget(scene);
        methodsContainer.setMinimumSize(CONTAINER_MIN_DIMENSION);
        methodsContainer.setLayout(LayoutFactory.createVerticalFlowLayout());
        methodsContainer.setOpaque(false);
        methodsContainer.setBorder(EMPTY_BORDER_4);
        LabelWidget operationName = new LabelWidget(scene);
        methodsContainer.addChild(operationName);
        addChild(methodsContainer);

        this.nameWidget.setLabel(component.getName());

        getActions().addAction(ActionFactory.createAcceptAction(new ComponentWidgetAcceptProvider()));
        getActions().addAction(ActionFactory.createPopupMenuAction(new InterfacePopupMenuProvider(this)));
        
        
        for (Method methodComp : interfaceComponent.getMethods()) {
            MethodWidget mw = new MethodWidget(getClassDiagramScene(), methodComp);
            addMember(methodsContainer, mw);
        }
    }

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
//        labelWidget.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
//        widget.addChild(labelWidget);
//        labelWidget.getActions().addAction(nameEditorAction);
//        //labelWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new MethodPopupMenuProvider(widget)));
//
//        return widget;
//    }

    public void addMethodWidget(MethodWidget operationWidget) {
        addMember(methodsContainer, operationWidget);
    }

    @Override
    public InterfaceComponent getComponent() {
        return (InterfaceComponent) component;
    }
}
