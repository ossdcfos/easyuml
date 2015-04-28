package org.uml.visual.widgets.relations;

import org.uml.model.relations.RelationBase;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.anchors.TriangleEquilateralAnchorShape;

/**
 *
 * @author Boris Perović
 */
public class IsRelationWidget extends RelationBaseWidget {

    public IsRelationWidget(RelationBase relation, ClassDiagramScene scene) {
        super(relation, scene);
        setTargetAnchorShape(new TriangleEquilateralAnchorShape(10, false));

        relation.setName("is");
        name.setLabel("is");
    }
}
