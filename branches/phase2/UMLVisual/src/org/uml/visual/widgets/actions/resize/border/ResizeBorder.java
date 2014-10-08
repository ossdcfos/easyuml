package org.uml.visual.widgets.actions.resize.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import org.netbeans.api.visual.action.ResizeProvider;
import org.netbeans.api.visual.border.Border;


public class ResizeBorder implements Border {
    private static final BasicStroke STROKE = new BasicStroke (1.0f, BasicStroke.JOIN_BEVEL, BasicStroke.CAP_BUTT, 5.0f, new float[] { 6.0f, 3.0f }, 0.0f);

    private int thickness;
    private Color color;
    private boolean outer;
    private ArrayList<ResizeProvider.ControlPoint> points;

    
    /*
     * @param thickness the thickness of the border
     * @param color the border color
     * @param points points which will be filled
     * @param outer if true, then the rectangle encapsulate the squares too; if false, then the rectangle encapsulates the widget client area only
     */
    public ResizeBorder (int thickness, Color color,ResizeProvider.ControlPoint[] points, boolean outer) {
        this.thickness = thickness;
        this.color = color;
        this.outer = outer;
        this.points=new ArrayList<ResizeProvider.ControlPoint>();
        if(points!=null)for(int i=0;i<points.length;i++)this.points.add(points[i]);
    }
    /*
     * @param thickness the thickness of the border
     * @param color the border color
     * @param points points which will be filled
     */
    public ResizeBorder (int thickness, Color color,ResizeProvider.ControlPoint[] points) {
        this(thickness,color,points,false);
    }
    /*
     * @param thickness the thickness of the border
     * @param color the border color
     */
    public ResizeBorder (int thickness, Color color) {
        this(thickness,color,new ResizeProvider.ControlPoint[]{ResizeProvider.ControlPoint.TOP_LEFT,ResizeProvider.ControlPoint.TOP_CENTER,ResizeProvider.ControlPoint.TOP_RIGHT,ResizeProvider.ControlPoint.CENTER_LEFT,ResizeProvider.ControlPoint.BOTTOM_LEFT,ResizeProvider.ControlPoint.BOTTOM_CENTER,ResizeProvider.ControlPoint.BOTTOM_RIGHT,ResizeProvider.ControlPoint.CENTER_RIGHT});
    }
    /*
     * @param thickness the thickness of the border
     */
    public ResizeBorder (int thickness) {
        this(thickness,Color.BLACK);
    }

    public Insets getInsets () {
        return new Insets (thickness, thickness, thickness, thickness);
    }

    public boolean isOuter () {
        return outer;
    }

    public void paint (Graphics2D gr, Rectangle bounds) {
        gr.setColor (color);

        Stroke stroke = gr.getStroke ();
        gr.setStroke (STROKE);
        if (outer)
            gr.draw (new Rectangle2D.Double (bounds.x + 0.5, bounds.y + 0.5, bounds.width - 1.0, bounds.height - 1.0));
        else
            gr.draw (new Rectangle2D.Double (bounds.x + thickness + 0.5, bounds.y + thickness + 0.5, bounds.width - thickness - thickness - 1.0, bounds.height - thickness - thickness - 1.0));
        gr.setStroke (stroke);

        
        //TBD, may it have sense for perfomance to have 8 boolean vars
        if(points.contains(ResizeProvider.ControlPoint.TOP_LEFT))gr.fillRect (bounds.x, bounds.y, thickness, thickness);
        else gr.drawRect (bounds.x, bounds.y, thickness, thickness);
        if(points.contains(ResizeProvider.ControlPoint.TOP_RIGHT))gr.fillRect (bounds.x + bounds.width - thickness, bounds.y, thickness, thickness);
        else gr.drawRect (bounds.x + bounds.width - thickness, bounds.y, thickness, thickness);
        if(points.contains(ResizeProvider.ControlPoint.BOTTOM_LEFT))gr.fillRect (bounds.x, bounds.y + bounds.height - thickness, thickness, thickness);
        else gr.drawRect (bounds.x, bounds.y + bounds.height - thickness, thickness, thickness);
        if(points.contains(ResizeProvider.ControlPoint.BOTTOM_RIGHT))gr.fillRect (bounds.x + bounds.width - thickness, bounds.y + bounds.height - thickness, thickness, thickness);
        else gr.drawRect (bounds.x + bounds.width - thickness, bounds.y + bounds.height - thickness, thickness, thickness);

        Point center = new Point(bounds.x+bounds.width/2,bounds.y+bounds.height/2);
        if (bounds.width >= thickness * 5) {
            if(points.contains(ResizeProvider.ControlPoint.TOP_CENTER))gr.fillRect (center.x - thickness / 2, bounds.y, thickness, thickness);
            else gr.drawRect (center.x - thickness / 2, bounds.y, thickness, thickness);
            if(points.contains(ResizeProvider.ControlPoint.BOTTOM_CENTER))gr.fillRect (center.x - thickness / 2, bounds.y + bounds.height - thickness, thickness, thickness);
            else gr.drawRect (center.x - thickness / 2, bounds.y + bounds.height - thickness, thickness, thickness);
        }
        if (bounds.height >= thickness * 5) {
            if(points.contains(ResizeProvider.ControlPoint.CENTER_LEFT))gr.fillRect (bounds.x, center.y - thickness / 2, thickness, thickness);
            else gr.drawRect (bounds.x, center.y - thickness / 2, thickness, thickness);
            if(points.contains(ResizeProvider.ControlPoint.CENTER_RIGHT))gr.fillRect (bounds.x + bounds.width - thickness, center.y - thickness / 2, thickness, thickness);
            else gr.drawRect (bounds.x + bounds.width - thickness, center.y - thickness / 2, thickness, thickness);
        }
    }

    public boolean isOpaque () {
        return outer;
    }

}
