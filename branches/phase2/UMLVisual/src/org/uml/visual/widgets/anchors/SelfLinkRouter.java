package org.uml.visual.widgets.anchors;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.components.ComponentBase;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author treyspiva
 */
public class SelfLinkRouter implements Router
{
    public static final int BEND_SPACE = 10;
    
    @Override
    public List<Point> routeConnection(ConnectionWidget widget)
    {
        Anchor sourceAnchor = widget.getSourceAnchor();
        Anchor targetAnchor = widget.getTargetAnchor();
        
        int modifier = findTotalLinkToSelfBefore(widget);
        int adjustmentValue = BEND_SPACE * modifier;
        
        ArrayList < Point > retVal = new ArrayList <>();
        
        Point sourcePt = sourceAnchor.compute(widget.getSourceAnchorEntry()).getAnchorSceneLocation();
        Point targetPt = targetAnchor.compute(widget.getTargetAnchorEntry()).getAnchorSceneLocation();
        
        retVal.add(sourcePt);
        
        if(sourcePt.y < targetPt.y)
        {
            // Default to the source being to the right of the target.
            int xAdjust = adjustmentValue;
            
            // Source is above target
            if(sourcePt.x < targetPt.x)
            {
                // Source is to the left of the target.
                xAdjust = -adjustmentValue;
            }
            
            retVal.add(new Point(sourcePt.x + xAdjust, sourcePt.y));
            retVal.add(new Point(sourcePt.x + xAdjust, targetPt.y + adjustmentValue));
            retVal.add(new Point(targetPt.x , targetPt.y + adjustmentValue));
        }
        else
        {
            // Default to the source being to the right of the target.
            int xAdjust = adjustmentValue;

            // Source is below the target.
            if(sourcePt.x < targetPt.x)
            {
                // Source is to the left of the target.
                xAdjust = -adjustmentValue;
            }
            
            retVal.add(new Point(sourcePt.x + xAdjust, sourcePt.y));
            retVal.add(new Point(sourcePt.x + xAdjust, targetPt.y - adjustmentValue));
            retVal.add(new Point(targetPt.x, targetPt.y - adjustmentValue));
        }
        
        retVal.add(targetPt);
        
        return retVal;
        
    }

    private int findTotalLinkToSelfBefore(ConnectionWidget widget)
    {
        int retVal = 0;
        
        if (widget.getScene() instanceof GraphScene)
        {
            ClassDiagramScene scene = (ClassDiagramScene) widget.getScene();
            
            Widget nodeWidget = widget.getSourceAnchor().getRelatedWidget();
            ComponentBase nodeObject = (ComponentBase)scene.findObject(nodeWidget);
            
            for(Object edgeObject : scene.findNodeEdges(nodeObject, true, false))
            {
                ConnectionWidget edgeWidget = (ConnectionWidget) scene.findWidget(edgeObject);
                Widget targetNode = edgeWidget.getTargetAnchor().getRelatedWidget();
                Widget sourceNode = edgeWidget.getSourceAnchor().getRelatedWidget();
                
                if(targetNode!=null && targetNode.equals(sourceNode))
                {
                    // We found a self link.
                    retVal++;
                }
                
                if(edgeWidget.equals(widget) == true)
                {
                    // We found the edge we where lookig to find.
                    break;
                }
            }
        }
        
        return retVal;
    }
}
