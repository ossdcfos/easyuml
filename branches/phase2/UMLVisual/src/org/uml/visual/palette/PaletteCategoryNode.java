/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.palette;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;

/**
 *
 * @author NUGS
 */
public class PaletteCategoryNode extends AbstractNode {

    public PaletteCategoryNode(PaletteCategory category) {
        super(Children.create(new PaletteItemChildFactory(category), true));
        //super(new PaletteItemChildren(category));
        setDisplayName(category.getName());
    }
}
