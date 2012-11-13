/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import org.netbeans.api.visual.action.AcceptProvider;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.ConnectorState;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.UmlClassDiagram;
import org.uml.visual.palette.PaletteItemNode;
import org.uml.visual.widgets.actions.SceneAcceptProvider;

/**
 *
 * @author NUGS
 */
public class ClassDiagramScene extends GraphScene<PaletteItemNode, String> {

    //private LayerWidget mainLayer;
    private UmlClassDiagram umlClassDiagram;
    private ClassDiagramContainerWidget classDiagramWidget;

    public ClassDiagramScene(UmlClassDiagram umlClassDiagram) {

        this.umlClassDiagram = umlClassDiagram;
        classDiagramWidget = new ClassDiagramContainerWidget(umlClassDiagram, this);
        classDiagramWidget.setPreferredLocation(new Point(100, 10));
        //mainLayer = new LayerWidget(this);
        //mainLayer.addChild(classDiagramWidget);               // Za Kasnije
        //addChild(mainLayer);
        addChild(classDiagramWidget);
        //addObject(umlClassDiagram, classDiagramWidget);       // Za kasnije
        getActions().addAction(ActionFactory.createPanAction());
        getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.1));
        getActions().addAction(ActionFactory.createAcceptAction(new SceneAcceptProvider(this)));
        getActions().addAction(ActionFactory.createMoveAction(ActionFactory.createSnapToGridMoveStrategy(16, 16), null));
        //getActions().addAction(ActionFactory.createPopupMenuAction(new MainPopupMenuProvider()));

        //TODO Prebaciti ovo u neki SceneAcceptProvider 
        /* Napravljen SceneAcceptProvider
         getActions().addAction(ActionFactory.createAcceptAction(new AcceptProvider() {
         @Override
         public ConnectorState isAcceptable(Widget widget, Point point, Transferable t) {
         return ConnectorState.ACCEPT;
         }

         @Override
         public void accept(Widget widget, Point point, Transferable t) {
                
         }
         }));*/

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

    @Override
    protected Widget attachNodeWidget(PaletteItemNode n) {
        return null;
    }

    @Override
    protected Widget attachEdgeWidget(String e) {
        return null;
    }

    @Override
    protected void attachEdgeSourceAnchor(String e, PaletteItemNode n, PaletteItemNode n1) {
    }

    @Override
    protected void attachEdgeTargetAnchor(String e, PaletteItemNode n, PaletteItemNode n1) {
    }
}
