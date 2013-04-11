/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.Dimension;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.Member;
import org.uml.visual.widgets.providers.ClassConnectProvider;

/**
 *
 * @author Jelena
 */
public abstract class MemberWidgetBase extends IconNodeWidget implements Nameable{

    
    public MemberWidgetBase(ClassDiagramScene scene) {
        super(scene);
        
    }
    
    public Member getMember() {
        return null;
    }
    
    abstract public LabelWidget getNameLabel();
    
}
