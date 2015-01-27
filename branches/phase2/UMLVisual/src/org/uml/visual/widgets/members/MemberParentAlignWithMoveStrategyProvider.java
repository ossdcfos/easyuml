package org.uml.visual.widgets.members;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.AlignWithMoveDecorator;
import org.netbeans.api.visual.action.AlignWithWidgetCollector;
import org.netbeans.api.visual.action.MoveProvider;
import org.netbeans.api.visual.action.MoveStrategy;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Boris
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

    private boolean outerBounds;
    private Point startPoint;

    public MemberParentAlignWithMoveStrategyProvider(AlignWithWidgetCollector collector, LayerWidget interractionLayer, AlignWithMoveDecorator decorator, boolean outerBounds) {
        super(collector, interractionLayer, decorator);
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
        show();
    }

    public void movementFinished(Widget widget) {
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

final class SingleLayerAlignWithWidgetCollector implements AlignWithWidgetCollector {

    private LayerWidget collectionLayer;
    private boolean outerBounds;

    public SingleLayerAlignWithWidgetCollector(LayerWidget collectionLayer, boolean outerBounds) {
        this.collectionLayer = collectionLayer;
        this.outerBounds = outerBounds;
    }

    public java.util.List<Rectangle> getRegions(Widget movingWidget) {
        java.util.List<Widget> children = collectionLayer.getChildren();
        ArrayList<Rectangle> regions = new ArrayList<Rectangle>(children.size());
        for (Widget widget : children) {
            if (widget != movingWidget)
                regions.add(widget.convertLocalToScene(outerBounds ? widget.getBounds() : widget.getClientArea()));
        }
        return regions;
    }
}

class AlignWithSupport {

    private static final int GRAVITY = 10;

    private AlignWithWidgetCollector collector;
    private LayerWidget interractionLayer;
    private AlignWithMoveDecorator decorator;

    private ConnectionWidget lineWidget1, lineWidget2;

    public AlignWithSupport(AlignWithWidgetCollector collector, LayerWidget interractionLayer, AlignWithMoveDecorator decorator) {
        this.collector = collector;
        this.interractionLayer = interractionLayer;
        this.decorator = decorator;
    }

    protected Point locationSuggested(Widget widget, Rectangle sceneWidgetBounds, Point suggestedLocation, boolean horizontal, boolean vertical, boolean bothSides, boolean snapHack) {
        Point point = new Point(suggestedLocation);
        Collection<Rectangle> regions = collector.getRegions(widget);

        if (horizontal) {
            boolean snap = false;
            int xs = 0, x = 0, dx = 0, y1 = 0, y2 = 0;

            int b1 = sceneWidgetBounds.x;
            int b2 = sceneWidgetBounds.x + sceneWidgetBounds.width;
            int b3 = sceneWidgetBounds.x + (sceneWidgetBounds.width / 2);

            for (Rectangle rectangle : regions) {
                int a1 = rectangle.x;
                int a2 = a1 + rectangle.width;
                int a3 = a1 + (rectangle.width / 2);

                int d;
                boolean snapNow = false;

                d = Math.abs(a1 - b1);
                if ((snap && d < dx) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    x = xs = a1;
                    dx = d;
                }

                if (bothSides) {
                    d = Math.abs(a1 - b2);
                    if ((snap && d < dx) || (!snap && d < GRAVITY)) {
                        snap = snapNow = true;
                        x = a1;
                        xs = a1 - sceneWidgetBounds.width;
                        dx = d;
                    }
                }

                d = Math.abs(a2 - b1);
                if ((snap && d < dx) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    x = xs = a2;
                    dx = d;
                }

                if (bothSides) {
                    d = Math.abs(a2 - b2);
                    if ((snap && d < dx) || (!snap && d < GRAVITY)) {
                        snap = snapNow = true;
                        x = a2;
                        xs = a2 - sceneWidgetBounds.width;
                        dx = d;
                    }
                }

                d = Math.abs(a3 - b3);
                if ((snap && d < dx) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    xs = a3 - (b3 - b1);
                    x = a3;
                    dx = d;
                }

                if (snapNow) {
                    y1 = rectangle.y;
                    y2 = rectangle.y + rectangle.height;
                }
            }

            if (snap) {
                point.x = xs;
                if (snapHack)
                    point.x -= widget.getBounds().x;
            }

            if (interractionLayer != null)
                lineWidget1.setControlPoints(snap ? Arrays.asList(
                        new Point(x, Math.min(sceneWidgetBounds.y, y1)),
                        new Point(x, Math.max(sceneWidgetBounds.y + sceneWidgetBounds.height, y2))
                ) : Collections.<Point>emptyList(), true);
        }

        if (vertical) {
            boolean snap = false;
            int ys = 0, y = 0, dy = 0, x1 = 0, x2 = 0;

            int b1 = sceneWidgetBounds.y;
            int b2 = sceneWidgetBounds.y + sceneWidgetBounds.height;
            int b3 = sceneWidgetBounds.y + (sceneWidgetBounds.height / 2);

            for (Rectangle rectangle : regions) {
                int a1 = rectangle.y;
                int a2 = a1 + rectangle.height;
                int a3 = a1 + (rectangle.height / 2);

                int d;
                boolean snapNow = false;

                d = Math.abs(a1 - b1);
                if ((snap && d < dy) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    y = ys = a1;
                    dy = d;
                }

                d = Math.abs(a1 - b2);
                if ((snap && d < dy) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    ys = a1 - sceneWidgetBounds.height;
                    y = a1;
                    dy = d;
                }

                d = Math.abs(a2 - b1);
                if ((snap && d < dy) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    y = ys = a2;
                    dy = d;
                }

                d = Math.abs(a2 - b2);
                if ((snap && d < dy) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    ys = a2 - sceneWidgetBounds.height;
                    y = a2;
                    dy = d;
                }

                d = Math.abs(a3 - b3);
                if ((snap && d < dy) || (!snap && d < GRAVITY)) {
                    snap = snapNow = true;
                    ys = a3 - (b3 - b1);
                    y = a3;
                    dy = d;
                }

                if (snapNow) {
                    x1 = rectangle.x;
                    x2 = rectangle.x + rectangle.width;
                }
            }
            if (snap) {
                point.y = ys;
                if (snapHack)
                    point.y -= widget.getBounds().y;
            }

            if (interractionLayer != null)
                lineWidget2.setControlPoints(snap ? Arrays.asList(
                        new Point(Math.min(sceneWidgetBounds.x, x1), y),
                        new Point(Math.max(sceneWidgetBounds.x + sceneWidgetBounds.width, x2), y)
                ) : Collections.<Point>emptyList(), true);
        }

        return point;
    }

    public void show() {
        if (interractionLayer != null) {
            if (lineWidget1 == null)
                lineWidget1 = decorator.createLineWidget(interractionLayer.getScene());
            if (lineWidget2 == null)
                lineWidget2 = decorator.createLineWidget(interractionLayer.getScene());
            interractionLayer.addChild(lineWidget1);
            interractionLayer.addChild(lineWidget2);
            lineWidget1.setControlPoints(Collections.<Point>emptySet(), true);
            lineWidget2.setControlPoints(Collections.<Point>emptySet(), true);
        }
    }

    public void hide() {
        if (interractionLayer != null) {
            interractionLayer.removeChild(lineWidget1);
            interractionLayer.removeChild(lineWidget2);
        }
    }
}
