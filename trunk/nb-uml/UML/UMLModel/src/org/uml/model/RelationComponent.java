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

    public RelationComponent() {
    }       
    
    public RelationComponent( ClassDiagramComponent source, ClassDiagramComponent target) {
        this.source=source;
        this.target=target;
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
    
    
    
    
    
    
}
