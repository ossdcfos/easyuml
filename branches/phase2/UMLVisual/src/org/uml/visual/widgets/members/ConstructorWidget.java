/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.members;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JOptionPane;
import org.uml.visual.widgets.providers.popups.ConstructorPopupMenuProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.uml.model.members.Constructor;
import org.uml.model.members.Member;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.actions.NameEditorAction;

/**
 *
 * @author Jelena
 */
public class ConstructorWidget extends MemberWidgetBase implements PropertyChangeListener{

    Constructor constructorComponent;
    private WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    LabelWidget visibilityLabel;
    LabelWidget nameLabel;
    
    public ConstructorWidget(ClassDiagramScene scene, Constructor constructor) {
        super(scene, constructor);
        this.constructorComponent = constructor;
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
        visibilityLabel = new LabelWidget(getScene());
        visibilityLabel.setLabel("+");
        this.addChild(visibilityLabel);

        nameLabel = new LabelWidget(getScene());
        nameLabel.setLabel(constructorComponent.getDeclaringClass().getName() + "()");
        this.addChild(nameLabel);
        //construktorNameWidget.getActions().addAction(nameEditorAction);
        nameLabel.getActions().addAction(ActionFactory.createPopupMenuAction(new ConstructorPopupMenuProvider(this)));
    }
    
    @Override
    public LabelWidget getNameLabel() {
        return nameLabel;
    }

    @Override
    public void setName(String newName) {
        nameLabel.setLabel(newName);
    }

    @Override
    public String getName() {
        return constructorComponent.getName();
    }

    @Override
    public Member getMember() {
        return constructorComponent;
    }

    @Override
    public void setAttributes(String attributes) {
        
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String newName = evt.getNewValue().toString();
        constructorComponent.setName(newName);
        setName(newName + "()");
    }

    @Override
    protected void setSelected(boolean isSelected) {
        if(isSelected){
            visibilityLabel.setForeground(SELECT_FONT_COLOR);
            nameLabel.setForeground(SELECT_FONT_COLOR);
        } else {
            visibilityLabel.setForeground(DEFAULT_FONT_COLOR);
            nameLabel.setForeground(DEFAULT_FONT_COLOR);
        }
    }
}
