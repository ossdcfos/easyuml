package org.uml.visual.widgets;

import com.timboudreau.vl.jung.ObjectSceneAdapter;
import java.beans.PropertyVetoException;
import java.util.Collection;
import org.uml.visual.widgets.components.ClassWidget;
import org.uml.visual.widgets.components.EnumWidget;
import org.uml.visual.widgets.components.ComponentWidgetBase;
import org.uml.visual.widgets.components.InterfaceWidget;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.components.EnumComponent;
import org.uml.model.relations.RelationBase;
import org.uml.model.relations.HasBaseRelation;
import java.util.HashMap;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.anchor.*;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneEventType;
import org.netbeans.api.visual.router.Router;
import org.netbeans.api.visual.widget.*;
import org.openide.explorer.ExplorerManager;
import org.openide.nodes.Node;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.Utilities;
import org.openide.util.lookup.*;
import org.uml.explorer.ClassDiagramNode;
import org.uml.explorer.ComponentNode;
import org.uml.explorer.MemberNode;
import org.uml.model.*;
import org.uml.model.members.MemberBase;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.UseRelation;
import org.uml.visual.UMLTopComponent;
import org.uml.visual.colorthemes.ColorTheme;
import org.uml.visual.colorthemes.ColorThemesStore;
import org.uml.visual.widgets.anchors.ParallelNodeAnchor;
import org.uml.visual.widgets.providers.*;
import org.uml.visual.widgets.popups.ScenePopupMenuProvider;
import org.uml.visual.widgets.relations.HasBaseRelationWidget;
import org.uml.visual.widgets.relations.ImplementsRelationWidget;
import org.uml.visual.widgets.relations.IsRelationWidget;
import org.uml.visual.widgets.relations.RelationBaseWidget;
import org.uml.visual.widgets.relations.UseRelationWidget;

/**
 *
 * https://blogs.oracle.com/geertjan/entry/how_to_serialize_visual_library
 * https://platform.netbeans.org/graph/examples.html layout, serijalizacija,
 * save as image
 *
 * @author NUGS
 */
public class ClassDiagramScene extends GraphScene<ComponentBase, RelationBase> implements LookupListener {

    private final LayerWidget mainLayer;
    private final LayerWidget connectionLayer;
    private final LayerWidget interractionLayer;

    private final ClassDiagram classDiagram;
    private final UMLTopComponent umlTopComponent;

    private final InstanceContent content = new InstanceContent();
    private final AbstractLookup aLookup = new AbstractLookup(content);

    private final HashMap<ComponentWidgetBase, Anchor> anchorMap = new HashMap<>();
    private Router selfLinkRouter;

    public static ColorTheme colorTheme = ColorThemesStore.DEFAULT_COLOR_THEME;

    private final Lookup.Result<ClassDiagramNode> focusedExplorerDiagram;
    private final Lookup.Result<ComponentNode> focusedExplorerComponent;
    private final Lookup.Result<MemberNode> focusedExplorerMember;

    @SuppressWarnings("LeakingThisInConstructor")
    public ClassDiagramScene(ClassDiagram umlClassDiagram, final UMLTopComponent umlTopComponent) {

        classDiagram = umlClassDiagram;
        // TODO selection of scene
        addObject(classDiagram, this);
        classDiagram.addDeleteListener(new IComponentDeleteListener() {
            @Override
            public void componentDeleted(INameable component) {
                removeNodeWithEdges((ComponentBase) component);

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
        // To support selecting background scene (deselecting all widgets)
        getActions().addAction(ActionFactory.createSelectAction(new SceneSelectProvider(), false));
        // To support drag-and-drop from the palette
        getActions().addAction(ActionFactory.createAcceptAction(new SceneAcceptProvider()));
        // To support right-click on the scene
        getActions().addAction(ActionFactory.createPopupMenuAction(new ScenePopupMenuProvider(this)));

        addObjectSceneListener(new FocusAdapter(),
                ObjectSceneEventType.OBJECT_ADDED,
                ObjectSceneEventType.OBJECT_REMOVED,
                ObjectSceneEventType.OBJECT_FOCUS_CHANGED);

        for (ComponentBase comp : umlClassDiagram.getComponents()) {
            Widget w = addNode(comp);
            w.setPreferredLocation(convertLocalToScene(comp.getLocation()));
            w.setPreferredBounds(comp.getBounds());
        }

        for (RelationBase rel : umlClassDiagram.getRelations()) {
            addRelationToScene(rel, rel.getSource(), rel.getTarget());
        }

        focusedExplorerDiagram = Utilities.actionsGlobalContext().lookupResult(ClassDiagramNode.class);
        focusedExplorerComponent = Utilities.actionsGlobalContext().lookupResult(ComponentNode.class);
        focusedExplorerMember = Utilities.actionsGlobalContext().lookupResult(MemberNode.class);
        addLookupListeners();
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

    public UMLTopComponent getUmlTopComponent() {
        return umlTopComponent;
    }

    public ExplorerManager getExplorerManager() {
        return getUmlTopComponent().getExplorerManager();
    }

    public ClassDiagram getClassDiagram() {
        return classDiagram;
    }

    public void setColorTheme(String name) {
        ColorTheme theme = ColorThemesStore.getColorTheme(name);
        ClassDiagramScene.colorTheme = theme;
        for (Widget widget : mainLayer.getChildren()) {
            if (widget instanceof ComponentWidgetBase) {
                ComponentWidgetBase componentWidget = (ComponentWidgetBase) widget;
                componentWidget.updateColor();
            }
        }
    }

    // Used to enable selection detection from ClassDiagramScene
    private void addLookupListeners() {
        focusedExplorerDiagram.addLookupListener(this);
        focusedExplorerComponent.addLookupListener(this);
        focusedExplorerMember.addLookupListener(this);
    }

    // Used to disable selection detection from ClassDiagramScene, while changing selection in explorer (avoid loop selections)
    private void removeLookupListeners() {
        focusedExplorerDiagram.removeLookupListener(this);
        focusedExplorerComponent.removeLookupListener(this);
        focusedExplorerMember.removeLookupListener(this);
    }

    @Override
    public Lookup getLookup() {
        return aLookup;
    }

    // adding of a component
    @Override
    protected Widget attachNodeWidget(ComponentBase component) {
        // if adding new component, add it to the diagram
        if (!classDiagram.getComponents().contains(component)) {
            classDiagram.addPartToContainter(component);
        }

        // Initialization
        ComponentWidgetBase widget = null;
        if (component instanceof ClassComponent) {
            widget = new ClassWidget(this, (ClassComponent) component);
        } else if (component instanceof InterfaceComponent) {
            widget = new InterfaceWidget(this, (InterfaceComponent) component);
        } else if (component instanceof EnumComponent) {
            widget = new EnumWidget(this, (EnumComponent) component);
        } else {
            throw new RuntimeException("Unknown component!");
        }

        mainLayer.addChild(widget);
        return widget;
    }

    // adding of a relation
    @Override
    protected Widget attachEdgeWidget(RelationBase relation) {
        // if adding new relation, add it to the diagram
        if (!classDiagram.getRelations().contains(relation)) {
            classDiagram.addRelation(relation);
        }

        // Initialization
        RelationBaseWidget widget = null;
        if (relation instanceof ImplementsRelation) {
            widget = new ImplementsRelationWidget(relation, this);
        } else if (relation instanceof IsRelation) {
            widget = new IsRelationWidget(relation, this);
        } else if (relation instanceof HasBaseRelation) {
            widget = new HasBaseRelationWidget(relation, this);
        } else if (relation instanceof UseRelation) {
            widget = new UseRelationWidget(relation, this);
        } else {
            throw new RuntimeException("Unknown component!");
        }

        connectionLayer.addChild(widget);
        return widget;
    }

    @Override
    protected void attachEdgeSourceAnchor(RelationBase edge, ComponentBase oldSourceNode, ComponentBase sourceNode) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        ComponentWidgetBase nodeWidget = (ComponentWidgetBase) findWidget(sourceNode);
        if (nodeWidget != null) {
            edgeWidget.setSourceAnchor(getAnchorForWidget(nodeWidget));
        }
        if (isSelfLink(edgeWidget))
            setSelfLinkRouter(edgeWidget);
    }

    @Override
    protected void attachEdgeTargetAnchor(RelationBase edge, ComponentBase oldTargetNode, ComponentBase targetNode) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        ComponentWidgetBase nodeWidget = (ComponentWidgetBase) findWidget(targetNode);
        if (nodeWidget != null) {
            edgeWidget.setTargetAnchor(getAnchorForWidget(nodeWidget));
        }
        if (isSelfLink(edgeWidget))
            setSelfLinkRouter(edgeWidget);
    }

    // Each widget must always return the same anchor in order for ParallelNodeAnchor to work
    private Anchor getAnchorForWidget(ComponentWidgetBase widget) {
        Anchor retVal = anchorMap.get(widget);

        if (retVal == null) {
            retVal = new ParallelNodeAnchor(widget);
            anchorMap.put(widget, retVal);
        }
        return retVal;
    }

    private boolean isSelfLink(ConnectionWidget connection) {
        Anchor sourceAnchor = connection.getSourceAnchor();
        Anchor targetAnchor = connection.getTargetAnchor();
        if (sourceAnchor != null && targetAnchor != null && sourceAnchor.getRelatedWidget() == targetAnchor.getRelatedWidget())
            return true;
        else
            return false;
    }

    private void setSelfLinkRouter(ConnectionWidget connection) {
        if (selfLinkRouter == null) {
            selfLinkRouter = new SelfLinkRouter();
        }
        connection.setRouter(selfLinkRouter);
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
        validate();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public void resultChanged(LookupEvent ev) {
        Lookup.Result source = (Lookup.Result) ev.getSource();
        Collection instances = source.allInstances();
        if (!instances.isEmpty()) {
            for (Object instance : instances) {
                // Set focused object based on the selection of a Node in Explorer
                if (instance instanceof ClassDiagramNode) {
                    selectScene();
                } else if (instance instanceof ComponentNode) {
                    ComponentBase component = ((ComponentNode) instance).getComponent();
                    if (isNode(component)) {
                        setFocusedObjectAndToggleListeners(component);
                    }
                } else if (instance instanceof MemberNode) {
                    MemberBase member = ((MemberNode) instance).getMember();
                    if (isObject(member)) {
                        setFocusedObjectAndToggleListeners(member);
                    }
                }
                // Process only the first of the instances (selection limited do 1)
                break;
            }
        } else {
            // No component or member selected in explorer, or some other TopComponent is focused, clear selection
            // this doesn't work, because sometimes there is empty instances event, when we have actually just selected some node
            // ExplorerTopComponent
            if (!umlTopComponent.isActivatedLinkedTC()) {
                selectScene();
            }
        }
        repaint();
        validate();
    }

    public void deselectAll() {
        setFocusedObjectAndToggleListeners(null);
    }

    // Selection and content.add need to be here, because on the first opening of class diagram
    // oldFocus and newFocus are null, so the event doesn't trigger
    public void selectScene() {
        // Select classDiagram (deselect all)
        // This does not work when selecting outside the scene. That case is supported in umlTopComponent.componentDeactivated()
        setFocusedObjectAndToggleListeners(classDiagram);

        // Focus UMLTopComponent when scene is selected
        getUmlTopComponent().requestFocusInWindow();
    }

    public void setFocusedObjectAndToggleListeners(Object object) {
        removeLookupListeners();
        setFocusedObject(object);
        addLookupListeners();
    }

    private class FocusAdapter extends ObjectSceneAdapter {

        // Nodes are selected in order for properties to show the sheet from UMLTopComponent ExplorerManager lookup
        // Content is used so that ExplorerTopComponent can synchronize selection through listening to lookup
        @Override
        public void focusChanged(ObjectSceneEvent event, Object previousFocusedObject, Object newFocusedObject) {
            // For selection inside the Explorer
            if (previousFocusedObject != null) {
                content.remove(previousFocusedObject);
            } else {
                content.remove(classDiagram);
            }

            if (newFocusedObject != null) {
                // For properties
                if (newFocusedObject instanceof ComponentBase) {
                    selectComponentNode((ComponentBase) newFocusedObject);
                } else if (newFocusedObject instanceof MemberBase) {
                    selectMemberNode((MemberBase) newFocusedObject);
                } else if (newFocusedObject instanceof ClassDiagram) {
                    selectNode(getExplorerManager().getRootContext());
                }

                // For selection inside the Explorer
                content.add(newFocusedObject);
            } else {
                deselectAllNodes();
            }
        }

        @Override
        public void objectAdded(ObjectSceneEvent event, Object addedObject) {
            umlTopComponent.notifyModified();
        }

        @Override
        public void objectRemoved(ObjectSceneEvent event, Object removedObject) {
            umlTopComponent.notifyModified();
        }
    }

    private void selectComponentNode(ComponentBase component) {
        for (Node cn : getExplorerManager().getRootContext().getChildren().getNodes()) {
            ComponentNode componentNode = (ComponentNode) cn;
            if (componentNode.getComponent() == component) {
                selectNode(componentNode);
                return;
            }
        }
    }

    private void selectMemberNode(MemberBase member) {
        for (Node cn : getExplorerManager().getRootContext().getChildren().getNodes()) {
            for (Node mn : cn.getChildren().getNodes()) {
                MemberNode memberNode = (MemberNode) mn;
                if (memberNode.getMember() == member) {
                    selectNode(memberNode);
                    return;
                }
            }
        }
    }

    /**
     * Select node in the tree. If null is passed, selection is cleared.
     *
     * @param node
     */
    private void selectNode(Node node) {
        try {
            if (node != null) getExplorerManager().setSelectedNodes(new Node[]{node});
            else getExplorerManager().setSelectedNodes(new Node[]{});
        } catch (PropertyVetoException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
    
    private void deselectAllNodes(){
        selectNode(null);
    }
}
