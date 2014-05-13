/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import org.netbeans.api.visual.widget.LabelWidget;
import org.uml.model.Member;

/**
 *
 * @author Jelena
 */
public abstract class MemberWidgetBase extends LabelWidget /*IconNodeWidget*/ implements NameableWidget{

    
    public MemberWidgetBase(ClassDiagramScene scene) {
        super(scene);
        
    }
    
    public Member getMember() {
        return null;
    }
    
    abstract public LabelWidget getNameLabel();

    
    
}
