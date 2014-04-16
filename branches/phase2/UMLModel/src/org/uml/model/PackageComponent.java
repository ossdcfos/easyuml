/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

/**
 * Implementation of InterfaceComponent, one of four posible types of ClassDiagramComponents.
 * @author zoran
 * @see ClassDiagramComponent
 * @see ClassComponent
 * @see EnumComponent
 * @see InterfaceComponent
 */
public class PackageComponent extends ClassDiagramComponent{

    /**
     * Contructor without parameters which sets PackageComponent's name on "UntitledPackage"
     */
    public PackageComponent() {
        this.setName("UntitledPackage");
    }

    /**
     * Constructor with parameter which sets PackageComponent's name
     * @param name of PackageComponent
     */
    public PackageComponent(String name) {
        super(name);
    }
    
    
    
    
}
