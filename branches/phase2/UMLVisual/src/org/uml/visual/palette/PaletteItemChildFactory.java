/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
        {"Class", "Components", "org/uml/visual/icons/class.png", "org.uml.model.components.ClassComponent", "org.uml.visual.widgets.components.ClassWidget"},
        {"Interface", "Components", "org/uml/visual/icons/interface.png", "org.uml.model.components.InterfaceComponent", "org.uml.visual.widgets.components.InterfaceWidget"},
        {"Enum", "Components", "org/uml/visual/icons/enum.png", "org.uml.model.components.EnumComponent", "org.uml.visual.widgets.components.EnumWidget"},
        
        {"Is", "Relations", "org/uml/visual/icons/is.png", "org.uml.model.relations.IsRelation", "org.uml.visual.widgets.RelationWidgetBase"},
        {"Implements", "Relations", "org/uml/visual/icons/implements.png", "org.uml.model.relations.ImplementsRelation", "org.uml.visual.widgets.RelationWidgetBase"},
        {"Has (agg)", "Relations", "org/uml/visual/icons/has_agg.png", "org.uml.model.relations.AggregationRelation", "org.uml.visual.widgets.RelationWidgetBase"},
        {"Has (comp)", "Relations", "org/uml/visual/icons/has_comp.png", "org.uml.model.relations.CompositionRelation", "org.uml.visual.widgets.RelationWidgetBase"},
        {"Use", "Relations", "org/uml/visual/icons/use.png", "org.uml.model.relations.UseRelation", "org.uml.visual.widgets.RelationWidgetBase"}
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
                    //item.setTargetWidget(Class.forName(itemString[4]));
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
