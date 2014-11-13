/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.members;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.openide.util.Exceptions;
import org.uml.model.members.Literal;
import org.uml.model.members.Member;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.popups.LiteralPopupProvider;

/**
 *
 * @author Jelena
 */
public class LiteralWidget extends MemberWidgetBase{

    Literal literalComponent;
    private WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    //LabelWidget visibilityLabel;
    LabelWidget nameLabel;
    
    public LiteralWidget(ClassDiagramScene scene, Literal literal) {
        super(scene, literal);
        this.literalComponent = literal;
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
//        visibilityLabel = new LabelWidget(getScene());
//        visibilityLabel.setLabel("+");
//        this.addChild(visibilityLabel);

        nameLabel = new LabelWidget(getScene());
        nameLabel.setLabel(literal.getName());
        this.addChild(nameLabel);
        nameLabel.getActions().addAction(nameEditorAction);
        nameLabel.getActions().addAction(ActionFactory.createPopupMenuAction(new LiteralPopupProvider(this)));

    }
    
    @Override
    public LabelWidget getNameLabel() {
        return nameLabel;
    }

    @Override
    public void setName(String newName) {
        if (getName().equals(newName)) {
            return;
        }
        String oldName = literalComponent.getName();
        if (!literalComponent.getDeclaringClass().nameExists(newName)) {
            nameLabel.setLabel(newName);
            literalComponent.setName(newName);
            literalComponent.getDeclaringClass().notifyMemberNameChanged(literalComponent, oldName);
        }
        else {
            //poruka
        }
    }

    @Override
    public String getName() {
        return literalComponent.getName();
    }

    @Override
    public Member getMember() {
        return literalComponent;
    }

    @Override
    public void setAttributes(String attributes) {
        
    }

    @Override
    protected void setSelected(boolean isSelected) {
        if(isSelected){
            nameLabel.setForeground(SELECT_FONT_COLOR);
        } else {
            nameLabel.setForeground(DEFAULT_FONT_COLOR);
        }
    }
    
}
