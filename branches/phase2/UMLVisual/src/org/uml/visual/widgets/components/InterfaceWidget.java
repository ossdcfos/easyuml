package org.uml.visual.widgets.components;

import java.awt.Dimension;
import java.awt.event.MouseListener;
import java.util.concurrent.Callable;
import org.uml.visual.widgets.members.MethodWidget;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.members.Method;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.actions.MemberNameEditor;
import org.uml.visual.widgets.providers.ComponentWidgetAcceptProvider;
import org.uml.visual.widgets.popups.InterfacePopupMenuProvider;
import org.uml.visual.widgets.providers.CloseInplaceEditorOnClickAdapter;

/**
 *
 * @author hrza
 */
public class InterfaceWidget extends ComponentWidgetBase {

    private final MemberContainerWidget methodsContainer;

    public InterfaceWidget(ClassDiagramScene scene, InterfaceComponent interfaceComponent) {
        super(scene, interfaceComponent);
        setMinimumSize(MIN_DIMENSION_2ROW);

        Widget headerWidget = new Widget(scene);
        headerWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        headerWidget.setBorder(EMPTY_CONTAINER_BORDER);

        LabelWidget interfaceLabel = new LabelWidget(scene, "<<interface>>");
        interfaceLabel.setAlignment(LabelWidget.Alignment.CENTER);
        headerWidget.addChild(interfaceLabel);

        nameLabel.setLabel(component.getName());
        headerWidget.addChild(nameLabel);
        addChild(headerWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        methodsContainer = new MemberContainerWidget(scene, "method");
        methodsContainer.addAddAction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                addMethodWidget();
                return null;
            }
        });
        addChild(methodsContainer);

        this.nameLabel.setLabel(component.getName());

        getActions().addAction(ActionFactory.createAcceptAction(new ComponentWidgetAcceptProvider()));
        getActions().addAction(ActionFactory.createPopupMenuAction(new InterfacePopupMenuProvider(this)));
        
//        setMaximumSize(new Dimension(Integer.MAX_VALUE, MIN_DIMENSION_2ROW.height + (getLabelSizeForFont(ADD_LABEL_FONT_SIZE) + 2*EMPTY_BORDER_SIZE)));

        for (Method method : interfaceComponent.getMethods()) {
            MethodWidget methodWidget = new MethodWidget(getClassDiagramScene(), method);
            addMember(methodsContainer, methodWidget);
        }
    }

    @Override
    public InterfaceComponent getComponent() {
        return (InterfaceComponent) component;
    }
    
    public void addMethodWidget() {
        Method method = new Method("untitledMethod", "void");
        getComponent().addMethod(method);
        MethodWidget methodWidget = new MethodWidget(getClassDiagramScene(), method);
        addMember(methodsContainer, methodWidget);
        getScene().validate();

        WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new MemberNameEditor(methodWidget));
        ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(methodWidget.getNameLabel());
        MouseListener mouseListener = new CloseInplaceEditorOnClickAdapter(nameEditorAction);
        getScene().getView().addMouseListener(mouseListener);

    }
}
