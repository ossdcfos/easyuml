/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.Image;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;
import org.uml.model.UmlClassDiagram;
import org.uml.model.UmlClassElement;

/**
 *
 * @author Uros
 */
public class ClassDiagramScene extends ObjectScene {

    private LayerWidget mainLayer;
    private UmlClassDiagram umlClassDiagram;
    private ClassDiagramWidget classDiagramWidget;

    public ClassDiagramScene(UmlClassDiagram umlClassDiagram) {

        this.umlClassDiagram = umlClassDiagram;
        classDiagramWidget = new ClassDiagramWidget(umlClassDiagram, this);
        classDiagramWidget.setPreferredLocation(new Point(100, 10));
        mainLayer = new LayerWidget(this);
        //mainLayer.addChild(classDiagramWidget);               // Za Kasnije
        addChild(mainLayer);
        //addObject(umlClassDiagram, classDiagramWidget);       // Za kasnije
//        mainLayer = new LayerWidget(this);
//        addChild(mainLayer);

        getActions().addAction(ActionFactory.createPanAction());
        getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.1));
        //getActions().addAction(ActionFactory.createPopupMenuAction(new MainPopupMenuProvider()));

        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {
            @Override
            public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
                return ConnectorState.ACCEPT;
            }

            @Override
            public void accept(Widget widget, Point point, Transferable t) {
                DataFlavor flavor = t.getTransferDataFlavors()[2];
                Class droppedClass = flavor.getRepresentationClass();
                try {
                    ClassWidget classWidget = new ClassWidget((ClassDiagramScene) getScene(), (UmlClassElement) droppedClass.newInstance());
                    mainLayer.addChild(classWidget);
                    
                } catch (Exception e) {
                }
            }
        }));
//        getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {
//            @Override
//            public ConnectorState isAcceptable(final Widget widget, final Point point, final Transferable t) {
//
//                SwingUtilities.invokeLater(new Runnable() {
//                    @Override
//                    public void run() {
//                        Image dragImage = getImageFromTransferable(t);
//                        JComponent view = widget.getScene().getView();
//
//
//                        Graphics2D graphics = widget.getScene().getGraphics();
//                        Rectangle visRect = view.getVisibleRect();
//                        view.paintImmediately(visRect.x, visRect.y, visRect.width, visRect.height);
//
//                        graphics.drawImage(dragImage,
//                                AffineTransform.getTranslateInstance(point.getLocation().getX(),
//                                point.getLocation().getY()),
//                                null);
//                    }
//                });
//                return ConnectorState.ACCEPT;
//            }
//
//            @Override
//            public void accept(Widget widget, Point point, Transferable t) {
//                Object transfesData = t.getTransferDataFlavors();
//                DataFlavor flavor = t.getTransferDataFlavors()[2];
//                Class droppedClass = flavor.getRepresentationClass();
//                try {
//                    ClassWidget classO = new ClassWidget(ClassDiagramScene.this, (UmlClassElement) droppedClass.newInstance());
//                    classO.setPreferredLocation(widget.convertLocalToScene(point));
//                    // mainLayer.addChild(classO); 
//                    ClassDiagramScene.this.addChild(classO);
//                    revalidate();
//                    // repaint();
//                } catch (InstantiationException ex) {
//                    Exceptions.printStackTrace(ex);
//                } catch (IllegalAccessException ex) {
//                    Exceptions.printStackTrace(ex);
//                }
//            }
//        }));
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
        return o instanceof Image ? (Image) o : ImageUtilities.loadImage("org/uml/visual/icons/class.gif");
    }

    public UmlClassDiagram getUmlClassDiagram() {
        return umlClassDiagram;
    }
}
