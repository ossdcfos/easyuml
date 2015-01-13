package org.uml.visual.palette;

import java.util.LinkedList;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

/**
 *
 * @author Boris
 */
class PaletteItemChildFactory extends ChildFactory<PaletteItem> {

    private final String[][] ITEMS = new String[][]{
        {"Class", "Components", "org/uml/visual/icons/class.png", "org.uml.model.components.ClassComponent"},
        {"Interface", "Components", "org/uml/visual/icons/interface.png", "org.uml.model.components.InterfaceComponent"},
        {"Enum", "Components", "org/uml/visual/icons/enum.png", "org.uml.model.components.EnumComponent"},
        
        {"Is", "Relations", "org/uml/visual/icons/is.png", "org.uml.model.relations.IsRelation"},
        {"Implements", "Relations", "org/uml/visual/icons/implements.png", "org.uml.model.relations.ImplementsRelation"},
        {"Has", "Relations", "org/uml/visual/icons/has.png", "org.uml.model.relations.HasRelation"},
        {"Has (agg)", "Relations", "org/uml/visual/icons/has_agg.png", "org.uml.model.relations.AggregationRelation"},
        {"Has (comp)", "Relations", "org/uml/visual/icons/has_comp.png", "org.uml.model.relations.CompositionRelation"},
        {"Use", "Relations", "org/uml/visual/icons/use.png", "org.uml.model.relations.UseRelation"}
    };

    private final PaletteCategory category;

    public PaletteItemChildFactory(PaletteCategory category) {
        this.category = category;
    }

    @Override
    protected boolean createKeys(List<PaletteItem> toPopulate) {
        LinkedList<PaletteItem> items = new LinkedList<>();
        for (String[] itemString : ITEMS) {
            if(category.getName().equals(itemString[1])) {
                PaletteItem item = new PaletteItem();
                item.setTitle(itemString[0]);
                item.setCategory(itemString[1]);
                item.setIcon(itemString[2]);
                try {
                    item.setDropClass(Class.forName(itemString[3]));
                } catch (ClassNotFoundException ex) {
                    ex.printStackTrace(System.out);
                }
                items.add(item);
            }
        }
        toPopulate.addAll(items);
        return true;
    }

    @Override
    protected Node createNodeForKey(PaletteItem key) {
        PaletteItemNode pin = new PaletteItemNode(key);
        return pin;
    }
}
