package org.uml.visual.widgets.anchors;

import java.awt.*;
import java.awt.geom.GeneralPath;
import org.netbeans.api.visual.anchor.AnchorShape;

/**
 *
 * @author Boris Perović
 */
public class TriangleEquilateralAnchorShape implements AnchorShape {

    private static final Stroke STROKE = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private GeneralPath path;
    private double side;
    private double altitude;
    private boolean filled;

    // http://www.dickbaldwin.com/java/Java310-fig17.htm  kako nacrtati romb
    // http://www.ehow.com/how_12165313_draw-diamond-java.html
    public TriangleEquilateralAnchorShape(int altitude, boolean filled) {
        this.filled = filled;

        path = new GeneralPath();
        this.filled = filled;
        this.altitude = altitude;
        this.side = altitude * 2 / Math.sqrt(3);

        path.moveTo(0.0f, 0.0f);
        path.lineTo(altitude, -side / 2);
        path.lineTo(altitude, +side / 2);
        path.lineTo(0.0f, 0.0f);
        path.closePath();
    }

    @Override
    public boolean isLineOriented() {
        return true;
    }

    @Override
    public int getRadius() {
        return (int)side + 1;
    }

    @Override
    public double getCutDistance() {
        return altitude;
    }

    @Override
    public void paint(Graphics2D graphics, boolean source) {
        Stroke previousStroke = graphics.getStroke();
        graphics.setStroke(STROKE);
        graphics.draw(path);
        graphics.setStroke(previousStroke);
        if (filled) graphics.fill(path);
    }
}
