package org.uml.visual.widgets.relations;

import java.awt.BasicStroke;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.anchor.AnchorShapeFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.uml.model.relations.RelationBase;
import org.uml.model.relations.UseRelation;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.popups.CardinalityPopupMenuProvider;

/**
 *
 * @author Boris
 */
public class UseRelationWidget extends RelationBaseWidget {

    public UseRelationWidget(RelationBase relation, ClassDiagramScene scene) {
        super(relation, scene);
        setStroke(DASHED);
        UseRelation useRelation = (UseRelation) relation;

        setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(45, 10));

        LabelWidget cardinalitySource = new LabelWidget(scene, useRelation.getCardinalitySource().toString());
        cardinalitySource.getActions().addAction(ActionFactory.createPopupMenuAction(new CardinalityPopupMenuProvider(useRelation, cardinalitySource, true)));
        addChild(cardinalitySource);
        setConstraint(cardinalitySource, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_SOURCE, 0.07f);

        LabelWidget cardinalityTarget = new LabelWidget(scene, useRelation.getCardinalityTarget().toString());
        cardinalityTarget.getActions().addAction(ActionFactory.createPopupMenuAction(new CardinalityPopupMenuProvider(useRelation, cardinalityTarget, false)));
        addChild(cardinalityTarget);
        setConstraint(cardinalityTarget, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_TARGET, 0.93f);
    }
}
