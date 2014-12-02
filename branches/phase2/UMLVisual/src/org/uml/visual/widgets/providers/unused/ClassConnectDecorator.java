package org.uml.visual.widgets.providers.unused;

import java.awt.Point;
import org.netbeans.api.visual.action.ConnectDecorator;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author "NUGS"
 */
public class ClassConnectDecorator implements ConnectDecorator{

    @Override
    public ConnectionWidget createConnectionWidget(Scene scene) {
        ConnectionWidget widget= new ConnectionWidget(scene);
        return widget;
               
    }

    @Override
    public Anchor createSourceAnchor(Widget widget) {
        Anchor sourceAnchor=AnchorFactory.createRectangularAnchor(widget);
        return sourceAnchor;
    }

    @Override
    public Anchor createTargetAnchor(Widget widget) {
        Anchor targetAnchor=AnchorFactory.createRectangularAnchor(widget);
        return targetAnchor;
    }

    @Override
    public Anchor createFloatAnchor(Point point) {
        Anchor floatAnchor=AnchorFactory.createFixedAnchor(point);
        return floatAnchor;
    }
    
}
