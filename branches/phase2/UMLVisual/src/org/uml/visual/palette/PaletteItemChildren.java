package org.uml.visual.palette;

import java.util.ArrayList;
import org.openide.nodes.Index;
import org.openide.nodes.Node;

/**
 *
 * @author NUGS
 */
public class PaletteItemChildren extends Index.ArrayChildren {

    private PaletteCategory category;
    //TODO Zameniti ostale Pallete iteme sa odgovarajucim widgetima kada se naprave !!!
    private String[][] items = new String[][]{
        {"Class", "Components", "org/uml/visual/icons/class.gif", "org.uml.model.ClassComponent", "org.uml.visual.widgets.ClassWidget"},
        {"Interface", "Components", "org/uml/visual/icons/class.gif", "org.uml.model.InterfaceComponent", "org.uml.visual.widgets.InterfaceWidget"},
        {"Enum", "Components", "org/uml/visual/icons/class.gif", "org.uml.model.EnumComponent", "org.uml.visual.widgets.EnumWidget"},
//        {"Package", "Components", "org/uml/visual/icons/package.gif", "org.uml.model.PackageComponent", "org.uml.visual.widgets.PackageWidget"},
        {"Is", "Relations", "org/uml/visual/icons/is.gif", "org.uml.model.IsRelationComponent", "org.uml.visual.widgets.RelationWidgetBase"},
        {"Implements", "Relations", "org/uml/visual/icons/implements.gif", "org.uml.model.ImplementsRelationComponent", "org.uml.visual.widgets.RelationWidgetBase"}, 
        {"Has", "Relations", "org/uml/visual/icons/has.gif", "org.uml.model.HasRelationComponent", "org.uml.visual.widgets.RelationWidgetBase"},
        {"Use", "Relations", "org/uml/visual/icons/use.gif", "org.uml.model.UseRelationComponent", "org.uml.visual.widgets.RelationWidgetBase"},

    //        {"Notes", "Other", "org/uml/visual/icons/notes.gif", "org.uml.model.UmlClassElement"}, 
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
                    item.setTargetWidget(Class.forName(items[i][4]));
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace();
                }
                childrenNodes.add(new PaletteItemNode(item));

            }
        }
        return childrenNodes;
    }
}
