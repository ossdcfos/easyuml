/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.palette;

import java.util.ArrayList;
import org.openide.nodes.Index;
import org.openide.nodes.Node;

/**
 *
 * @author Uros
 */
public class PaletteItemChildren extends Index.ArrayChildren {

    //TODO Ostavi samo klasu, dodaj interfejs, apstraktnu klasu, i nemoj da koristis "Shapes" vec jbm li ga elements tako nesto smisli da kapiram
    //e a posto ne dobijam kao argument classDiagram imam posle problem sad cu da ti pokazem
    private PaletteCategory category;
    private String[][] items = new String[][]{
        {"Class", "Shapes", "org/uml/visual/palette/class.gif", "org.uml.model.UmlClassElement"}, 
        //        {"Method","Shapes","org/uml/visual/palette/methodPublic.gif","org.uml.model.Method"},
    //        {"Variable","Shapes","org/uml/visual/palette/variablePublic.gif","org.uml.model.Field"},
    };

    public PaletteItemChildren(PaletteCategory category) {
        this.category = category;
    }

    @Override
    protected java.util.List<Node> initCollection() {
        ArrayList childrenNodes = new ArrayList(items.length);
        for (int i = 0; i < items.length; i++) {
            //sta ako je ovo nova kategorija?
            if (category.getName().equals(items[i][1])) {
                PaletteItem item = new PaletteItem();
                item.setTitle(items[i][0]);
                item.setCategory(items[i][1]);
                //Proveri da li je bolje na Neuroph-u ili kako je na uml radjeno
                item.setIcon(items[i][2]);
                try {
                    item.setDropClass(Class.forName(items[i][3]));
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                childrenNodes.add(new PaletteItemNode(item));

            }
        }
        return childrenNodes;
    }
}
