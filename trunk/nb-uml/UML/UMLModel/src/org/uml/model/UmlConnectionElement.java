/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

/**
 *
 * @author Uros
 */
public class UmlConnectionElement {
    
    public ClassComponent fromClass;
    public ClassComponent toClass;
    public String name;

    public UmlConnectionElement(ClassComponent fromClass, ClassComponent toClass) {
        this.fromClass = fromClass;
        this.toClass = toClass;
    }

    public ClassComponent getFromClass() {
        return fromClass;
    }

    public void setFromClass(ClassComponent fromClass) {
        this.fromClass = fromClass;
    }

    public ClassComponent getToClass() {
        return toClass;
    }

    public void setToClass(ClassComponent toClass) {
        this.toClass = toClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
