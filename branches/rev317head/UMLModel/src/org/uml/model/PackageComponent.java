/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of InterfaceComponent, one of four posible types of
 * ClassDiagramComponents.
 *
 * @author zoran
 * @see ClassDiagramComponent
 * @see ClassComponent
 * @see EnumComponent
 * @see InterfaceComponent
 */
public class PackageComponent extends ClassDiagramComponent {

    private HashMap<String, ClassDiagramComponent> classDiagramComponent;

    /**
     * Contructor without parameters which sets PackageComponent's name on
     * "UntitledPackage"
     */
    public PackageComponent() {
        this.setName("UntitledPackage");
    }

    /**
     * Constructor with parameter which sets PackageComponent's name
     *
     * @param name of PackageComponent
     */
    public PackageComponent(String name) {
        super(name);
        classDiagramComponent = new HashMap<String, ClassDiagramComponent>();
    }

    public HashMap<String, ClassDiagramComponent> getComponents() {
        return classDiagramComponent;
    }

    public void addComponent(ClassDiagramComponent cdc) {
//        if (cdc instanceof ClassComponent) {
//            cdc = (ClassDiagramComponent) cdc;
//        }
        classDiagramComponent.put(cdc.getName(), cdc);
    }

    public void removeComponent(ClassDiagramComponent cdc) {
        classDiagramComponent.remove(cdc.getName());
    }

    public void printAllComponentsOnStdOut() {
        System.out.println("PackageComponent: " + this.getName());
        System.out.println("Contains these ClassDiagramComponents: ");
        int counter = 1;
        for (Map.Entry<String, ClassDiagramComponent> entry : classDiagramComponent.entrySet()) {
            String string = entry.getKey();
            ClassDiagramComponent classComponent = entry.getValue();
            System.out.println("Component no." + counter + ": \t" + classComponent.getName());
            counter++;
        }
    }
}
