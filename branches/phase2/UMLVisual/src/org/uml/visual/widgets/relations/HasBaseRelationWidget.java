package org.uml.visual.widgets.relations;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.anchor.AnchorShapeFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.uml.model.relations.HasBaseRelation;
import org.uml.model.relations.RelationBase;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.anchors.RhombusAnchorShape;
import org.uml.visual.widgets.popups.CardinalityPopupMenuProvider;

/**
 *
 * @author Boris
 */
public class HasBaseRelationWidget extends RelationBaseWidget {

    public HasBaseRelationWidget(RelationBase relation, ClassDiagramScene scene) {
        super(relation, scene);
        HasBaseRelation hasRelation = (HasBaseRelation) relation;

        switch (hasRelation.getType()) {
            case DEFAULT:
                break;
            case AGGREGATION:
                setSourceAnchorShape(new RhombusAnchorShape(45, 10, false));
                break;
            case COMPOSITION:
                setSourceAnchorShape(new RhombusAnchorShape(45, 10, true));
                break;
        }
        
        setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(45, 10));

        LabelWidget cardinalityTarget = new LabelWidget(scene, hasRelation.getCardinalityTarget().toString());
        cardinalityTarget.getActions().addAction(ActionFactory.createPopupMenuAction(new CardinalityPopupMenuProvider(hasRelation, cardinalityTarget, false)));
        addChild(cardinalityTarget);
        setConstraint(cardinalityTarget, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_TARGET, 0.93f);
    }
}
