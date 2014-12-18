package org.uml.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

/**
 *
 * @author netlink
 * @param <T>
 */
public abstract class ContainerBase<T extends INameable & IHasSignature> implements INameable, IHasSignature {

    protected String name;
    protected LinkedHashSet<T> containerComponents; // contains classes, interfaces or enums
    protected transient List<IComponentDeleteListener> deleteListeners = new ArrayList<>();
    
    protected transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }

//    protected void fire(String propertyName, Object old, Object nue) {
//        for (PropertyChangeListener pcl : listeners) {
//            pcl.propertyChange(new PropertyChangeEvent(this, propertyName, old, nue));
//        }
//    }

    public ContainerBase(String name) {
        this.name = name;
        this.containerComponents = new LinkedHashSet<>();
    }

    public void addDeleteListener(IComponentDeleteListener icdl) {
        deleteListeners.add(icdl);
    }

    /**
     * Adds new ClassDiagramComponent into collection of existing components. If
     * component with that name already exists in this collection, new one will
     * be added with it's name concatenated with current component counter
     * number.
     *
     * @param component to be added to collection
     */
    public void addComponent(T component) {
        String componentName = component.getName();
        int suffix = 1;
        while (signatureExists(component.toString())) {
            component.setName(componentName + suffix);
            suffix++;
        }
        containerComponents.add(component);
        pcs.firePropertyChange("ADD", null, component);
    }

    /**
     * Removes the given component from this diagram's collection of components.
     *
     * @param component to be removed
     */
    public void removeComponent(T component) {
        for (IComponentDeleteListener icdl : deleteListeners) {
            icdl.componentDeleted(component);
        }
        containerComponents.remove(component);
        pcs.firePropertyChange("REMOVE", null, component);
    }

    /**
     * Checks if component with provided name exists in collection of
     * components.
     *
     * @param componentString of the component
     * @return if that component exists in collection
     */
    public boolean signatureExists(String componentString) {
        for (T component : containerComponents) {
            if(component.getSignature().equals(componentString)) return true;
        }
        return false;
    }

    /**
     * Returns name of this Container
     *
     * @return name of the Container
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Sets the name of this Container
     *
     * @param newName of Container
     */
    @Override
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        pcs.firePropertyChange("name", oldName, newName);
    }

    // temporary solution, add package
    @Override
    public String getSignature(){
        return getName();
    }

}
