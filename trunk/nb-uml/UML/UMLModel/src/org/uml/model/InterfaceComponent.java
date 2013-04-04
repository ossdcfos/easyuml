package org.uml.model;

import java.util.HashMap;

/**
 *
 * @author zoran
 */
public class InterfaceComponent  extends ClassDiagramComponent {
    private HashMap<String, Method> methods;

    public InterfaceComponent() {
        this.setName("UntitledInterface");
        methods= new HashMap<String, Method>();
    }
    public InterfaceComponent(String name) {
        super(name);
    }

    public HashMap<String, Method> getMethods() {
        return methods;
    }
    
     public Method getMethod(String name) {
        return methods.get(name);
    }    
    
    public void addMethod(Method method) {
        methods.put(method.getName(), method);
        addMember(method);
    }
    
    public void removeMethod(String name) {
        methods.remove(name);
    }
}
