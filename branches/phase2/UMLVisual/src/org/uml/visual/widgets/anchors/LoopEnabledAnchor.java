package org.uml.visual.widgets.anchors;

import java.awt.Point;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author Boris
 */
public class LoopEnabledAnchor extends Anchor {

    public LoopEnabledAnchor(Widget widget) {
        super(widget);
    }

    @Override
    public Result compute(Entry entry) {
        if (entry.getAttachedAnchor().getRelatedWidget() == entry.getOppositeAnchor().getRelatedWidget()) {
            Widget w = entry.getAttachedAnchor().getRelatedWidget();
            Point left = w.getLocation();
            left.translate(0, w.getMinimumSize().height / 2);
            Point top = w.getLocation();
            top.translate(w.getMinimumSize().width / 2, 0);
            if (entry.isAttachedToConnectionSource())
                return new Result(left, Direction.LEFT);
            //the - point - on - the - LEFT - side - of - the(entry.getAttachedAnchor().getRelatedWidget());
            else
                return new Result(top, Direction.TOP);
            //the - point - on - the - RIGHT - side - of - the(entry.getAttachedAnchor().getRelatedWidget());
        }
        
        return AnchorFactory.createRectangularAnchor(getRelatedWidget()).compute(entry);
    }

}
