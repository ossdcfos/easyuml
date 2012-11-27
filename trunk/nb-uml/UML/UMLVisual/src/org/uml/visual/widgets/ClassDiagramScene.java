/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.uml.model.RelationComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;
import org.uml.visual.providers.ClassConnectProvider;
import org.uml.visual.providers.ScenePopupMenuProvider;
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
    private ClassDiagram umlClassDiagram;
    
    private LayerWidget connectionLayer;
    private LayerWidget interractionLayer;
    

    public ClassDiagramScene(ClassDiagram umlClassDiagram) {

        this.umlClassDiagram = umlClassDiagram;
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);
        connectionLayer= new LayerWidget(this);
        addChild(connectionLayer);
        interractionLayer= new LayerWidget(this);
        addChild(interractionLayer);
        getActions().addAction(ActionFactory.createPanAction());
        getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.1));
        getActions().addAction(ActionFactory.createAcceptAction(new SceneAcceptProvider(this)));
        getActions().addAction(ActionFactory.createPopupMenuAction( new ScenePopupMenuProvider(this)));   
        getActions().addAction(ActionFactory.createMoveAction(ActionFactory.createSnapToGridMoveStrategy(16, 16), null));
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
    public ClassDiagram getUmlClassDiagram() {
        return umlClassDiagram;
    }

    @Override
    protected Widget attachNodeWidget(ClassDiagramComponent n) {
        ClassWidget widget = null;
        if (n instanceof ClassComponent) {                      // Mozda refleksijom da pretavaramo imena komponente u widgete ili neko mapiranje kao u Neurophu? 
            widget = UmlWidgetFactory.createClassWidget(this, (ClassComponent) n);
        } else {
            widget = new ClassWidget(this, null);
        }

        //WARNING Ovo je ukljuceno u Factory Methodu 
        
//        widget.getActions().addAction(ActionFactory.createMoveAction());
//        //single-click, the event is not consumed:
//        widget.getActions().addAction(createSelectAction());
//
//        //mouse-dragged, the event is consumed while mouse is dragged:
//        widget.getActions().addAction(ActionFactory.createMoveAction());
//
//        //mouse-over, the event is consumed while the mouse is over the widget:
//        widget.getActions().addAction(createObjectHoverAction());

        mainLayer.addChild(widget);
        return widget;
    }

    @Override
    protected Widget attachEdgeWidget(RelationComponent e) {
        ConnectionWidget widget= new ConnectionWidget(this);
        widget.setTargetAnchorShape(AnchorShape.NONE);
        widget.setEndPointShape (PointShape.SQUARE_FILLED_BIG);
        WidgetAction.Chain actions= widget.getActions();
        actions.addAction(createObjectHoverAction());
        actions.addAction(createSelectAction());
        //reconnectAction;
        connectionLayer.addChild(widget);
        return widget;
    }

    @Override
    protected void attachEdgeSourceAnchor(RelationComponent edge, ClassDiagramComponent oldSourceNode, ClassDiagramComponent sourceNode) {
        ConnectionWidget edgeWidget= (ConnectionWidget) findWidget(edge);
        Widget sourceNodeWidget= findWidget(sourceNode);
        Anchor sourceAnchor=AnchorFactory.createRectangularAnchor(sourceNodeWidget);
        edgeWidget.setSourceAnchor(sourceAnchor);
    }

    @Override
    protected void attachEdgeTargetAnchor(RelationComponent edge, ClassDiagramComponent oldSourceNode, ClassDiagramComponent targetNode) {
        ConnectionWidget edgeWidget= (ConnectionWidget) findWidget(edge);
        Widget targetNodeWidget= findWidget(targetNode);
        Anchor targetAnchor = AnchorFactory.createRectangularAnchor (targetNodeWidget);
        edgeWidget.setTargetAnchor (targetAnchor);
    }
    
        public LayerWidget getInterractionLayer() {
        return interractionLayer;
    }

   

    /**
     * Ads widget to main layer
     *
     * @param widget
     */
    public void addWidget(IconNodeWidget widget) {
        mainLayer.addChild(widget);
        validate();
    }

    public LayerWidget getConnectionLayer() {
        return connectionLayer;
    }
    
}
