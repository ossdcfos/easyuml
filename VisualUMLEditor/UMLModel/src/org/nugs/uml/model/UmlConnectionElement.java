/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nugs.uml.model;

/**
 *
 * @author zoran
 */
public class UmlConnectionElement {
    public UmlClassElement fromClass;
    public UmlClassElement toClass;
    public String name;

    public UmlConnectionElement(UmlClassElement fromClass, UmlClassElement toClass) {
        this.fromClass = fromClass;
        this.toClass = toClass;
    }

    public UmlClassElement getFromClass() {
        return fromClass;
    }

    public void setFromClass(UmlClassElement fromClass) {
        this.fromClass = fromClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UmlClassElement getToClass() {
        return toClass;
    }

    public void setToClass(UmlClassElement toClass) {
        this.toClass = toClass;
    }
    
    
    
    
    
}
