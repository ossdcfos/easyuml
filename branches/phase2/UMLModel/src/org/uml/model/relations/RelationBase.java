package org.uml.model.relations;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ComponentBase;

/**
 * Relation between two UML Class Diagram components.
 * Currently supports Is, Implements, Has (aggregation, composition, default)
 * and Uses relations using its subclasses.
 *
 * @author NUGS
 * @see IsRelationComponent
 * @see ImplementsRelationComponent
 * @see HasRelationComponent
 * @see UseRelationComponent
 * @see ClassDiagram
 * @see ComponentBase
 */
public abstract class RelationBase implements Serializable {

    /**
     * Source component for this relation.
     */
    ComponentBase source;

    /**
     * Target component for this relation.
     */
    ComponentBase target;

    /**
     * Name of the relation.
     */
    protected String name;

    /**
     * Property change support and related methods.
     */
    protected transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

    /**
     * Default constructor without parameters used to create a new instance of relation object.
     */
    public RelationBase() {
    }

    /**
     * Constructor with three parameters used to create relation object
     * and set its name, source and target.
     *
     * @param source component
     * @param target component
     * @param name of the relation
     * @see ComponentBase
     */
    public RelationBase(ComponentBase source, ComponentBase target, String name) {
        this.source = source;
        this.target = target;
        this.name = name;
    }

    /**
     * Returns source of the relation.
     *
     * @return source of the relation
     */
    public ComponentBase getSource() {
        return source;
    }

    /**
     * Sets source of the relation.
     *
     * @param source to be set
     */
    public void setSource(ComponentBase source) {
        this.source = source;
    }

    /**
     * Returns target of the relation.
     *
     * @return target of the relation
     */
    public ComponentBase getTarget() {
        return target;
    }

    /**
     * Sets target of the relation.
     *
     * @param target to be set
     */
    public void setTarget(ComponentBase target) {
        this.target = target;
    }

    /**
     * Returns the name of the relation.
     *
     * @return name of the relation
     */
    public String getName() {
        return name;
    }

    /**
     * Set the relation name to newName. Fires "name" property change event.
     *
     * @param newName to be set
     */
    public void setName(String newName) {
        String oldName = getName();
        name = newName;
        pcs.firePropertyChange("name", oldName, newName);
    }

    /**
     * Checks if source and target can be connected by this relation.
     * @param source component
     * @param target component
     * @return true if can connect, false otherwise
     */
    public abstract boolean canConnect(ComponentBase source, ComponentBase target);

    /**
     * Hash code of the relation. Used to compare for equality.
     * @return hash code
     */
    @Override
    public abstract int hashCode();
    
    /**
     * Compares obj to this relation. Overriden for specific comparison for equality.
     * @param obj to compare to this
     * @return true if same, false otherwise
     */
    @Override
    public abstract boolean equals(Object obj);
}
