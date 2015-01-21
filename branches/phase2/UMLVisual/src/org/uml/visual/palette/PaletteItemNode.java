package org.uml.visual.palette;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author NUGS
 */
public class PaletteItemNode extends AbstractNode{
    
    private final PaletteItem paletteItem;

    public PaletteItemNode(PaletteItem key) {
        super(Children.LEAF, Lookups.singleton(key));
        this.paletteItem = key;
        setIconBaseWithExtension(key.getIcon());
        setDisplayName(key.getTitle());
        setShortDescription(key.getShortDescription());
    }

    public PaletteItem getPaletteItem() {
        return paletteItem;
    }
}
