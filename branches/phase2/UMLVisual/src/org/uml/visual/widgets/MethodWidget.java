package org.uml.visual.widgets;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.openide.util.Exceptions;
import org.uml.model.Member;
import org.uml.model.Method;
import org.uml.visual.parser.WidgetParser;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.MethodPopupMenuProvider;

/**
 *
 * @author Jelena
 */
public class MethodWidget extends MemberWidgetBase {

    Method methodComponent;
    private WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    LabelWidget visibilityLabel;
    LabelWidget methodNameWidget;
    WidgetParser wp;

    public MethodWidget(ClassDiagramScene scene, Method method) {
        super(scene);
        this.methodComponent = method;
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
        visibilityLabel = new LabelWidget(getScene());
        visibilityLabel.setLabel("+");
        this.addChild(visibilityLabel);

        methodNameWidget = new LabelWidget(getScene());
        methodNameWidget.setLabel(methodComponent.getSignatureForLabel());
        this.addChild(methodNameWidget);
        methodNameWidget.getActions().addAction(nameEditorAction);
        methodNameWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new MethodPopupMenuProvider(this)));
        wp = new WidgetParser();
        refreshLabel();
    }

    @Override
    public LabelWidget getNameLabel() {
        return methodNameWidget;
    }

    @Override
    public void setName(String newName) {
        if (getName().equals(newName)) {
            return;
        }
        String oldName = methodComponent.getName();
        if (!methodComponent.getDeclaringClass().nameExists(newName)) {
            methodNameWidget.setLabel(newName);
            methodComponent.setName(newName);
            methodComponent.getDeclaringClass().notifyMemberNameChanged(methodComponent, oldName);
        } else {
            //poruka
        }
    }

    @Override
    public String getName() {
        return methodComponent.getName();
    }

    @Override
    public Member getMember() {
        return methodComponent;
    }

    public LabelWidget getMethodNameWidget() {
        return methodNameWidget;
    }

    public Method getMethodComponent() {
        return methodComponent;
    }

    @Override
    public void setAttributes(String attributes) {
        String oldName = methodComponent.getName();
        wp.fillMethodComponents(methodComponent, attributes);
        String newName = methodComponent.getName();
        if (newName.equals(oldName)) {
            throw new RuntimeException("Error: name already exists.");
        } else {
            if (!methodComponent.getDeclaringClass().nameExists(newName)) {
                methodComponent.getDeclaringClass().notifyMemberNameChanged(methodComponent, oldName);
            } else {
                methodComponent.setName(oldName);
                throw new RuntimeException("Error: name already exists.");
            }
        }
        methodNameWidget.setLabel(methodComponent.getSignatureForLabel());
        refreshLabel();

    }

    public void refreshLabel() {
        switch (methodComponent.getVisibility()) {
            case PUBLIC:
                visibilityLabel.setLabel("+");
                break;
            case PRIVATE:
                visibilityLabel.setLabel("-");
                break;
            case PROTECTED:
                visibilityLabel.setLabel("#");
                break;
            case PACKAGE:
                visibilityLabel.setLabel("~");
                break;
        }
    }
}
