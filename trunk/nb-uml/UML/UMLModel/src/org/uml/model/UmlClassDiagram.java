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
    
    private HashMap<String, UmlClassElement> classes;
    private HashMap<String, UmlConnectionElement> connections;

    public UmlClassDiagram() {
        
        this.classes=new HashMap<String, UmlClassElement>();
    }

    public HashMap<String, UmlClassElement> getClasses() {
        return classes;
    }

    public void setClasses(HashMap<String, UmlClassElement> classes) {
        this.classes = classes;
    }
    
    public void remove (String name) {
        classes.remove(name);
    }
    
     
}
