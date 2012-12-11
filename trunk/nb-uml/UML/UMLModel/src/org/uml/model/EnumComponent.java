package org.uml.model;

import java.util.HashMap;

/**
 *
 * @author zoran
 */
public class EnumComponent extends ClassDiagramComponent {
    private HashMap<String,java.lang.reflect.Field> fields;
    private HashMap<String,java.lang.reflect.Method> methods;
    
    public EnumComponent() {
        this.setName("UntitledEnum");
        fields= new HashMap<String, java.lang.reflect.Field>();
        methods= new HashMap<String, java.lang.reflect.Method>();
    }

    public EnumComponent(String name) {
        super(name);
    }

    public HashMap<String, java.lang.reflect.Field> getFields() {
        return fields;
    }

    public HashMap<String, java.lang.reflect.Method> getMethods() {
        return methods;
    }


    
    public java.lang.reflect.Field getField (String name) {
        return fields.get(name);
    }
    
    public void addField (java.lang.reflect.Field field) {
        fields.put(field.getName(), field);
        addMember(field);
    } 
    
    public void removeField (String name) {
        fields.remove(name);
    }
    
     public java.lang.reflect.Method getMethod(String name) {
        return methods.get(name);
    }    
    
    public void addMethod(java.lang.reflect.Method method) {
        methods.put(method.getName(), method);
        addMember(method);
    }
    
    public void removeMethod(String name) {
        methods.remove(name);
    }
}
