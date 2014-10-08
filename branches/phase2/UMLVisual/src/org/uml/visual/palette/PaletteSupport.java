/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.palette;

import javax.swing.Action;
import org.netbeans.spi.palette.PaletteActions;
import org.netbeans.spi.palette.PaletteController;
import org.netbeans.spi.palette.PaletteFactory;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.Children;
import org.openide.util.Lookup;

/**
 *
 * @author NUGS
 */
public class PaletteSupport {
    public static PaletteController pc = null;

    public static PaletteController getPalette() {
        if(pc == null){
            AbstractNode paletteRoot = new AbstractNode(Children.create(new PaletteCategoryChildFactory(), true));
            paletteRoot.setName("Palette Root");
            pc = PaletteFactory.createPalette(paletteRoot, new EmptyPaletteActions(), null, new PaletteDnDHandler());
            //pc = PaletteFactory.createPalette(paletteRoot, new EmptyPaletteActions(), null, null/* new PaletteDnDHandler()*/);
        }
        return pc;
    }

    private static class EmptyPaletteActions extends PaletteActions {
        @Override
        public Action[] getImportActions() {return null;}
        @Override
        public Action[] getCustomPaletteActions() {return null;}
        @Override
        public Action[] getCustomCategoryActions(Lookup lookup) {return null;}
        @Override
        public Action[] getCustomItemActions(Lookup lookup) {return null;}
        @Override
        public Action getPreferredAction(Lookup lookup) {return null;}
    }
}