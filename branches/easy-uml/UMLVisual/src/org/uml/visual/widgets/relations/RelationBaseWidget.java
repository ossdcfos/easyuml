package org.uml.visual.widgets.relations;

import java.awt.BasicStroke;
import java.awt.Color;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.uml.model.relations.RelationBase;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.actions.RelationLabelTextFieldEditorAction;
import org.uml.visual.widgets.popups.RelationPopupMenuProvider;

/**
 *
 * @author Boris Perović
 */
public class RelationBaseWidget extends ConnectionWidget {

    protected final static BasicStroke DASHED
            = new BasicStroke(1.0f,
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    10.0f, new float[]{10.0f}, 0.0f);

    RelationBase relation;
    LabelWidget name;

    public RelationBaseWidget(RelationBase relation, ClassDiagramScene scene) {
        super(scene);
        this.relation = relation;

        name = new LabelWidget(scene, relation.getName());
        name.setOpaque(true);
        int transparentSceneRGBA = 0x00FFFFFF & ((Color) scene.getBackground()).getRGB();
        name.setBackground(new Color(0x7FFFFFFF | transparentSceneRGBA, true));

        // Layout
        addChild(name);
        setConstraint(name, LayoutFactory.ConnectionWidgetLayoutAlignment.CENTER, 0.5f);

        setEndPointShape(PointShape.SQUARE_FILLED_BIG);

        setControlPointShape(PointShape.SQUARE_FILLED_BIG);
        setPaintControlPoints(true);

        setRouter(RouterFactory.createFreeRouter());

        // Actions
        // Double-click name editor action
        name.getActions().addAction(ActionFactory.createInplaceEditorAction(new RelationLabelTextFieldEditorAction(relation)));
        // Right-click action
        getActions().addAction(ActionFactory.createPopupMenuAction(new RelationPopupMenuProvider(this)));
        // Control points actions
        getActions().addAction(ActionFactory.createAddRemoveControlPointAction());
        getActions().addAction(ActionFactory.createMoveControlPointAction(ActionFactory.createFreeMoveControlPointProvider(), ConnectionWidget.RoutingPolicy.UPDATE_END_POINTS_ONLY));
        // Selection and hover action
        getActions().addAction(scene.createSelectAction());
        getActions().addAction(scene.createWidgetHoverAction());
    }

    public RelationBase getRelation() {
        return relation;
    }
}
