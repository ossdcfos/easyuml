package org.uml.visual.widgets.unused;

import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.uml.model.relations.RelationBase;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.IUMLWidget;

/**
 *
 * @author Uros
 */
abstract public class RelationWidgetBase extends IconNodeWidget implements IUMLWidget{

    public RelationWidgetBase(ClassDiagramScene scene) {
        super(scene); 
    }
    
    public RelationBase getRelationComponent () {
        return null;
    }
    
}
