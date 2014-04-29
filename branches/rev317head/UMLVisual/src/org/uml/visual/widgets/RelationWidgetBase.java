/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.uml.model.RelationComponent;

/**
 *
 * @author Uros
 */
abstract public class RelationWidgetBase extends IconNodeWidget implements NameableWidget{

    public RelationWidgetBase(ClassDiagramScene scene) {
        super(scene);
    }
    
    public RelationComponent getRelationComponent () {
        return null;
    }
    
}
