package org.uml.visual.palette;

import java.util.LinkedList;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;

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
        PaletteCategoryNode pcn = new PaletteCategoryNode(key);
        return pcn;
    }
}
