/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.palette;

import java.awt.Image;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.beans.BeanInfo;
import java.io.IOException;
import org.netbeans.spi.palette.DragAndDropHandler;
import org.openide.nodes.Node;
import org.openide.util.Lookup;
import org.openide.util.datatransfer.ExTransferable;

/**
 *
 * @author NUGS
 */
public class PaletteDnDHandler extends DragAndDropHandler{
        
    @Override
    public void customize (ExTransferable et, Lookup lkp) {
        //Makes lookup on node
        Node node=lkp.lookup(Node.class);
        //gets paletteItem when node is PaletteItem.class
        PaletteItem paletteItem=node.getLookup().lookup(PaletteItem.class);
        //gets the Class of paletteItem, ex. Class,Method,Field...
        final Class dropClass= paletteItem.getDropClass();
        
        final Image image = (Image) node.getIcon(BeanInfo.ICON_COLOR_16x16);
        et.put(new ExTransferable.Single(DataFlavor.imageFlavor) {

            @Override
            protected Object getData() throws IOException, UnsupportedFlavorException {
                return image;
            }
        });
        et.put(new ExTransferable.Single(new DataFlavor(dropClass,dropClass.getSimpleName())) {
    
        @Override
        protected Object getData() throws IOException, UnsupportedFlavorException {
        return dropClass;
        }
    });
    }
}
