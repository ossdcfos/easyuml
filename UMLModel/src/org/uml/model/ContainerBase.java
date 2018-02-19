package org.uml.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * Abstract class representing a container which can contain some components.
 * Examples/uses: 1) ClassDiagram is a container, with ComponentBase objects as contained components
 *                2) ComponentBase is a container, with MemberBase objects as contained components.
 * Parts implement INameable, as they need to be renamed in case of an existing signature (see link). 
 * Parts implement IHasSignature, as they need to be uniquely identified.
 * 
 * @see signatureExists(String componentSignature)
 * @author Boris Perović
 * @param <T> type of contained components.
 */
public abstract class ContainerBase<T extends INameable & IHasSignature> implements INameable {

    /**
     * Name of the container.
     */
    protected String name;
    /**
     * Components contained in the container.
     */
    protected LinkedHashSet<T> components = new LinkedHashSet<>();
        
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
    public void addPropertyChangeListenerIfNotExisting(PropertyChangeListener listener){
        if(!listenerExists(listener)){
            addPropertyChangeListener(listener);
        }
    }
    private boolean listenerExists(PropertyChangeListener listener) {
        return Arrays.asList(pcs.getPropertyChangeListeners()).contains(listener);
    }
    
    /**
     * Constructs a new empty container, with the specified name.
     * 
     * @param name 
     */
    public ContainerBase(String name) {
        this.name = name;
    }

    /**
     * Adds a component into the container. If a component with the same signature 
     * already exists in the container, a smallest possible integer will be appended 
     * to component's name, such that component's signature becomes unique.
     * Fires "ADD_COMPONENT" property change event.
     *
     * @param component to be added to the container
     */
    protected void addComponentToContainter(T component) {
        String componentName = component.getName();
        int suffix = 1;
        while (signatureExists(component.getSignature())) {
            component.setName(componentName + suffix);
            suffix++;
        }
        components.add(component);
        pcs.firePropertyChange("ADD_COMPONENT", null, component);
    }
    
    public void updateComponentOrder(List<T> list) {
        components.clear();
        for (T component : list) {
            System.out.println(component.getName());
            components.add(component);
        }
    }

    /**
     * Removes the given component from the container.
     * Fires "REMOVE_COMPONENT" property change event.
     * 
     * @param component to be removed from the container
     */
    public void removeComponentFromContainer(T component) {
        components.remove(component);
        pcs.firePropertyChange("REMOVE_COMPONENT", null, component);
    }
    
    /**
     * Remove all components from the container
     * 
     * Does not fire any event
     */
    public void removeAllComponentsFromContainer() {
        components.clear();
    }

    /**
     * Checks if the component with the provided signature exists 
     * in the container.
     *
     * @param componentSignature of the component
     * @return if that component exists in the container
     */
    public boolean signatureExists(String componentSignature) {
        for (T component : components) {
            if(component.getSignature().equals(componentSignature)) return true;
        }
        return false;
    }

    /**
     * Returns the name of this Container.
     *
     * @return name of the Container
     */
    @Override
    public String getName() {
        return name;
    }
}
