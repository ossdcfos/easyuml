package org.uml.visual.widgets.unused;

import java.awt.*;
import java.awt.geom.GeneralPath;
import org.netbeans.api.visual.anchor.AnchorShape;

/**
 *
 * @author Zoran
 *
 * http://bits.netbeans.org/dev/javadoc/org-netbeans-api-visual/org/netbeans/api/visual/anchor/package-summary.html
 * @see http://grepcode.com/file/repo1.maven.org/maven2/com.googlecode.sarasvati.thirdparty.netbeans/visual/7.3/org/netbeans/modules/visual/anchor/ArrowAnchorShape.java#ArrowAnchorShape
 * @see http://www.javaprogrammingforums.com/object-oriented-programming/10461-java-graphics-drawing-one-object-into-another.html
 */
public class CardinalityAnchor implements AnchorShape {

    private static final Stroke STROKE = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private GeneralPath path;
    private int size=10;
    int degrees;
    
    String cardinality;
    
    // http://www.dickbaldwin.com/java/Java310-fig17.htm  kako nacrtati romb
    // http://www.ehow.com/how_12165313_draw-diamond-java.html

    public CardinalityAnchor(String cardinality) {
        path = new GeneralPath();
        this.cardinality = cardinality;
      //  double sin = -size * Math.sqrt(1 - cos * cos);
      //  cos *= size;

        // strelica
//        path.moveTo(0.0f, 0.0f);
//        path.lineTo((float) cos, (float) -sin);
//        path.moveTo(0.0f, 0.0f);
//        path.lineTo((float) cos, (float) sin);
        

//        path.moveTo(0.5f*ds,0.0f*ds);
//        path.lineTo(0.0f*ds,0.5f*ds);
//        path.lineTo(-0.5f*ds,0.0f*ds);
//        path.lineTo(0.0f*ds,-0.5f*ds);        
       
        // path create romb
        GeneralPath thePath = new GeneralPath();
        float x1 = size, y1=0;
        //double x1 = size*sin, y1=size*cos;
        path.moveTo(x1, y1);
        float x2 = size * 0.5f, y2 = size*0.5f;
        path.lineTo(x2, y2);
        float x3 = 0.0f, y3=0.0f;
        path.lineTo(x3, y3);
        float x4 = size*0.5f , y4=-size*0.5f;
        path.lineTo(0.5*size,-size*0.5);
        
        path.closePath();        
        
    }

    @Override
    public boolean isLineOriented() {
        return false;
    }

    @Override
    public int getRadius() {
        return size+1;
    }

    @Override
    public double getCutDistance() {
        return 5;
    }

    @Override
    public void paint(Graphics2D graphics, boolean source) {
        graphics.drawString(cardinality, 10, -10);

    }
}
