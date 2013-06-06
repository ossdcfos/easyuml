package org.uml.visual.widgets;

import java.awt.BasicStroke;
import java.util.Set;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.anchor.Anchor;
import org.netbeans.api.visual.anchor.AnchorFactory;
import org.netbeans.api.visual.anchor.AnchorShape;
import org.netbeans.api.visual.anchor.AnchorShapeFactory;
import org.netbeans.api.visual.anchor.PointShape;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneListener;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.LayerWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;
import org.uml.model.HasRelationComponent;
import org.uml.model.InterfaceComponent;
import org.uml.model.RelationComponent;
import org.uml.model.UseRelationComponent;
import org.uml.visual.widgets.actions.RelationLabelTextFieldEditorAction;
import org.uml.visual.widgets.providers.ConnectionPopupMenuProvider;
import org.uml.visual.widgets.providers.ScenePopupMenuProvider;
import org.uml.visual.widgets.providers.SceneAcceptProvider;

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
    private LayerWidget connectionLayer;
    private LayerWidget interractionLayer;
    private ClassDiagram umlClassDiagram;
    
    InstanceContent content = new InstanceContent();
    AbstractLookup aLookup = new AbstractLookup(content);    
            
    public ClassDiagramScene(ClassDiagram umlClassDiagram) {

        this.umlClassDiagram = umlClassDiagram;
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);
        connectionLayer = new LayerWidget(this);
        addChild(connectionLayer);
        interractionLayer = new LayerWidget(this);
        addChild(interractionLayer);
        getActions().addAction(ActionFactory.createPanAction());
        getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.1));
        getActions().addAction(ActionFactory.createAcceptAction(new SceneAcceptProvider(this)));
        getActions().addAction(ActionFactory.createPopupMenuAction(new ScenePopupMenuProvider(this)));
        getActions().addAction(ActionFactory.createMoveAction(ActionFactory.createSnapToGridMoveStrategy(16, 16), null));
        getActions().addAction(ActionFactory.createZoomAction());
        
        addObjectSceneListener(new ObjectSceneListener() {

            @Override
            public void objectAdded(ObjectSceneEvent event, Object addedObject) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void objectRemoved(ObjectSceneEvent event, Object removedObject) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void objectStateChanged(ObjectSceneEvent event, Object changedObject, ObjectState previousState, ObjectState newState) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void selectionChanged(ObjectSceneEvent event, Set<Object> previousSelection, Set<Object> newSelection) {
                for (Object o : previousSelection) {                   
                    
                    content.remove(o);
                }

                for (Object o : newSelection) {
                    content.add(o);
                }
            }

            @Override
            public void highlightingChanged(ObjectSceneEvent event, Set<Object> previousHighlighting, Set<Object> newHighlighting) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void hoverChanged(ObjectSceneEvent event, Object previousHoveredObject, Object newHoveredObject) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void focusChanged(ObjectSceneEvent event, Object previousFocusedObject, Object newFocusedObject) {
                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
            
        });
        
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
    public Lookup getLookup() {
        return aLookup;
    }    

    @Override
    protected Widget attachNodeWidget(ClassDiagramComponent component) {
        
        ComponentWidgetBase widget = null;
        umlClassDiagram.addComponent(component);
        if (component instanceof ClassComponent) {
          widget = new ClassWidget(this, (ClassComponent) component);          
        } else if(component instanceof InterfaceComponent) {
          widget = new InterfaceWidget(this, (InterfaceComponent) component);
        } else if(component instanceof EnumComponent){
          widget= new EnumWidget(this, (EnumComponent) component);
        } else {
            throw new RuntimeException("Unknown component!");
        }
        // add block for RelationComponent too!
        
        
        //WARNING Ovo prebaciti u ComponentWidgetBase akcije zajednicke za sve komponnete dijagrama

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
        LabelWidget name = new LabelWidget (this, e.getName());
        name.setOpaque (true);
        umlClassDiagram.addRelation(e);
        ConnectionWidget widget = new ConnectionWidget(this);
        if(e.getClass().getSimpleName().equals("ImplementsRelationComponent")) {
        final float dash1[] = {10.0f};
        final BasicStroke dashed =
        new BasicStroke(1.0f,
                        BasicStroke.CAP_BUTT,
                        BasicStroke.JOIN_MITER,
                        10.0f, dash1, 0.0f);
         widget.setStroke(dashed);
         widget.setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(45, 10));
         name.setLabel("<<implements>>");
        }
        else if (e.getClass().getSimpleName().equals("IsRelationComponent")) {
         widget.setTargetAnchorShape(AnchorShape.TRIANGLE_HOLLOW);
         e.setName("is");
        }
        else if (e.getClass().getSimpleName().equals("HasRelationComponent")) {
         widget.setSourceAnchorShape(AnchorShapeFactory.createImageAnchorShape(ImageUtilities.loadImage("org/uml/visual/icons/rhombus.gif")));
         widget.setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(45, 10));
         HasRelationComponent hasRelation = (HasRelationComponent) e;
         LabelWidget cardinalityTarget = new LabelWidget(this, hasRelation.getCardinalityTarget().toString());
         cardinalityTarget.setOpaque (true);
         widget.addChild(cardinalityTarget);
         widget.setConstraint (cardinalityTarget, LayoutFactory.ConnectionWidgetLayoutAlignment.BOTTOM_RIGHT, 2f);
        }
        else {
         widget.setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(45, 10));
         UseRelationComponent useRelation = (UseRelationComponent) e;
         LabelWidget cardinalitySource = new LabelWidget(this, useRelation.getCardinalitySource().toString());
         cardinalitySource.setOpaque (true);
         widget.addChild(cardinalitySource);
         widget.setConstraint (cardinalitySource, LayoutFactory.ConnectionWidgetLayoutAlignment.BOTTOM_RIGHT, -0.8f);
         LabelWidget cardinalityTarget = new LabelWidget(this, useRelation.getCardinalityTarget().toString());
         cardinalityTarget.setOpaque (true);
         widget.addChild(cardinalityTarget);
         widget.setConstraint (cardinalityTarget, LayoutFactory.ConnectionWidgetLayoutAlignment.BOTTOM_RIGHT, 0.8f);
        }
        widget.setEndPointShape (PointShape.SQUARE_FILLED_BIG);
        widget.setRouter(RouterFactory.createFreeRouter());
        widget.setPaintControlPoints (true);
        widget.setControlPointShape (PointShape.SQUARE_FILLED_BIG);
        widget.getActions().addAction(ActionFactory.createPopupMenuAction(new ConnectionPopupMenuProvider(widget,umlClassDiagram,e,name)));
        name.getActions().addAction(ActionFactory.createInplaceEditorAction(new RelationLabelTextFieldEditorAction(e,umlClassDiagram)));
        widget.addChild(name);
        widget.setConstraint (name, LayoutFactory.ConnectionWidgetLayoutAlignment.CENTER, 0.5f);
        WidgetAction.Chain actions= widget.getActions();
        actions.addAction(createObjectHoverAction());
        actions.addAction(createSelectAction());
        actions.addAction(ActionFactory.createAddRemoveControlPointAction());
        actions.addAction (ActionFactory.createMoveControlPointAction (ActionFactory.createFreeMoveControlPointProvider (), ConnectionWidget.RoutingPolicy.UPDATE_END_POINTS_ONLY));
        //reconnectAction;
        connectionLayer.addChild(widget);
    
        return widget;
    }

    @Override
    protected void attachEdgeSourceAnchor(RelationComponent edge, ClassDiagramComponent oldSourceNode, ClassDiagramComponent sourceNode) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        Widget sourceNodeWidget = findWidget(sourceNode);
        Anchor sourceAnchor = AnchorFactory.createRectangularAnchor(sourceNodeWidget);
        edgeWidget.setSourceAnchor(sourceAnchor);
    }

    @Override
    protected void attachEdgeTargetAnchor(RelationComponent edge, ClassDiagramComponent oldSourceNode, ClassDiagramComponent targetNode) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        Widget targetNodeWidget = findWidget(targetNode);
        Anchor targetAnchor = AnchorFactory.createRectangularAnchor(targetNodeWidget);
        edgeWidget.setTargetAnchor(targetAnchor);
    }

    public LayerWidget getInterractionLayer() {
        return interractionLayer;
    }

    /**
     * Ads widget to main layer
     *
     * @param widget
     */
//    public void addWidget(IconNodeWidget widget) {
//        mainLayer.addChild(widget);
//        validate();
//    }

    public LayerWidget getConnectionLayer() {
        return connectionLayer;
    }

    public LayerWidget getMainLayer() {
        return mainLayer;
    }
    
}
