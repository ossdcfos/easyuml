package org.uml.visual.widgets;

import org.uml.visual.widgets.anchors.SelfLinkRouter;
import com.timboudreau.vl.jung.ObjectSceneAdapter;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Collections;
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
import java.util.Set;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.anchor.*;
import org.netbeans.api.visual.graph.GraphScene;
import org.netbeans.api.visual.model.ObjectSceneEvent;
import org.netbeans.api.visual.model.ObjectSceneEventType;
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
import org.uml.visual.themes.Theme;
import org.uml.visual.themes.ColorThemesStore;
import org.uml.visual.widgets.anchors.ParallelNodeAnchor;
import org.uml.visual.widgets.providers.*;
import org.uml.visual.widgets.popups.ScenePopupMenuProvider;
import org.uml.visual.widgets.relations.HasBaseRelationWidget;
import org.uml.visual.widgets.relations.ImplementsRelationWidget;
import org.uml.visual.widgets.relations.IsRelationWidget;
import org.uml.visual.widgets.relations.RelationBaseWidget;
import org.uml.visual.widgets.relations.UseRelationWidget;

/**
 * ClassDiagramScene is a GraphScene, where nodes are components derived from
 * ComponentBase class (classes, interfaces and enums) and edges are relations
 * derived from RelationBase class. It listens to lookup from node lookup from
 * the Explorer, in order to select appropriate nodes in graph and listens to
 * property changes on underlying class diagram, to catch REMOVE events and
 * appropriately remove the widgets.
 *
 * https://blogs.oracle.com/geertjan/entry/how_to_serialize_visual_library
 * https://platform.netbeans.org/graph/examples.html layout, serijalizacija,
 * save as image
 *
 * @author NUGS
 */
public class ClassDiagramScene extends GraphScene<ComponentBase, RelationBase> implements LookupListener, PropertyChangeListener {

    /**
     * Layer holding all the node widgets.
     */
    private final LayerWidget mainLayer;
    /**
     * Layer holding all the relation widgets.
     */
    private final LayerWidget connectionLayer;
    /**
     * Layer holding all the things being interacted with (drag and drop etc.).
     */
    private final LayerWidget interractionLayer;

    /**
     * Underlying class diagram.
     */
    private final ClassDiagram classDiagram;
    /**
     * Parent UMLTopComponent.
     */
    private final UMLTopComponent umlTopComponent;

    /**
     * Abstract lookup used to publish selected objects.
     */
    private final InstanceContent content = new InstanceContent();
    private final AbstractLookup aLookup = new AbstractLookup(content);

    /**
     * Translate widget -> anchor. Every widget uses one anchor, as that is a
     * prerequisite to use the ParallelNodeAnchor.
     */
    private final HashMap<ComponentWidgetBase, Anchor> anchorMap = new HashMap<>();

    /**
     * Visual options flags and themes.
     */
    private boolean showIcons = true;
    private boolean showMembers = true;
    private boolean showSimpleTypes = false;
    private Theme colorTheme = ColorThemesStore.DEFAULT_COLOR_THEME;

    /**
     * Nodes lookup to listen to the selection in the UMLExplorer.
     */
    private final Lookup.Result<ClassDiagramNode> focusedExplorerDiagram;
    private final Lookup.Result<ComponentNode> focusedExplorerComponent;
    private final Lookup.Result<MemberNode> focusedExplorerMember;

    @SuppressWarnings("LeakingThisInConstructor")
    public ClassDiagramScene(ClassDiagram umlClassDiagram, final UMLTopComponent umlTopComponent) {

        classDiagram = umlClassDiagram;
        addObject(classDiagram, this); // seleciton of scene
        classDiagram.addPropertyChangeListener(this);

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

        setFont(new Font("Default", Font.PLAIN, 12));

        // ObjectSceneAdapter to support the selection events
        addObjectSceneListener(new FocusAdapter(),
                ObjectSceneEventType.OBJECT_FOCUS_CHANGED);

        // Add all the components to the diagram
        for (ComponentBase comp : classDiagram.getComponents()) {
            Widget w = addNode(comp);
            w.setPreferredLocation(convertLocalToScene(comp.getLocation()));
//            w.setPreferredBounds(comp.getBounds());
        }

        // Add all the relations to the diagram
        for (RelationBase rel : classDiagram.getRelations()) {
            addRelationToScene(rel);
        }

        // Listen to node lookup from the Explorer
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

    public Theme getColorTheme() {
        return colorTheme;
    }

    public void setColorTheme(String name) {
        colorTheme = ColorThemesStore.getColorTheme(name);
        for (Widget widget : mainLayer.getChildren()) {
            if (widget instanceof ComponentWidgetBase) {
                ComponentWidgetBase componentWidget = (ComponentWidgetBase) widget;
                componentWidget.updateColor();
            }
        }
    }

    public boolean isShowIcons() {
        return showIcons;
    }

    public void setShowIcons(boolean showIcons) {
        this.showIcons = showIcons;
        for (Widget widget : mainLayer.getChildren()) {
            if (widget instanceof ComponentWidgetBase) {
                ComponentWidgetBase componentWidget = (ComponentWidgetBase) widget;
                componentWidget.updateIconDisplay(showIcons);
            }
        }
        repaint();
        validate();
    }

    public boolean isShowMembers() {
        return showMembers;
    }

    public void setShowMembers(boolean showMembers) {
        this.showMembers = showMembers;
        for (Widget widget : mainLayer.getChildren()) {
            if (widget instanceof ComponentWidgetBase) {
                ComponentWidgetBase componentWidget = (ComponentWidgetBase) widget;
                componentWidget.updateMemberDisplay(showMembers);
            }
        }
        repaint();
        validate();
    }

    public boolean isShowSimpleTypes() {
        return showSimpleTypes;
    }

    public void setShowSimpleTypes(boolean showSimpleTypes) {
        this.showSimpleTypes = showSimpleTypes;
        for (Widget widget : mainLayer.getChildren()) {
            if (widget instanceof ComponentWidgetBase) {
                ComponentWidgetBase componentWidget = (ComponentWidgetBase) widget;
                componentWidget.updateTypeNamesDisplay(showSimpleTypes);
            }
        }
        repaint();
        validate();
    }

    /**
     * Used to enable selection detection from ClassDiagramScene
     */
    private void addLookupListeners() {
        focusedExplorerDiagram.addLookupListener(this);
        focusedExplorerComponent.addLookupListener(this);
        focusedExplorerMember.addLookupListener(this);
    }

    /**
     * Used to disable selection detection from ClassDiagramScene, while
     * changing selection in explorer (avoid loop selections)
     */
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
            classDiagram.addComponent(component);
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
//            throw new RuntimeException("Unknown component!");
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
//            throw new RuntimeException("Unknown relation!");
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
    }

    @Override
    protected void attachEdgeTargetAnchor(RelationBase edge, ComponentBase oldTargetNode, ComponentBase targetNode) {
        ConnectionWidget edgeWidget = (ConnectionWidget) findWidget(edge);
        ComponentWidgetBase nodeWidget = (ComponentWidgetBase) findWidget(targetNode);
        if (nodeWidget != null) {
            edgeWidget.setTargetAnchor(getAnchorForWidget(nodeWidget));
        }
        if (isSelfLink(edgeWidget)) {
            edgeWidget.setRouter(new SelfLinkRouter());
        }
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
        if (sourceAnchor != null && targetAnchor != null && sourceAnchor.getRelatedWidget() == targetAnchor.getRelatedWidget()) {
            return true;
        } else {
            return false;
        }
    }

    public final void addRelationToScene(RelationBase relation) {
        if (!getObjects().contains(relation)) {
            addEdge(relation);
            setEdgeSource(relation, relation.getSource());
            setEdgeTarget(relation, relation.getTarget());
        } else {
            JOptionPane.showMessageDialog(null, "Relation already exists!");
        }
        repaint();
        validate();
    }

    /**
     * Lookup listener which listens to Node selection from UMLExplorer.
     *
     * @param ev
     */
    @Override
    @SuppressWarnings("rawtypes")
    public void resultChanged(LookupEvent ev) {
        Lookup.Result source = (Lookup.Result) ev.getSource();
        Collection instances = source.allInstances();
        if (!instances.isEmpty()) {
            for (Object instance : instances) {
                // Set focused object based on the selection of a Node in Explorer
                if (instance instanceof ClassDiagramNode) {
                    setFocusedObjectAndToggleListeners(classDiagram);
//                    selectScene();
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
                setFocusedObjectAndToggleListeners(classDiagram);
//                selectScene();
            }
        }
        validate();
        repaint();
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

//         Focus UMLTopComponent when scene is selected
        getUmlTopComponent().requestFocus();
    }

    public void setFocusedObjectAndToggleListeners(Object object) {
        removeLookupListeners();
        Set selection;
        if (object != null) {
            selection = Collections.singleton(object);
        } else {
            selection = Collections.EMPTY_SET;
        }
        setSelectedObjects(selection);
        setFocusedObject(object);
        addLookupListeners();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (null != evt.getPropertyName()) {
            switch (evt.getPropertyName()) {
                case "name":
                    break;
                case "ADD_COMPONENT":
                    break;
                case "REMOVE_COMPONENT":
                    ComponentBase component = (ComponentBase) evt.getNewValue();
                    removeNode(component);
                    classDiagram.removeRelationsForAComponent(component);
                    break;
                case "ADD_RELATION":
                    break;
                case "REMOVE_RELATION":
                    RelationBase relation = (RelationBase) evt.getNewValue();
                    removeEdge(relation);
                    break;
            }
            umlTopComponent.notifyModified();
        }
        repaint();
    }

    private class FocusAdapter extends ObjectSceneAdapter {

        // Nodes are selected in order for properties to show the sheet from UMLTopComponent ExplorerManager lookup
        // Content is used so that ExplorerTopComponent can synchronize selection through listening to lookup
        @Override
        public void focusChanged(ObjectSceneEvent event, Object previousFocusedObject, Object newFocusedObject) {
            // Deselect the old one
            if (previousFocusedObject != null) {
                content.remove(previousFocusedObject);
            } else {
                System.out.println("Previous focus null!");
//                content.remove(classDiagram);
            }

            // Select the new one
            if (newFocusedObject != null) {
                // For properties
                if (newFocusedObject instanceof ClassDiagram) {
                    selectBackgrundSceneNode();
                } else if (newFocusedObject instanceof ComponentBase) {
                    selectBackgroundComponentNode((ComponentBase) newFocusedObject);
                } else if (newFocusedObject instanceof MemberBase) {
                    selectBackgroundMemberNode((MemberBase) newFocusedObject);
                }

                // For selection inside the Explorer
                content.add(newFocusedObject);
            } else {
                deselectAllBackgroundNodes();
            }
        }
    }

    private void selectBackgrundSceneNode() {
        selectBackgroundNode(getExplorerManager().getRootContext());
    }

    private void selectBackgroundComponentNode(ComponentBase component) {
        for (Node cn : getExplorerManager().getRootContext().getChildren().getNodes()) {
            ComponentNode componentNode = (ComponentNode) cn;
            if (componentNode.getComponent() == component) {
                selectBackgroundNode(componentNode);
                return;
            }
        }
    }

    private void selectBackgroundMemberNode(MemberBase member) {
        for (Node cn : getExplorerManager().getRootContext().getChildren().getNodes()) {
            for (Node mn : cn.getChildren().getNodes()) {
                MemberNode memberNode = (MemberNode) mn;
                if (memberNode.getMember() == member) {
                    selectBackgroundNode(memberNode);
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
    private void selectBackgroundNode(Node node) {
        try {
            if (node != null) {
                getExplorerManager().setSelectedNodes(new Node[]{node});
            } else {
                getExplorerManager().setSelectedNodes(new Node[]{});
            }
        } catch (PropertyVetoException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    private void deselectAllBackgroundNodes() {
        selectBackgroundNode(null);
    }
}
