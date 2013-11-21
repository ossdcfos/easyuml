package org.uml.model;

import java.util.HashMap;

/**
 * Implementation of InterfaceComponent, one of three posible types of ClassDiagramComponents.
 * @author zoran
 * @see ClassDiagramComponent
 * @see ClassComponent
 * @see EnumComponent
 */
public class InterfaceComponent  extends ClassDiagramComponent {
    private HashMap<String, Method> methods;

    /**
     * Constructor without parameters
     */
    public InterfaceComponent() {
        this.setName("UntitledInterface");
        methods= new HashMap<String, Method>();
    }
    
    /**
     * One parameter constructor
     * @param name of InterfaceComponent
     */
    public InterfaceComponent(String name) {
        super(name);
    }

    public HashMap<String, Method> getMethods() {
        return methods;
    }
    
     public Method getMethod(String name) {
        return methods.get(name);
    }    
    
    /**
     * Adds a Method instance to InterfaceComponent's members collection. 
     * @param method which will be added
     */ 
    public void addMethod(Method method) {
        method.setDeclaringClass(this);
        methods.put(method.getName(), method);
        addMember(method);
    }
    
    /**
     * Removes Method from InterfaceComponent's members collection
     * @param name of the method which will be removed
     */
    public void removeMethod(String name) {
        methods.remove(name);
    }
}
