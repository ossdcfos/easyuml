/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

/**
 *
 * @author "NUGS"
 */
public class RelationComponent {
    
    ClassDiagramComponent source;
    ClassDiagramComponent target;
    private String name;
    public RelationComponent() {
    }       
    
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
    
    
    
    
    
    
}
