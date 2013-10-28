package org.uml.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Class that provides implementation of UML Class Diagrams
 * @author Uros
 * @version 1.0
 * @see ClassDiagramComponent
 * @see RelationComponent
 */

public class ClassDiagram implements Serializable {
    
    private String name;
    private HashMap<String, ClassDiagramComponent> components; // contains classes, interfaces or enums
    private HashMap<String, RelationComponent> relations;

    //compoments counter:
    int compCounter=1;

     public ClassDiagram() {
        name = "UML Class Diagram";
        this.components=new HashMap<String, ClassDiagramComponent>();
        this.relations= new HashMap<String, RelationComponent>();
    }
    
    /**
     * Method that adds new ClassDiagramComponent into collection of existing components.
     * If component with that name already exists in this collection, new one will be added
     * with it's name concatenated with current component counter number.
     * <font size="6" color = "red">Should all of this methods return a boolean which represents if requested operation was successful?</font>
     * @param component to be added to collection
     */
    
    
    public void addComponent(ClassDiagramComponent component) {
        if (nameExists(component.getName())) {
            component.setName(component.getName() + compCounter);
            compCounter++;
        }

        component.setParentDiagram(this);
        components.put(component.getName(), component);
    }
    
    
    /** 
     * Method that add new RealtionComponent into collection of existing relations.
     * @param relationComponent that will be added to the collection of relations
     */
    public void addRelation (RelationComponent relationComponent){
        relations.put(relationComponent.getName(), relationComponent);
        System.out.println(relations.toString());
    }

    
    /**
     * Removes component from collection.
     * @param name of the component
     */
    public void removeComponent(String name) {
        components.remove(name);
    }
    
    
    /**
     * Removes relation from collection.
     * @param name of the relation
     */
    public void removeRelation (String name) {
        relations.remove(name);
    }

    /**
     * Getter for components collection
     * @return collection of components
     */
    public HashMap<String, ClassDiagramComponent> getComponents() {
        return components;
    }

    /**
     * Getter for relations collection
     * @return collection of relations
     */
    public HashMap<String, RelationComponent> getRelations() {
        return relations;
    }

    /**
     * Setter for relations collection 
     * @param relations 
     */
    public void setRelations(HashMap<String, RelationComponent> relations) {
        this.relations = relations;
    }
    
    /**
     * Checks if component with provided name exists in collection.
     * @param name of the component
     * @return if that component exists in collection
     */
    public boolean nameExists(String name) {
        return components.containsKey(name);
    }

    /**
     * Changes name of the component in collection. 
     * @param comp - component which will replace old one
     * @param oldName - old component's name
     */
    public void componentNameChanged(ClassDiagramComponent comp, String oldName) {
        components.remove(oldName);
        addComponent(comp);
    }

    /**
     * Getter for name of the ClassDiagram
     * @return name of the UML Class Diagram
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name of the ClassDiagram
     * @param name of UML Class Diagram
     */
    public void setName(String name) {
        this.name = name;
    }
    
    
}
