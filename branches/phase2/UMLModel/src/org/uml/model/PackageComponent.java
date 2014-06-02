package org.uml.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Representation of UML class diagram packages, used to describe packages
 * inside projects. Is one of four possible members.
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
     * Constructor without parameters which sets PackageComponent's name to its
     * default value.
     */
    public PackageComponent() {
        this.setName("UntitledPackage");
    }

    /**
     * Constructor with parameter which sets PackageComponent's name and
     * instantiates classDiagramComponent collection.
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

    /**
     * Adds a classDiagramComponent to PackageComponent's classDiagramComponent
     * collection.
     *
     * @param cdc - classDiagramComponent to be added
     */
    public void addComponent(ClassDiagramComponent cdc) {
//        if (cdc instanceof ClassComponent) {
//            cdc = (ClassDiagramComponent) cdc;
//        }
        classDiagramComponent.put(cdc.getName(), cdc);
    }

    /**
     * Removes a classDiagramComponent from PackageComponent's
     * classDiagramComponent collection.
     *
     * @param cdc - classDiagramComponent to be removed
     */
    public void removeComponent(ClassDiagramComponent cdc) {
        classDiagramComponent.remove(cdc.getName());
    }

    /**
     * Prints all classes that this PackageComponent includes in it into
     * standard output (console, in most cases).
     */
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
