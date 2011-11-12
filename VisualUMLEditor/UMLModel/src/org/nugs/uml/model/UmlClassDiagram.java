package org.nugs.uml.model;

import java.util.HashMap;

/**
 *
 * @author zoran
 */
public class UmlClassDiagram {
    private HashMap<String, UmlClassElement> classes;
    private HashMap<String, UmlConnectionElement> connections;

    public UmlClassDiagram() {
        this.classes = new HashMap<String, UmlClassElement>();
    }
    
    public void addClass(UmlClassElement umlClass) {
        classes.put(umlClass.getName(), umlClass);
    }
    
    public UmlClassElement getClass(String name) {
        return classes.get(name);
    }
    
    public void remove(String name) {
        classes.remove(name);
    }
    
    
    
}
