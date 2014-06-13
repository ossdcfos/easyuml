package org.uml.visual.widgets;

import java.awt.*;
import java.awt.geom.GeneralPath;
import org.netbeans.api.visual.anchor.AnchorShape;

/**
 *
 * @author Ilija
 * @see http://grepcode.com/file/repo1.maven.org/maven2/com.googlecode.sarasvati.thirdparty.netbeans/visual/7.3/org/netbeans/modules/visual/anchor/ArrowAnchorShape.java#ArrowAnchorShape
 * @see http://www.javaprogrammingforums.com/object-oriented-programming/10461-java-graphics-drawing-one-object-into-another.html
 */
public class RhombusAnchorShape implements AnchorShape {

    private static final Stroke STROKE = new BasicStroke(1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private GeneralPath path;
    private int size;
    int degrees;
    
    // http://www.dickbaldwin.com/java/Java310-fig17.htm  kako nacrtati romb
    // http://www.ehow.com/how_12165313_draw-diamond-java.html

    public RhombusAnchorShape(int degrees, int size) {
        this.size = size;
        this.degrees=degrees;
        path = new GeneralPath();
        double radians = Math.PI * degrees / 180.0;
        double cos = Math.cos(radians / 2.0);
        double sin = Math.sin(radians / 2.0);
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
        return true;
    }

    @Override
    public int getRadius() {
        return size+1;
    }

    @Override
    public double getCutDistance() {
        return 0;
    }

    @Override
    public void paint(Graphics2D graphics, boolean source) {
        Stroke previousStroke = graphics.getStroke();
        graphics.setStroke(STROKE);
        graphics.draw(path);
        graphics.setStroke(previousStroke);

        graphics.setColor(Color.BLACK);
        graphics.fill(path);
        
        graphics.rotate(Math.toRadians(degrees));
        
//        super.paintComponent(graphics);


        // ilija
//        Graphics2D g2d = (Graphics2D) graphics;
//
////        g2d.translate(275, 75);
//        g2d.rotate(Math.toRadians(45));
//
//        Rectangle r = new Rectangle(0, 0, size, size);
//        g2d.fillRect(0, 0, size, size);
//        graphics.setColor(Color.BLACK);
//        g2d.draw(r);
    }
}
