/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import java.util.ArrayList;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;

/**
 *
 * @author Jelena
 */
public class ClassDiagramComponentChildren  extends Index.ArrayChildren {
    
    private Category category;
    
    private String[][] items = new String[][]{
        {"0", "Classes", "Klasa"},
        {"1", "Interfaces", "Interfejs"},
        {"2", "Enumerations", "Enumeracija"},
        {"3", "Interfaces", "Interfejs1"},
        {"4", "Enumerations", "Enumeracija1"},
        {"5", "Classes", "Klasa1"},
    };
    
    public ClassDiagramComponentChildren(Category Category) {
        this.category = Category;
    }
    
    @Override
    protected java.util.List<Node> initCollection() {
        ArrayList childrenNodes = new ArrayList( items.length );
        for( int i=0; i<items.length; i++ ) {
            if( category.getName().equals( items[i][1] ) ) {
                ClassDiagramComponent item = new ClassComponent();
                item.setName(items[i][2]);
                childrenNodes.add( new ClassDiagramComponentNode( item ) );
            }
        }
        return childrenNodes;
    }
}
