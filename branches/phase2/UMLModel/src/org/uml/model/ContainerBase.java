package org.uml.model;

//import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
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

//    @XStreamAsAttribute
    protected String name;
    
    protected LinkedHashSet<T> parts; // contains components or members
    protected transient List<IComponentDeleteListener> deleteListeners = new ArrayList<>();
    
    protected transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean listenerTypeExists(Class clazz){
        for(PropertyChangeListener pcl : pcs.getPropertyChangeListeners()){
            if(clazz.isAssignableFrom(pcl.getClass())) return true;
        }
        return false;
    }
    
    public ContainerBase(String name) {
        this.name = name;
        this.parts = new LinkedHashSet<>();
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
     * @param part to be added to collection
     */
    public void addPartToContainter(T part) {
        String componentName = part.getName();
        int suffix = 1;
        while (signatureExists(part.getSignature())) {
            part.setName(componentName + suffix);
            suffix++;
        }
        parts.add(part);
        pcs.firePropertyChange("ADD", null, part);
    }

    /**
     * Removes the given component from this diagram's collection of components.
     *
     * @param part to be removed
     */
    public void removePartFromContainer(T part) {
        for (IComponentDeleteListener icdl : deleteListeners) {
            icdl.componentDeleted(part);
        }
        parts.remove(part);
        pcs.firePropertyChange("REMOVE", null, part);
    }

    /**
     * Checks if component with provided name exists in collection of
     * components.
     *
     * @param componentString of the component
     * @return if that component exists in collection
     */
    public boolean signatureExists(String componentString) {
        for (T component : parts) {
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

    // TODO temporary solution, add package
    @Override
    public String getSignature(){
        return getName();
    }
}
