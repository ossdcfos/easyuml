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
import org.uml.model.RelationComponent;
import org.uml.model.UmlClassDiagram;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;
import org.uml.visual.widgets.actions.SceneAcceptProvider;

/**
 *
 * Shapes-Objects-Objekti Class Abstract Class Interface Enum
 * Connections-Relationships-Veze Inheritance Associattion (Use. Has)
 *
 * Other-Drugi alati Comment
 *
 * @author NUGS
 */                                             /* ClassDiagramComponent, ClassDiagramRelation*/
// ClassDiagramComponent  - Class, Interface, 
public class ClassDiagramScene extends GraphScene<ClassDiagramComponent, RelationComponent> {

    private LayerWidget mainLayer;
    private UmlClassDiagram umlClassDiagram;

    public ClassDiagramScene(UmlClassDiagram umlClassDiagram) {

        this.umlClassDiagram = umlClassDiagram;
        //classDiagramWidget = new ClassDiagramContainerWidget(umlClassDiagram, this);
        //classDiagramWidget.setPreferredLocation(new Point(100, 10));
        mainLayer = new LayerWidget(this);
        //mainLayer.addChild(classDiagramWidget);               // Za Kasnije
        addChild(mainLayer);
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
    protected Widget attachNodeWidget(ClassDiagramComponent n) {
        ClassWidget widget = new ClassWidget(this, (ClassComponent)n);
              widget.getActions().addAction(ActionFactory.createMoveAction());
        //single-click, the event is not consumed:
        widget.getActions().addAction(createSelectAction());

        //mouse-dragged, the event is consumed while mouse is dragged:
        widget.getActions().addAction(ActionFactory.createMoveAction());

        //mouse-over, the event is consumed while the mouse is over the widget:
        widget.getActions().addAction(createObjectHoverAction());
        mainLayer.addChild(widget);
        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(RelationComponent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeSourceAnchor(RelationComponent e, ClassDiagramComponent n, ClassDiagramComponent n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    protected void attachEdgeTargetAnchor(RelationComponent e, ClassDiagramComponent n, ClassDiagramComponent n1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
