package org.uml.visual.widgets;

import org.uml.model.EnumComponent;
import org.uml.visual.widgets.anchors.RhombusAnchorShape;
import org.uml.model.relations.UseRelationComponent;
import org.uml.model.relations.RelationComponent;
import org.uml.model.relations.HasBaseRelationComponent;
import org.uml.visual.widgets.providers.popups.ConnectionPopupMenuProvider;
import java.awt.BasicStroke;
import java.awt.Rectangle;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.anchor.*;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.*;
import org.openide.util.Lookup;
import org.openide.util.lookup.*;
import org.uml.model.*;
import org.uml.model.relations.ImplementsRelationComponent;
import org.uml.model.relations.IsRelationComponent;
import org.uml.visual.UMLTopComponent;
import org.uml.visual.widgets.actions.RelationLabelTextFieldEditorAction;
import org.uml.visual.widgets.providers.*;
import org.uml.visual.widgets.providers.unused.ScenePopupMenuProvider;

/**
 *
 * https://blogs.oracle.com/geertjan/entry/how_to_serialize_visual_library
 * https://platform.netbeans.org/graph/examples.html layout, serijalizacija,
 * save as image
 *
 * @author NUGS
 */
public class ClassDiagramScene extends GraphScene<ComponentBase, RelationComponent> {

    private LayerWidget mainLayer;
    private LayerWidget connectionLayer;
    private LayerWidget interractionLayer;
    final private ClassDiagram classDiagram;
    final private UMLTopComponent umlTopComponent;
    private InstanceContent content = new InstanceContent();
    AbstractLookup aLookup = new AbstractLookup(content);

    public ClassDiagramScene(ClassDiagram umlClassDiagram, UMLTopComponent umlTopComponent) {

        classDiagram = umlClassDiagram;
        
        classDiagram.addDeleteListener(new IComponentDeleteListener() {
            @Override
            public void componentDeleted(ComponentBase component) {
                ComponentWidgetBase classWidget = (ComponentWidgetBase) findWidget(component);

                removeNodeWithEdges(component);
                
                classDiagram.removeRelationsForAComponent(component);
//                for (Map.Entry<String, RelationComponent> entry : classDiagram.getRelations().entrySet()) {
//                    RelationComponent relation = entry.getValue();
//                    if (relation.getSource().getName().equals(classWidget.getName()) || relation.getTarget().getName().equals(classWidget.getName())) {
//                        classDiagram.removeRelation(relation.getName());
//                        removeEdge(relation);
//                    }
//                }
//                removeNode(component);
                repaint();
                validate();
            }
        });
        
        this.umlTopComponent = umlTopComponent;
        mainLayer = new LayerWidget(this);
        addChild(mainLayer);
        connectionLayer = new LayerWidget(this);
        addChild(connectionLayer);
        interractionLayer = new LayerWidget(this);
        addChild(interractionLayer);

        // middle-click + drag  Scene.getInputBindings().getPanActionButton()
        getActions().addAction(ActionFactory.createPanAction());

        // ctrl + scroll        Scene.getInputBindings().getZoomActionModifiers()
        getActions().addAction(ActionFactory.createMouseCenteredZoomAction(1.1));
        //getActions().addAction(ActionFactory.createZoomAction());

        // To support selecting background scene (deselecting all widgets)
        getActions().addAction(ActionFactory.createSelectAction(new SceneSelectProvider(this), false));
        //getActions().addAction(this.createSelectAction());

        // To support drag-and-drop from the palette
        getActions().addAction(ActionFactory.createAcceptAction(new SceneAcceptProvider(this)));

        getActions().addAction(ActionFactory.createPopupMenuAction(new ScenePopupMenuProvider(this)));
        //getActions().addAction(ActionFactory.createMoveAction(ActionFactory.createSnapToGridMoveStrategy(16, 16), null));
        //getActions().addAction(ActionFactory.createZoomAction());
        //getActions().addAction(ActionFactory.createHoverAction(new ClassHoverProvider()));
        //getActions().addAction(ActionFactory.createHoverAction(new MemberHoverProvider()));
        // dodaj widget ali ne i componentu ponovo kao u addNode...

        for (ComponentBase comp : umlClassDiagram.getComponents().values()) {
            Widget w = addNode(comp);
            w.setPreferredLocation(convertLocalToScene(comp.getPosition()));
            w.setPreferredBounds(comp.getBounds());
        }

        for (RelationComponent rel : umlClassDiagram.getRelations().values()) {
            addRelationToScene(rel, rel.getSource(), rel.getTarget());
//            ConnectionWidget w = (ConnectionWidget) addEdge(rel);
//            this.setEdgeSource(rel, rel.getSource());
//            this.setEdgeTarget(rel, rel.getTarget());
        }

        // ne treba ovde, vec tamo gde se pravi scena
        //GraphLayoutFactory.createOrthogonalGraphLayout(this, true);
//        addObjectSceneListener(new ObjectSceneListener() {
//            @Override
//            public void objectAdded(ObjectSceneEvent event, Object addedObject) {
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void objectRemoved(ObjectSceneEvent event, Object removedObject) {
//                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void objectStateChanged(ObjectSceneEvent event, Object changedObject, ObjectState previousState, ObjectState newState) {
//                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void selectionChanged(ObjectSceneEvent event, Set<Object> previousSelection, Set<Object> newSelection) {
//                for (Object o : previousSelection) {
//                    if (o instanceof ComponentWidgetBase) {
//                        ComponentWidgetBase comp = (ComponentWidgetBase) o;
//                        comp.notifyStateChanged(comp.getState(), comp.getState().deriveSelected(false));
//                    }
//                    content.remove(o);
//                }
//                for (Object o : newSelection) {
//                    if (o instanceof ComponentWidgetBase) {
//                        ComponentWidgetBase comp = (ComponentWidgetBase) o;
//                        comp.notifyStateChanged(comp.getState(), comp.getState().deriveSelected(true));
//                    }
//                    content.add(o);
//                    //setSelectedObjects(newSelection);
//                }
//            }
//
//            @Override
//            public void highlightingChanged(ObjectSceneEvent event, Set<Object> previousHighlighting, Set<Object> newHighlighting) {
//                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void hoverChanged(ObjectSceneEvent event, Object previousHoveredObject, Object newHoveredObject) {
//                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//            }
//
//            @Override
//            public void focusChanged(ObjectSceneEvent event, Object previousFocusedObject, Object newFocusedObject) {
//                if (previousFocusedObject != null) {
//                    content.remove(previousFocusedObject);
//                }
//                if (newFocusedObject != null) {
//                    content.add(newFocusedObject);
//                }
//            }
//        }, ObjectSceneEventType.OBJECT_SELECTION_CHANGED, ObjectSceneEventType.OBJECT_FOCUS_CHANGED);
    }

    public UMLTopComponent getUmlTopComponent() {
        return umlTopComponent;
    }

    public ClassDiagram getClassDiagram() {
        return classDiagram;
    }

    @Override
    public Lookup getLookup() {
        return aLookup;
    }

    public InstanceContent getContent() {
        return content;
    }

    // adding of a component
    @Override
    protected Widget attachNodeWidget(ComponentBase component) {
        ComponentWidgetBase widget = null;

        // need to check, if loading existing diagram...
        if (!classDiagram.getComponents().containsValue(component)) {
            classDiagram.addComponent(component);
        }

        if (component instanceof ClassComponent) {
            widget = new ClassWidget(this, (ClassComponent) component);
        } else if (component instanceof InterfaceComponent) {
            widget = new InterfaceWidget(this, (InterfaceComponent) component);
        } else if (component instanceof EnumComponent) {
            widget = new EnumWidget(this, (EnumComponent) component);
        } else if (component instanceof PackageComponent) {
            widget = new PackageWidget(this, (PackageComponent) component);
        } else {
            throw new RuntimeException("Unknown component!");
        }

        mainLayer.addChild(widget);

        return widget;
    }

    // adding of a relation
    @Override
    protected Widget attachEdgeWidget(RelationComponent e) {
        if (getObjects().contains(e)) {
            System.out.println("Vec postoji!");
            return null;
        }

        LabelWidget name = new LabelWidget(this, e.getName());
        name.setOpaque(true);

        if (!classDiagram.getRelations().containsValue(e)) { // need to check, if loading existing diagram...
            classDiagram.addRelation(e);
        }

        ConnectionWidget widget = new ConnectionWidget(this);
        if (e instanceof ImplementsRelationComponent) {
            final float dash1[] = {10.0f};
            final BasicStroke dashed
                    = new BasicStroke(1.0f,
                            BasicStroke.CAP_BUTT,
                            BasicStroke.JOIN_MITER,
                            10.0f, dash1, 0.0f);
            widget.setStroke(dashed);
            widget.setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(45, 10));
            name.setLabel("<<implements>>");
            //e.setName("<<implements>>");
        } else if (e instanceof IsRelationComponent) {
            widget.setTargetAnchorShape(AnchorShape.TRIANGLE_HOLLOW);
            name.setLabel("is");
            //e.setName("is");
        } else if (e instanceof HasBaseRelationComponent) {
            HasBaseRelationComponent hasRelation = (HasBaseRelationComponent) e;

            if (hasRelation.isComposition())
                widget.setSourceAnchorShape(new RhombusAnchorShape(45, 10, true));
            else
                widget.setSourceAnchorShape(new RhombusAnchorShape(45, 10, false));

            widget.setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(45, 10));

            LabelWidget cardinalityTarget = new LabelWidget(this, hasRelation.getCardinalityTarget().toString());
            widget.addChild(cardinalityTarget);
            widget.setConstraint(cardinalityTarget, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_TARGET, 1f);
        } else {
            UseRelationComponent useRelation = (UseRelationComponent) e;

            widget.setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(45, 10));

            LabelWidget cardinalitySource = new LabelWidget(this, useRelation.getCardinalitySource().toString());
            widget.addChild(cardinalitySource);
            widget.setConstraint(cardinalitySource, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_SOURCE, 0f);

            LabelWidget cardinalityTarget = new LabelWidget(this, useRelation.getCardinalityTarget().toString());
            widget.addChild(cardinalityTarget);
            widget.setConstraint(cardinalityTarget, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_TARGET, 1f);
        }
        widget.addChild(name);
        widget.setConstraint(name, LayoutFactory.ConnectionWidgetLayoutAlignment.CENTER, 0.5f);
        name.getActions().addAction(ActionFactory.createInplaceEditorAction(new RelationLabelTextFieldEditorAction(e, classDiagram)));

        widget.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
        widget.setRouter(RouterFactory.createFreeRouter());
        widget.getActions().addAction(ActionFactory.createPopupMenuAction(new ConnectionPopupMenuProvider(widget, e)));

        widget.getActions().addAction(createObjectHoverAction());
        widget.getActions().addAction(createSelectAction());

        widget.setPaintControlPoints(true);
        widget.setControlPointShape(PointShape.SQUARE_FILLED_BIG);
        widget.getActions().addAction(ActionFactory.createAddRemoveControlPointAction());
        widget.getActions().addAction(ActionFactory.createMoveControlPointAction(ActionFactory.createFreeMoveControlPointProvider(), ConnectionWidget.RoutingPolicy.UPDATE_END_POINTS_ONLY));
        //reconnectAction;

        connectionLayer.addChild(widget);
        return widget;
    }

    @Override
    protected void attachEdgeSourceAnchor(RelationComponent edge, ComponentBase oldSourceNode, ComponentBase sourceNode) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        if (edgeWidget != null) {
            Widget sourceNodeWidget = findWidget(sourceNode);
            Anchor sourceAnchor = AnchorFactory.createRectangularAnchor(sourceNodeWidget);
            edgeWidget.setSourceAnchor(sourceAnchor);
        }
    }

    @Override
    protected void attachEdgeTargetAnchor(RelationComponent edge, ComponentBase oldSourceNode, ComponentBase targetNode) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        if (edgeWidget != null) {
            Widget targetNodeWidget = findWidget(targetNode);
            Anchor targetAnchor = AnchorFactory.createRectangularAnchor(targetNodeWidget);
            edgeWidget.setTargetAnchor(targetAnchor);
        }
    }

    public final void addRelationToScene(RelationComponent relation, ComponentBase source, ComponentBase target) {
        if (!getObjects().contains(relation)) {
            addEdge(relation);
            setEdgeSource(relation, source);
            setEdgeTarget(relation, target);
        } else {
            JOptionPane.showMessageDialog(null, "Relation already exists!");
        }
        repaint();
    }

    public LayerWidget getMainLayer() {
        return mainLayer;
    }

    public LayerWidget getConnectionLayer() {
        return connectionLayer;
    }

    public LayerWidget getInterractionLayer() {
        return interractionLayer;
    }

}
