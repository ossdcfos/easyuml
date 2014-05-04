package org.uml.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Implementation of UML Class Diagrams. Contains collection of
 * ClassDiagramComponents and RelationComponents. This represents the whole
 * UML diagram consisting of classes (+ enumerations, interfaces, and packages)
 * along with their respective relations.
 *
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
    int compCounter = 1;
    private HashMap<String, PackageComponent> packages;
    int relationsCounter = 0;
/**
 * Standard ClassDiagram constructor without arguments.
 * <p>
 * Sets name to default value and instantiates components, relations and packages
 * hash maps.
 */
    public ClassDiagram() {
        name = "UML Class Diagram";
        this.components = new HashMap<String, ClassDiagramComponent>();
        this.relations = new HashMap<String, RelationComponent>();
        this.packages = new HashMap<String, PackageComponent>();
    }

    /**
     * Adds new ClassDiagramComponent into collection of existing components. If
     * component with that name already exists in this collection, new one will
     * be added with it's name concatenated with current component counter
     * number.
     *
     * @param component to be added to collection
     */
    /**
     * Adds new ClassDiagramComponent into collection of existing components.If
     * component with that name already exists in this collection, new one will
     * be added with it's name concatenated with current component counter
     * number.
     *
     * @param component to be added to collection
     */
    public void addComponent(ClassDiagramComponent component) {
        if (nameExists(component.getName())) {
            component.setName(component.getName() + compCounter);
        }
        compCounter++;
        component.setParentDiagram(this);
        components.put(component.getName(), component);
    }

    /**
     * Adds new RealtionComponent into collection of existing relations. If 
     * relationComponent with same name already exists in relations, a counter
     * is concatenated to its name and is then added. The counter is then
     * incremented by one. 
     *
     * @param relationComponent that will be added to the collection of
     * relations
     */
    public void addRelation(RelationComponent relationComponent) {
        if (relations.containsKey(relationComponent.getName())) {
            relations.put(relationComponent.getName() + relationsCounter, relationComponent);
            relationsCounter++;
        }else {
            relations.put(relationComponent.getName(), relationComponent);
        }
        System.out.println(relations.toString());
    }

    /**
     * Removes component from collection of components.
     *
     * @param name of the component
     */
    public void removeComponent(String name) {
        components.remove(name);
    }

    /**
     * Removes relation from collection of relations.
     *
     * @param name of the relation
     */
    public void removeRelation(String name) {
        relations.remove(name);
    }

    /**
     * Getter for collection of components.
     *
     * @return collection of components
     */
    public HashMap<String, ClassDiagramComponent> getComponents() {
        return components;
    }

    /**
     * Getter for relations collection.
     *
     * @return collection of relations
     */
    public HashMap<String, RelationComponent> getRelations() {
        return relations;
    }

    /**
     * Setter for relations collection.
     *
     * @param relations
     */
    public void setRelations(HashMap<String, RelationComponent> relations) {
        this.relations = relations;
    }

    /**
     * Checks if component with provided name exists in collection of
     * components.
     *
     * @param name of the component
     * @return if that component exists in collection
     */
    public boolean nameExists(String name) {
        return components.containsKey(name);
    }

    /**
     * Removes component from collection of components and adds same component
     * with new name into that collection.
     *
     * @param comp - component whose name will be changed
     * @param oldName - old component's name
     */
    public void componentNameChanged(ClassDiagramComponent comp, String oldName) {
        components.remove(oldName);
        addComponent(comp);
    }

    /**
     * Getter for name of the ClassDiagram
     *
     * @return name of the UML Class Diagram
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name of the ClassDiagram
     *
     * @param name of UML Class Diagram
     */
    public void setName(String name) {
        this.name = name;
    }
/**
 * Adds a PackageComponent into package collection.
 * 
 * @param pc package to be added
 */
    public void addPackage(PackageComponent pc) {
        packages.put(pc.getName(), pc);
    }
/**
 * Removes package from package collection.
 * 
 * @param pc package to be removed
 */
    public void removePackage(PackageComponent pc) {
        packages.remove(pc.getName());
    }
/**
 * Gets packages collection of ClassDiagram.
 * 
 * @return packages collection
 */
    public HashMap<String, PackageComponent> getPackages() {
        return packages;
    }
}
