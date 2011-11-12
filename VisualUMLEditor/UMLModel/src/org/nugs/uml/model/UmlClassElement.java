package org.nugs.uml.model;

import java.util.HashMap;

/**
 *
 * @author zoran
 */
public class UmlClassElement {
    private HashMap <String, Attribute> attributes;
    private HashMap <String, Method> methods;
    private String name;

    public UmlClassElement() {
        attributes = new HashMap <String, Attribute>();
        methods = new HashMap <String, Method>();
    }

    public UmlClassElement(String name) {
        this();
        this.name = name;
    }

    public HashMap<String, Attribute> getAttributes() {
        return attributes;
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

    public Attribute getAttribute(String name) {
        return attributes.get(name);
    }
    
    public void addAttribute(Attribute attribute) {
        attributes.put(attribute.getName(), attribute);
    }
    
    public void removeAttribute(String name) {
        attributes.remove(name);
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
