/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.palette;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author Uros
 */
public class PaletteItemNode extends AbstractNode{
    
    private PaletteItem paletteItem;

    public PaletteItemNode(PaletteItem key) {
        super(Children.LEAF, Lookups.singleton(key));
        this.paletteItem = key;
        setIconBaseWithExtension(key.getIcon());
        setDisplayName(key.getTitle());
    }

    public PaletteItem getPaletteItem() {
        return paletteItem;
    }
    
    
}
