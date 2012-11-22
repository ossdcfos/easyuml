package org.uml.visual.widgets.actions;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import javax.swing.JComponent;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.uml.model.ClassComponent;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author Uros
 */
public class SceneAcceptProvider implements AcceptProvider{
    
    private ClassDiagramScene classDiagramScene;

    public SceneAcceptProvider(ClassDiagramScene classDiagramScene) {
   
        this.classDiagramScene= classDiagramScene;
    }
    

    @Override
    public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
         Image dragImage = getImageFromTransferable(t);
                JComponent view = classDiagramScene.getView();
                Graphics2D g2 = (Graphics2D) view.getGraphics();
                Rectangle visRect = view.getVisibleRect();
                view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
                g2.drawImage(dragImage,
                        AffineTransform.getTranslateInstance(point.getLocation().getX(),
                        point.getLocation().getY()),
                        null);
                return ConnectorState.ACCEPT;
    }

    @Override
    public void accept(Widget widget, Point point, Transferable t) {
           Class<? extends ClassComponent> droppedClass = (Class<? extends ClassComponent>) t.getTransferDataFlavors()[2].getRepresentationClass(); // Jako ruzno! Osmisliti kako da izvlacimo iz DataFlavor-a bez gadjanja indeksa!
           try {
                    Image dragImage = getImageFromTransferable(t);
                    ClassComponent umlClass= (ClassComponent) droppedClass.newInstance();
                    umlClass.setImage(dragImage);
                    Widget w=classDiagramScene.addNode((ClassComponent) droppedClass.newInstance());
                    w.setPreferredLocation(widget.convertLocalToScene(point));
                    //addChild(new ClassWidget((ClassDiagramScene) getScene(), (UmlClassElement) droppedClass.newInstance()));
                } catch (InstantiationException ex) {
                    Exceptions.printStackTrace(ex);
                } catch (IllegalAccessException ex) {
                    Exceptions.printStackTrace(ex);
                }
                
                
    }
    
    private Image getImageFromTransferable(Transferable transferable) {
        Object o = null;
        try {
            o = transferable.getTransferData(DataFlavor.imageFlavor);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (UnsupportedFlavorException ex) {
            ex.printStackTrace();
        }
        return o instanceof Image ? (Image) o : ImageUtilities.loadImage("org/netbeans/shapesample/palette/shape1.png");
    }
    
    
}
