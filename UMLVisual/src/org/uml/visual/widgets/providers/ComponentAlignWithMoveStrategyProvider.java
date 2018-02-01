package org.uml.visual.widgets.providers;

/**
 *
 * @author Boris Perović
 */
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.widget.Widget;

import java.awt.*;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 * @author David Kaspar
 */
public final class ComponentAlignWithMoveStrategyProvider extends AlignWithSupport implements MoveStrategy, MoveProvider {

    static final BasicStroke STROKE = new BasicStroke(1.0f, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT, 5.0f, new float[]{6.0f, 3.0f}, 0.0f);
    static final AlignWithMoveDecorator ALIGN_WITH_MOVE_DECORATOR_DEFAULT = new AlignWithMoveDecorator() {
        public ConnectionWidget createLineWidget(Scene scene) {
            ConnectionWidget widget = new ConnectionWidget(scene);
            widget.setStroke(STROKE);
            widget.setForeground(Color.BLUE);
            return widget;
        }
    };
    private ClassDiagramScene scene;
    private boolean outerBounds;
    private Point startLocation;
    
    public ComponentAlignWithMoveStrategyProvider(ClassDiagramScene scene, boolean outerBounds) {
        super(new SingleLayerAlignWithWidgetCollector(scene.getMainLayer(), outerBounds), scene.getInterractionLayer(), ALIGN_WITH_MOVE_DECORATOR_DEFAULT);
        this.scene = scene;
        this.outerBounds = outerBounds;
    }

    public Point locationSuggested(Widget widget, Point originalLocation, Point suggestedLocation) {
        Point widgetLocation = widget.getLocation();
        Rectangle widgetBounds = outerBounds ? widget.getBounds() : widget.getClientArea();
        Rectangle bounds = widget.convertLocalToScene(widgetBounds);
        bounds.translate(suggestedLocation.x - widgetLocation.x, suggestedLocation.y - widgetLocation.y);
        Insets insets = widget.getBorder().getInsets();
        if (!outerBounds) {
            suggestedLocation.x += insets.left;
            suggestedLocation.y += insets.top;
        }
        Point point = super.locationSuggested(widget, bounds, widget.getParentWidget().convertLocalToScene(suggestedLocation), true, true, true, true);
        if (!outerBounds) {
            point.x -= insets.left;
            point.y -= insets.top;
        }
        return widget.getParentWidget().convertSceneToLocal(point);
    }

    public void movementStarted(Widget widget) {
        show();
        startLocation = widget.getLocation();
    }

    public void movementFinished(Widget widget) {
        hide();
        if(!startLocation.equals(widget.getLocation())) {
            scene.updateComponents();
            scene.getUmlTopComponent().notifyModified();
        }
    }

    public Point getOriginalLocation(Widget widget) {
        return ActionFactory.createDefaultMoveProvider().getOriginalLocation(widget);
    }

    public void setNewLocation(Widget widget, Point location) {
        ActionFactory.createDefaultMoveProvider().setNewLocation(widget, location);
    }
}
