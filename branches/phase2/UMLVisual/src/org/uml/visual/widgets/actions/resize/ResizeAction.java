package org.uml.visual.widgets.actions.resize;

import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.action.ResizeControlPointResolver;
import org.netbeans.api.visual.widget.Widget;

import java.awt.*;
import java.awt.event.MouseEvent;
import org.netbeans.api.visual.action.ActionFactory;

/**
 *
 * Rezing a widget based on insets of the widget border.
 */
public final class ResizeAction extends WidgetAction.LockedAdapter {

    private ResizeControlPointResolver resolver;
    private ResizeStrategyProvider providerstrategy;

    private Widget resizingWidget = null;
    private ResizeProvider.ControlPoint controlPoint;
    private Rectangle originalSceneRectangle = null;
    private Insets insets;
    private Point dragSceneLocation = null;

    public ResizeAction(ResizeStrategyProvider providerstrategy) {
        this.providerstrategy = providerstrategy;
        this.resolver = ActionFactory.createDefaultResizeControlPointResolver();
    }

    protected boolean isLocked() {
        return resizingWidget != null;
    }

    public State mousePressed(Widget widget, WidgetMouseEvent event) {
        if (isLocked()) {
            return State.createLocked(widget, this);
        }
        if (event.getButton() == MouseEvent.BUTTON1 && event.getClickCount() == 1) {
            insets = widget.getBorder().getInsets();
            controlPoint = resolver.resolveControlPoint(widget, event.getPoint());
            if (controlPoint != null) {
                resizingWidget = widget;
                originalSceneRectangle = null;
                if (widget.isPreferredBoundsSet()) {
                    originalSceneRectangle = widget.getPreferredBounds();
                }
                if (originalSceneRectangle == null) {
                    originalSceneRectangle = widget.getBounds();
                }
                if (originalSceneRectangle == null) {
                    originalSceneRectangle = widget.getPreferredBounds();
                }
                dragSceneLocation = widget.convertLocalToScene(event.getPoint());
                providerstrategy.resizingStarted(widget, event.getModifiersEx());
                return State.createLocked(widget, this);
            }
        }
        return State.REJECTED;
    }

    public State mouseReleased(Widget widget, WidgetMouseEvent event) {
        boolean state = resize(widget, event.getPoint(), event);
        if (state) {
            resizingWidget = null;
            providerstrategy.resizingFinished(widget, event.getModifiersEx());
        }
        return state ? State.CONSUMED : State.REJECTED;
    }

    public State mouseDragged(Widget widget, WidgetMouseEvent event) {
        return resize(widget, event.getPoint(), event) ? State.createLocked(widget, this) : State.REJECTED;
    }

    private boolean resize(Widget widget, Point newLocation, WidgetMouseEvent event) {
        if (resizingWidget != widget) {
            return false;
        }

        newLocation = widget.convertLocalToScene(newLocation);
        int dx = newLocation.x - dragSceneLocation.x;
        int dy = newLocation.y - dragSceneLocation.y;
        int minx = insets.left + insets.right;
        int miny = insets.top + insets.bottom;

        Rectangle rectangle = new Rectangle(originalSceneRectangle);
        switch (controlPoint) {
            case BOTTOM_CENTER:
                resizeToBottom(miny, rectangle, dy);
                break;
            case BOTTOM_LEFT:
                resizeToLeft(minx, rectangle, dx);
                resizeToBottom(miny, rectangle, dy);
                break;
            case BOTTOM_RIGHT:
                resizeToRight(minx, rectangle, dx);
                resizeToBottom(miny, rectangle, dy);
                break;
            case CENTER_LEFT:
                resizeToLeft(minx, rectangle, dx);
                break;
            case CENTER_RIGHT:
                resizeToRight(minx, rectangle, dx);
                break;
            case TOP_CENTER:
                resizeToTop(miny, rectangle, dy);
                break;
            case TOP_LEFT:
                resizeToLeft(minx, rectangle, dx);
                resizeToTop(miny, rectangle, dy);
                break;
            case TOP_RIGHT:
                resizeToRight(minx, rectangle, dx);
                resizeToTop(miny, rectangle, dy);
                break;
        }

        widget.setPreferredBounds(providerstrategy.boundsSuggested(widget, originalSceneRectangle, rectangle, controlPoint, event.getModifiersEx()));
        return true;
    }

    private static void resizeToTop(int miny, Rectangle rectangle, int dy) {
        if (rectangle.height - dy < miny) {
            dy = rectangle.height - miny;
        }
        rectangle.y += dy;
        rectangle.height -= dy;
    }

    private static void resizeToBottom(int miny, Rectangle rectangle, int dy) {
        if (rectangle.height + dy < miny) {
            dy = miny - rectangle.height;
        }
        rectangle.height += dy;
    }

    private static void resizeToLeft(int minx, Rectangle rectangle, int dx) {
        if (rectangle.width - dx < minx) {
            dx = rectangle.width - minx;
        }
        rectangle.x += dx;
        rectangle.width -= dx;
    }

    private static void resizeToRight(int minx, Rectangle rectangle, int dx) {
        if (rectangle.width + dx < minx) {
            dx = minx - rectangle.width;
        }
        rectangle.width += dx;
    }
}
