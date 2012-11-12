/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.util.logging.Logger;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.model.ObjectScene;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
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

        getActions().addAction(ActionFactory.createPanAction());
        getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.1));
        //getActions().addAction(ActionFactory.createPopupMenuAction(new MainPopupMenuProvider()));

        //TODO Prebaciti ovo u neki SceneAcceptProvider 
        
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
    }

    //TODO Osmisliti preko graphics-a iscrtavanje prilikom dragovanja 
    
//    private Image getImageFromTransferable(Transferable transferable) {
//        Object o = null;
//        try {
//            o = transferable.getTransferData(DataFlavor.imageFlavor);
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        } catch (UnsupportedFlavorException ex) {
//            ex.printStackTrace();
//        }
//        return o instanceof Image ? (Image) o : ImageUtilities.loadImage("org/uml/visual/icons/class.gif");
//    }

    public UmlClassDiagram getUmlClassDiagram() {
        return umlClassDiagram;
    }
}
