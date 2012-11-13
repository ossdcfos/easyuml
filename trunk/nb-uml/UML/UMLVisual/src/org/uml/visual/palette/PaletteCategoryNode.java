/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.palette;

import org.openide.nodes.AbstractNode;
import org.openide.util.lookup.Lookups;

/**
 *
 * @author NUGS
 */
public class PaletteCategoryNode extends AbstractNode{

    public PaletteCategoryNode(PaletteCategory category) {
        super( new PaletteItemChildren(category), Lookups.singleton(category));
        setDisplayName(category.getName());
        
    }
    
    
}
