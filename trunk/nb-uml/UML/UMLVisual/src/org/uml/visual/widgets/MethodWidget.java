/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Utilities;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.Member;
import org.uml.model.Method;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.ClassPopupMenuProvider;
import org.uml.visual.widgets.providers.ClassWidgetAcceptProvider;
import org.uml.visual.widgets.providers.MethodPopupMenuProvider;

/**
 *
 * @author Jelena
 */
public class MethodWidget extends MemberWidgetBase{

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
        }
        else {
            //poruka
        }
    }

    @Override
    public String getName() {
        return methodNameWidget.getLabel();
    }

    @Override
    public Member getMember() {
        return methodComponent;
    }

    

    
    
    
}
