//package org.uml.visual.palette.unused;
//
//import org.openide.nodes.Children;
//import org.openide.nodes.Node;
//import org.uml.visual.palette.PaletteCategory;
//import org.uml.visual.palette.PaletteCategoryNode;
//
///**
// *
// * @author NUGS
// */
//public class PaletteCategoryChildren extends Children.Keys<PaletteCategory> {
//
//    private static final String[] CATEGORIES = new String[]{ "Components", "Relations" };
//
//    public PaletteCategoryChildren() {
//    }
//
//    @Override
//    protected Node[] createNodes(PaletteCategory key) {
//        PaletteCategory cat = key;
//        return new Node[]{ new PaletteCategoryNode(cat) };
//    }
//
//    @Override
//    protected void addNotify() {
//        super.addNotify();
//        PaletteCategory[] cats = new PaletteCategory[CATEGORIES.length];
//        for (int i = 0; i < cats.length; i++) {
//            PaletteCategory cat = new PaletteCategory();
//            cat.setName(CATEGORIES[i]);
//            cats[i] = cat;
//        }
//        setKeys(cats);
//    }
//}
