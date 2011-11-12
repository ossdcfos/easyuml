  /*
 * ShapeChildren.java
 *
 * Created on September 21, 2006, 9:10 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 *
 * To understand this class, see http://platform.netbeans.org/tutorials/nbm-nodesapi3.html
 */


package org.netbeans.shapesample.palette;

import java.util.ArrayList;
import org.openide.nodes.Index;
import org.openide.nodes.Node;

/**
 *
 * @author Geertjan Wielenga
 */
public class ShapeChildren  extends Index.ArrayChildren {

    private Category category;

    private String[][] items = new String[][]{
        {"0", "Shapes", "org/netbeans/shapesample/palette/class.gif"},
//        {"1", "Shapes", "org/netbeans/shapesample/palette/methodPublic.gif"},
//        {"2", "Shapes", "org/netbeans/shapesample/palette/variablePublic.gif"},
    };

    public ShapeChildren(Category Category) {
        this.category = Category;
    }

    protected java.util.List initCollection() {
        ArrayList childrenNodes = new ArrayList( items.length );
        for( int i=0; i<items.length; i++ ) {
            if( category.getName().equals( items[i][1] ) ) {
                Shape item = new Shape();
                item.setNumber(new Integer(items[i][0]));
                item.setCategory(items[i][1]);
                item.setImage(items[i][2]);
                childrenNodes.add( new ShapeNode( item ) );
            }
        }
        return childrenNodes;
    }

}