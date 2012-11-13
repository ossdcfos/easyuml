package org.uml.visual.palette;

import org.openide.nodes.Children;
import org.openide.nodes.Node;

/**
 *
 * @author NUGS
 */
public class PaletteCategoryChildren extends Children.Keys{
    
        private String[] categories= new String[] {"Shapes"};

    public PaletteCategoryChildren() {
    }
        
    @Override
    protected Node[] createNodes (Object key) {
        
        PaletteCategory obj= (PaletteCategory) key;
        return new Node[] { new PaletteCategoryNode(obj)};
    }
    
    @Override
    protected void addNotify () {
        super.addNotify();
        PaletteCategory[] objs= new PaletteCategory[categories.length];
        for (int i = 0; i < objs.length; i++){
            PaletteCategory cat= new PaletteCategory();
            cat.setName(categories[i]);
            objs[i]=cat;
                }
        setKeys(objs);
        }
}
