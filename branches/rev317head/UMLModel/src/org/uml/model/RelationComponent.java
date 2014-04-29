package org.uml.model;

import java.io.Serializable;

/**
 * Implementation of relation components that can be added to the class diagram
 * @author "NUGS"
 * @see ClassDiagram
 * @see ClassDiagramComponent
 */
public class RelationComponent implements Serializable {
    
    ClassDiagramComponent source;
    ClassDiagramComponent target;
    private String name;
    
    /**
     * Default constructor without parameters.
     */
    public RelationComponent() {
    }       
    
    /**
     * Constructor with parameters.
     * @param source
     * @param target
     * @param name 
     */
    public RelationComponent( ClassDiagramComponent source, ClassDiagramComponent target, String name) {
        this.source=source;
        this.target=target;
        this.name = name;
    }

    public ClassDiagramComponent getSource() {
        return source;
    }

    public ClassDiagramComponent getTarget() {
        return target;
    }

    public void setSource(ClassDiagramComponent source) {
        this.source = source;
    }

    public void setTarget(ClassDiagramComponent target) {
        this.target = target;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String getXmlRepresentation() {
        return "Subclass should override this method";
    }
    
    
    
    
}
