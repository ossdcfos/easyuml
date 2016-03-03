package org.uml.visual.widgets.providers;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.AlignWithMoveDecorator;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author Boris Perović
 * Small modifications to classes from org.netbeans.modules.visual, as it is private package
 */
public final class MemberParentAlignWithMoveStrategyProvider extends AlignWithSupport implements MoveStrategy, MoveProvider {

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
    private Point startLocation;
    private boolean outerBounds;

    public MemberParentAlignWithMoveStrategyProvider(ClassDiagramScene scene, boolean outerBounds) {
        super(new SingleLayerAlignWithWidgetCollector(scene.getMainLayer(), outerBounds), scene.getInterractionLayer(), ALIGN_WITH_MOVE_DECORATOR_DEFAULT);
        this.scene = scene;
        this.outerBounds = outerBounds;
    }

    public Point locationSuggested(Widget widget, Point originalLocation, Point suggestedLocation) {
        widget = widget.getParentWidget().getParentWidget();
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
        widget = widget.getParentWidget().getParentWidget();
        startLocation = widget.getLocation();
        show();
    }

    public void movementFinished(Widget widget) {
        widget = widget.getParentWidget().getParentWidget();
        Point newLocation = widget.getLocation();
        if (!startLocation.equals(newLocation)) {
            scene.getUmlTopComponent().notifyModified();
        }
        hide();
    }

    public Point getOriginalLocation(Widget widget) {
        widget = widget.getParentWidget().getParentWidget();
        return ActionFactory.createDefaultMoveProvider().getOriginalLocation(widget);
    }

    public void setNewLocation(Widget widget, Point location) {
        widget = widget.getParentWidget().getParentWidget();
        ActionFactory.createDefaultMoveProvider().setNewLocation(widget, location);
    }
}
