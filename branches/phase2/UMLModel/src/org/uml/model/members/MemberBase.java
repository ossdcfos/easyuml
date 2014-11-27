package org.uml.model.members;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import org.uml.model.Visibility;
import org.uml.model.components.ComponentBase;

/**
 * Represents  a class member.
 *
 * @author zoran
 * @see Literal
 * @see Field
 * @see Method
 * @see Constructor
 * @see ComponentBase
 *
 */
public abstract class MemberBase {

    private String name;
    
    /*
     * Modifier is a int value representing access and non-access modifier in
     * Java e.g. public is represented as 0x00000001, static as 0x00000008.
     * 
     * @see java.lang.reflect.Modifier
     */
    protected int modifiers;
    //private String modifiers; //modifiers are implemented as Strings, it is possible to later be changed to enum
    private transient ComponentBase declaringClass;
    protected Visibility visibility;
    private transient List<PropertyChangeListener> listeners = Collections.synchronizedList(new LinkedList());

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        listeners.add(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        listeners.remove(pcl);
    }

    protected void fire(String propertyName, Object old, Object nue) {
        for (PropertyChangeListener pcl : listeners) {
            pcl.propertyChange(new PropertyChangeEvent(this, propertyName, old, nue));
        }
    }

    /**
     * Default constructor. Only sets the name of a Member.
     *
     * @param name of a member
     */
    public MemberBase(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String newName) {
        name = newName;
    }

    // used from Property sheet
    public void changeName(String newName) {
        String oldName = name;
        name = newName;
        declaringClass.notifyMemberNameChanged(this, oldName);
        fire("name", oldName, newName);
    }

    public int getModifiers() {
        return modifiers;
    }

    public void setModifiers(int modifier) {
        this.modifiers = modifier;
    }

    public ComponentBase getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(ComponentBase declaringClass) {
        this.declaringClass = declaringClass;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        Visibility oldValue = this.visibility;
        this.visibility = visibility;
        fire("visibility", oldValue, this.visibility);
    }

//    @Override
//    public int hashCode() {
//        int hash = 7;
//        hash = 41 * hash + Objects.hashCode(this.name);
//        hash = 41 * hash + Objects.hashCode(this.declaringClass);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) return false;
//        if (getClass() != obj.getClass()) return false;
//        final MemberBase other = (MemberBase) obj;
//        if (!Objects.equals(this.name, other.name)) return false;
//        if (!Objects.equals(this.declaringClass, other.declaringClass))
//            return false;
//        return true;
//    }
    
}
