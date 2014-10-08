package org.uml.visual.widgets.actions.resize;

import java.awt.Rectangle;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.action.ResizeStrategy;
import org.netbeans.api.visual.widget.Widget;

abstract public class ResizeStrategyProvider implements ResizeStrategy, ResizeProvider {

    abstract Rectangle boundsSuggested(Widget widget, Rectangle originalSceneRectangle, Rectangle rectangle, ControlPoint controlPoint, int modifiersEx);

    abstract void resizingFinished(Widget widget, int modifiersEx);

    abstract void resizingStarted(Widget widget, int modifiersEx);

}
