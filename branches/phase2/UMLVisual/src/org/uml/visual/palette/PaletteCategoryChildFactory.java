/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.palette;

import java.beans.IntrospectionException;
import java.util.LinkedList;
import java.util.List;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;

/**
 *
 * @author Boris
 */
class PaletteCategoryChildFactory extends ChildFactory<PaletteCategory> {

    private static final String[] CATEGORIES = new String[]{"Components", "Relations"};

    public PaletteCategoryChildFactory() {
    }

    @Override
    protected boolean createKeys(List<PaletteCategory> toPopulate) {
        LinkedList<PaletteCategory> cats = new LinkedList<>();
        for (String catName : CATEGORIES) {
            PaletteCategory cat = new PaletteCategory();
            cat.setName(catName);
            cats.add(cat);
        }
        toPopulate.addAll(cats);
        return true;
    }

    @Override
    protected Node createNodeForKey(PaletteCategory key) {
        PaletteCategoryNode pcn = null;
        pcn = new PaletteCategoryNode(key);
        return pcn;
    }
}
