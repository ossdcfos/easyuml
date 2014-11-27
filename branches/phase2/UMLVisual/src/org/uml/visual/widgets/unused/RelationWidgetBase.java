/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.unused;

import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.uml.model.relations.RelationBase;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.INameableWidget;

/**
 *
 * @author Uros
 */
abstract public class RelationWidgetBase extends IconNodeWidget implements INameableWidget{

    public RelationWidgetBase(ClassDiagramScene scene) {
        super(scene); 
    }
    
    public RelationBase getRelationComponent () {
        return null;
    }
    
}
