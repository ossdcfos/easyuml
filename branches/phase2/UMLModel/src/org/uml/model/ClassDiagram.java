package org.uml.model;

import java.io.Serializable;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.RelationBase;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * UML Class Diagrams which can contain class, interface and enum components,
 * along with relations between them.
 * Implements Serializable because it is needed in file creation wizard.
 *
 * @author Uros
 * @see ComponentBase
 * @see RelationBase
 */
public class ClassDiagram extends ContainerBase<ComponentBase> implements Serializable {

    /**
     * Set of relations between components in the diagram.
     */
    private final HashSet<RelationBase> relations;

    /**
     * Constructs a ClassDiagram object with name set to "UML ClassDiagram"
     * and an empty relations set.
     */
    public ClassDiagram() {
        super("UML Class Diagram");
        this.relations = new HashSet<>();
    }

    /**
     * Adds a component to the diagram by adding it to the container
     * and setting this diagram as component's parent diagram.
     * 
     * @param component that will be added to the ClassDiagram
     */
    public void addComponent(ComponentBase component){
        component.setParentDiagram(this);
        addComponentToContainter(component);
    }

    /**
     * Adds a relation to the diagram if not already present.
     *
     * @param relation that will be added to the ClassDiagram
     */
    public void addRelation(RelationBase relation) {
        relations.add(relation);
        pcs.firePropertyChange("ADD_RELATION", null, relation);
    }

    /**
     * Removes the given relation from the diagram.
     *
     * @param relation to be removed
     */
    public void removeRelation(RelationBase relation) {
        relations.remove(relation);
        pcs.firePropertyChange("REMOVE_RELATION", null, relation);
    }

    /**
     * Removes all relations that have the given component as a source or a target.
     * @param component for which all the relations will be removed
     */
    public void removeRelationsForAComponent(ComponentBase component) {
        List<RelationBase> toRemove = new LinkedList<>();
        for (RelationBase relation : relations) {
            if (relation.getSource().equals(component) || relation.getTarget().equals(component)) {
                toRemove.add(relation);
            }
        }
        for (RelationBase rc : toRemove) {
            removeRelation(rc);
        }
    }

    /**
     * Returns the collection of this diagram's components.
     *
     * @return collection of components
     */
    @SuppressWarnings("unchecked")
    public LinkedHashSet<ComponentBase> getComponents() {
        return new LinkedHashSet<>(components);
    }

    /**
     * Returns the collection of this diagram's relations.
     *
     * @return collection of relations
     */
    public HashSet<RelationBase> getRelations() {
        return relations;
    }
    
    /**
     * Sets the name of the diagram. Fires "name" property change event.
     *
     * @param newName of the diagram
     */
    @Override
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        pcs.firePropertyChange("name", oldName, newName);
    }
}
