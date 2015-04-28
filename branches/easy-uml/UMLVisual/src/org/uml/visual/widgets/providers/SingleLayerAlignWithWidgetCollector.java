package org.uml.visual.widgets.providers;


import java.awt.Rectangle;
import java.util.ArrayList;
import org.netbeans.api.visual.action.AlignWithWidgetCollector;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;

public class SingleLayerAlignWithWidgetCollector implements AlignWithWidgetCollector {

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