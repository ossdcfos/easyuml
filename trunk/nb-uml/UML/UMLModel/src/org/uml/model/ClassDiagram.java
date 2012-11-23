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
public class ClassDiagram {
    
    private HashMap<String, ClassDiagramComponent> components; // classes, interfaces or enums
    private HashMap<String, RelationComponent> relations;

    public ClassDiagram() {
        
        this.components=new HashMap<String, ClassDiagramComponent>();
    }
    
    public void addComponent(ClassDiagramComponent component) {
        if (components.containsKey(component.getName())) {
            throw new RuntimeException("Component with same name allready exists!");
        }
        
        components.put(component.getName(), component);
    }
    
    public void removeComponent(String name) {
        components.remove(name);
    }

    public HashMap<String, ClassDiagramComponent> getComponents() {
        return components;
    }

    
    public void remove (String name) {
        components.remove(name);
    }
    
     
}
