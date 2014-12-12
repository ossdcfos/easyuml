package org.uml.visual.widgets;

import org.uml.visual.widgets.components.ClassWidget;
import org.uml.visual.widgets.components.EnumWidget;
import org.uml.visual.widgets.components.ComponentWidgetBase;
import org.uml.visual.widgets.components.InterfaceWidget;
import org.uml.visual.widgets.components.PackageWidget;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.components.PackageComponent;
import org.uml.model.components.EnumComponent;
import org.uml.visual.widgets.anchors.RhombusAnchorShape;
import org.uml.model.relations.UseRelation;
import org.uml.model.relations.RelationBase;
import org.uml.model.relations.HasBaseRelation;
import org.uml.visual.widgets.providers.popups.RelationPopupMenuProvider;
import java.awt.BasicStroke;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.anchor.*;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.router.RouterFactory;
import org.netbeans.api.visual.widget.*;
import org.openide.nodes.AbstractNode;
import org.openide.util.Lookup;
import org.openide.util.lookup.*;
import org.uml.model.*;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.IsRelation;
import org.uml.visual.UMLTopComponent;
import org.uml.visual.widgets.actions.RelationLabelTextFieldEditorAction;
import org.uml.visual.widgets.providers.*;
import org.uml.visual.widgets.providers.popups.ScenePopupMenuProvider;

/**
 *
 * https://blogs.oracle.com/geertjan/entry/how_to_serialize_visual_library
 * https://platform.netbeans.org/graph/examples.html layout, serijalizacija,
 * save as image
 *
 * @author NUGS
 */
public class ClassDiagramScene extends GraphScene<ComponentBase, RelationBase> {

    private LayerWidget mainLayer;
    private LayerWidget connectionLayer;
    private LayerWidget interractionLayer;
    private ClassDiagram classDiagram;
    private UMLTopComponent umlTopComponent;
    private InstanceContent content = new InstanceContent();
    AbstractLookup aLookup = new AbstractLookup(content);

    public ClassDiagramScene(ClassDiagram umlClassDiagram, final UMLTopComponent umlTopComponent) {

        classDiagram = umlClassDiagram;
        
        classDiagram.addDeleteListener(new IComponentDeleteListener() {
            @Override
            public void componentDeleted(INameable component) {
                removeNodeWithEdges((ComponentBase)component);
                
                classDiagram.removeRelationsForAComponent(component);
                
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
        //getActions().addAction(createSelectAction());

        // To support drag-and-drop from the palette
        getActions().addAction(ActionFactory.createAcceptAction(new SceneAcceptProvider(this)));

        getActions().addAction(ActionFactory.createPopupMenuAction(new ScenePopupMenuProvider(this)));
        //getActions().addAction(ActionFactory.createMoveAction(ActionFactory.createSnapToGridMoveStrategy(16, 16), null));
        //getActions().addAction(ActionFactory.createZoomAction());
        //getActions().addAction(ActionFactory.createHoverAction(new ClassHoverProvider()));
        //getActions().addAction(ActionFactory.createHoverAction(new MemberHoverProvider()));
        // dodaj widget ali ne i componentu ponovo kao u addNode...

        for (ComponentBase comp : umlClassDiagram.getComponents()) {
            Widget w = addNode(comp);
            w.setPreferredLocation(convertLocalToScene(comp.getPosition()));
            w.setPreferredBounds(comp.getBounds());
        }

        for (RelationBase rel : umlClassDiagram.getRelations()) {
            addRelationToScene(rel, rel.getSource(), rel.getTarget());
        }

        // ne treba ovde, vec tamo gde se pravi scena
        //GraphLayoutFactory.createOrthogonalGraphLayout(this, true);
//        addObjectSceneListener(new ObjectSceneListener() {
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
//        }, ObjectSceneEventType.OBJECT_SELECTION_CHANGED);
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
        if (!classDiagram.getComponents().contains(component)) {
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
    protected Widget attachEdgeWidget(RelationBase e) {
        if (getObjects().contains(e)) {
            System.out.println("Vec postoji!");
            return null;
        }

        LabelWidget name = new LabelWidget(this, e.getName());
        name.setOpaque(true);

        if (!classDiagram.getRelations().contains(e)) { // need to check, if loading existing diagram...
            classDiagram.addRelation(e);
        }

        ConnectionWidget widget = new ConnectionWidget(this);
        if (e instanceof ImplementsRelation) {
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
        } else if (e instanceof IsRelation) {
            widget.setTargetAnchorShape(AnchorShape.TRIANGLE_HOLLOW);
            name.setLabel("is");
            //e.setName("is");
        } else if (e instanceof HasBaseRelation) {
            HasBaseRelation hasRelation = (HasBaseRelation) e;

            if (hasRelation.isComposition())
                widget.setSourceAnchorShape(new RhombusAnchorShape(45, 10, true));
            else
                widget.setSourceAnchorShape(new RhombusAnchorShape(45, 10, false));

            widget.setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(45, 10));

            LabelWidget cardinalityTarget = new LabelWidget(this, hasRelation.getCardinalityTarget().toString());
            widget.addChild(cardinalityTarget);
            widget.setConstraint(cardinalityTarget, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_TARGET, 0.93f);
        } else {
            UseRelation useRelation = (UseRelation) e;

            widget.setTargetAnchorShape(AnchorShapeFactory.createArrowAnchorShape(45, 10));

            LabelWidget cardinalitySource = new LabelWidget(this, useRelation.getCardinalitySource().toString());
            widget.addChild(cardinalitySource);
            widget.setConstraint(cardinalitySource, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_SOURCE, 0.07f);

            LabelWidget cardinalityTarget = new LabelWidget(this, useRelation.getCardinalityTarget().toString());
            widget.addChild(cardinalityTarget);
            widget.setConstraint(cardinalityTarget, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_TARGET, 0.93f);
        }
        widget.addChild(name);
        widget.setConstraint(name, LayoutFactory.ConnectionWidgetLayoutAlignment.CENTER, 0.5f);
        name.getActions().addAction(ActionFactory.createInplaceEditorAction(new RelationLabelTextFieldEditorAction(e, classDiagram)));

        widget.setEndPointShape(PointShape.SQUARE_FILLED_BIG);
        widget.setRouter(RouterFactory.createFreeRouter());
        widget.getActions().addAction(ActionFactory.createPopupMenuAction(new RelationPopupMenuProvider(widget, e)));

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
    protected void attachEdgeSourceAnchor(RelationBase edge, ComponentBase oldSourceNode, ComponentBase sourceNode) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        if (edgeWidget != null) {
            Widget sourceNodeWidget = findWidget(sourceNode);
            Anchor sourceAnchor = AnchorFactory.createRectangularAnchor(sourceNodeWidget);
            edgeWidget.setSourceAnchor(sourceAnchor);
        }
    }

    @Override
    protected void attachEdgeTargetAnchor(RelationBase edge, ComponentBase oldSourceNode, ComponentBase targetNode) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        if (edgeWidget != null) {
            Widget targetNodeWidget = findWidget(targetNode);
            Anchor targetAnchor = AnchorFactory.createRectangularAnchor(targetNodeWidget);
            edgeWidget.setTargetAnchor(targetAnchor);
        }
    }

    public final void addRelationToScene(RelationBase relation, ComponentBase source, ComponentBase target) {
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
