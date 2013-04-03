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

    int compCounter=1;

    public ClassDiagram() {

        this.components=new HashMap<String, ClassDiagramComponent>();
        this.relations= new HashMap<String, RelationComponent>();
    }

    public void addComponent(ClassDiagramComponent component) {
        if (nameExists(component.getName())) {
            component.setName(component.getName() + compCounter);
            compCounter++;
            
        }

        component.setParentDiagram(this);
        components.put(component.getName(), component);
        
        System.out.println("Komponente:");
        for (ClassDiagramComponent classDiagramComponent : components.values()) {
            System.out.println(classDiagramComponent.getName());
        }
    }
    
    public void addRelation (RelationComponent relationComponent){
        relations.put(relationComponent.getName(), relationComponent);
        System.out.println(relations.toString());
    }

    public void removeComponent(String name) {
        components.remove(name);
    }
    
    public void removeRelation (String name) {
        relations.remove(name);
    }

    public HashMap<String, ClassDiagramComponent> getComponents() {
        return components;
    }

    public HashMap<String, RelationComponent> getRelations() {
        return relations;
    }

    public void setRelations(HashMap<String, RelationComponent> relations) {
        this.relations = relations;
    }
    
    

    public boolean nameExists(String name) {
        return components.containsKey(name);
    }

    public void componentNameChanged(ClassDiagramComponent comp, String oldName) {
        components.remove(oldName);
        addComponent(comp);
    }
}
