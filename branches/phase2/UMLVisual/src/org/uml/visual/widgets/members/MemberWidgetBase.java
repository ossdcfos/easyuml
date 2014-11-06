/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.members;

import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.uml.model.members.Member;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.INameableWidget;

/**
 *
 * @author Jelena
 */
public abstract class MemberWidgetBase extends LabelWidget /*IconNodeWidget*/ implements INameableWidget{

    
    public MemberWidgetBase(ClassDiagramScene scene) {
        super(scene);
        
        // To support hovering (in notifyStateChanged), otherwise a Provider is needed
        getActions().addAction(scene.createObjectHoverAction());
        
    }
    
    public Member getMember() {
        return null;
    }
    
    abstract public LabelWidget getNameLabel();

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        // u ovu metodu ubaciti reakcija ne hover, focus, selected itd.
        super.notifyStateChanged(previousState, state);
        
//        if (state.isHovered()) {
//            this.setBackground(new Color(0x808080));
//        }else {
//            this.setBackground(new Color(0xF7F7F7));
//        }
    }
    
}
