/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.uml.model.Member;
import org.uml.model.Method;
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

    public MethodWidget(ClassDiagramScene scene, Method method) {
        super(scene);
        this.methodComponent = method;
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
        visibilityLabel = new LabelWidget(getScene());
        visibilityLabel.setLabel("+");
        this.addChild(visibilityLabel);

        methodNameWidget = new LabelWidget(getScene());
        methodNameWidget.setLabel(methodComponent.getName());
        this.addChild(methodNameWidget);
        methodNameWidget.getActions().addAction(nameEditorAction);
        methodNameWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new MethodPopupMenuProvider(this)));

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
            methodComponent.getDeclaringClass().componentNameChanged(methodComponent, oldName);
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

    @Override
    public void setAttributes(String attributes) {
        
    }
}
