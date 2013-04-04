package org.uml.model;

import java.util.HashMap;

/**
 *
 * @author zoran
 */
public class EnumComponent extends ClassDiagramComponent {
    private HashMap<String, Field> fields;
    private HashMap<String, Method> methods;
    
    public EnumComponent() {
        this.setName("UntitledEnum");
        fields= new HashMap<String, Field>();
        methods= new HashMap<String, Method>();
    }

    public EnumComponent(String name) {
        super(name);
    }

    public HashMap<String, Field> getFields() {
        return fields;
    }

    public HashMap<String, Method> getMethods() {
        return methods;
    }


    
    public Field getField (String name) {
        return fields.get(name);
    }
    
    public void addField (Field field) {
        fields.put(field.getName(), field);
        addMember(field);
    } 
    
    public void removeField (String name) {
        fields.remove(name);
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
