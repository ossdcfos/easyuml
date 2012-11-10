/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

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
import javax.swing.SwingUtilities;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Exceptions;
import org.openide.util.ImageUtilities;
import org.uml.model.UmlClassDiagram;
import org.uml.model.UmlClassElement;

/**
 *
 * @author Uros
 */
public class ClassDiagramScene extends ObjectScene{
    
    private LayerWidget mainLayer;
    private LayerWidget interractionLayer;
    private LayerWidget connectionLayer;
    private UmlClassDiagram umlClassDiagram;

    public UmlClassDiagram getUmlClassDiagram() {
        return umlClassDiagram;
    }
    private ClassDiagramWidget classDiagramWidget;

    public ClassDiagramScene(UmlClassDiagram umlClassDiagram) {
        
        this.umlClassDiagram= umlClassDiagram;
        classDiagramWidget= new ClassDiagramWidget(umlClassDiagram, this);
        classDiagramWidget.setPreferredLocation(new Point(100,10));
        
        connectionLayer = new LayerWidget(this);    // draw connections
        interractionLayer = new LayerWidget(this); // draw connections while creating them
        mainLayer= new LayerWidget(this);
        
        mainLayer.addChild(classDiagramWidget);
        
        addChild(mainLayer);
        addChild(connectionLayer);
        addChild(interractionLayer);
        
        addObject(umlClassDiagram, classDiagramWidget);
        
        getActions().addAction(ActionFactory.createPanAction());
        getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.1));
        //getActions().addAction(ActionFactory.createPopupMenuAction(new MainPopupMenuProvider()));
        
   getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {
       @Override
            public ConnectorState isAcceptable(final Widget widget, final Point point, final Transferable t) {

                SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        Image dragImage = getImageFromTransferable(t);
                        JComponent view = widget.getScene().getView();

                 
                        Graphics2D graphics = widget.getScene().getGraphics();
                        Rectangle visRect = view.getVisibleRect();
                        view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);

                        graphics.drawImage(dragImage,
                                AffineTransform.getTranslateInstance(point.getLocation().getX(),
                                point.getLocation().getY()),
                                null);
                    }
                });


                return ConnectorState.REJECT;
            }

       @Override
            public void accept(Widget widget, Point point, Transferable t) {
           try {
                        
                    String className = (String)t.getTransferData(DataFlavor.stringFlavor);
                    try {                   
                         Class<? extends UmlClassElement> forName= (Class<? extends UmlClassElement>) Class.forName(className);
                   try {
                       ClassWidget classO= new ClassWidget(ClassDiagramScene.this,(UmlClassElement)forName.newInstance());
                       addChild(classO);
                       classO.setPreferredLocation(widget.convertLocalToScene(point));
                   } catch (InstantiationException ex) {
                       Exceptions.printStackTrace(ex);
                   } catch (IllegalAccessException ex) {
                       Exceptions.printStackTrace(ex);
                   }
                   } catch (ClassNotFoundException ex) {
                        Exceptions.printStackTrace(ex);
                    }
           } catch (UnsupportedFlavorException ex) {
               Exceptions.printStackTrace(ex);
           } catch (IOException ex) {
               Exceptions.printStackTrace(ex);
           }
       }
        }));
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
