/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

import java.util.HashMap;

/**
 *
 * @author Uros
 */
public class UmlClassElement {
    
        private HashMap<String,Field> fields;
        private HashMap<String,Method> methods;
        private String name;

    public UmlClassElement() {
        
        fields= new HashMap<String, Field>();
        methods= new HashMap<String, Method>();
    }

    public UmlClassElement(String name) {
        this.name = name;
    }

    public HashMap<String, Field> getFields() {
        return fields;
    }

    public HashMap<String, Method> getMethods() {
        return methods;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Field getAttribute (String name) {
        return fields.get(name);
    }
    
    public void addAttribute (Field attribute) {
        fields.put(attribute.getName(), attribute);
    } 
    
    public void removeAttribute (String name) {
        fields.remove(name);
    }
    
     public Method getMethod(String name) {
        return methods.get(name);
    }    
    
    public void addMethod(Method method) {
        methods.put(method.getName(), method);
    }
    
    public void removeMethod(String name) {
        methods.remove(name);
    }
                      
}
