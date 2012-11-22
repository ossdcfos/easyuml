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
public class UmlClassDiagram {
    
    private HashMap<String, ClassDiagramComponent> classes;
    private HashMap<String, RelationComponent> connections;

    public UmlClassDiagram() {
        
        this.classes=new HashMap<String, ClassDiagramComponent>();
    }

    public HashMap<String, ClassDiagramComponent> getClasses() {
        return classes;
    }

    public void setClasses(HashMap<String, ClassDiagramComponent> classes) {
        this.classes = classes;
    }
    
    public void remove (String name) {
        classes.remove(name);
    }
    
     
}
