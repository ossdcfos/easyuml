package org.uml.model.components;

import java.util.HashMap;
import java.util.Map;
import org.uml.model.members.MemberBase;

/**
 * UML class diagram packages, used to describe packages
 * inside projects. Is one of four possible members.
 *
 * @author zoran
 * @see ComponentBase
 * @see ClassComponent
 * @see EnumComponent
 * @see InterfaceComponent
 */
public class PackageComponent extends ComponentBase {

    private HashMap<String, ComponentBase> classDiagramComponents;

    /**
     * Default constructor only specifying parent diagram. Sets the name to default value.
     * @param parentDiagram
     */
    public PackageComponent() {
        this("UntitledPackage");
    }

    /**
     * Constructor with parameter which sets PackageComponent's name and
     * instantiates classDiagramComponents collection.
     *
     * @param parentDiagram
     * @param name of PackageComponent
     */
    public PackageComponent(String name) {
        super(name);
        classDiagramComponents = new HashMap<>();
    }

    public HashMap<String, ComponentBase> getComponents() {
        return classDiagramComponents;
    }

    /**
     * Adds a classDiagramComponent to PackageComponent's classDiagramComponent
     * collection.
     *
     * @param cdc - classDiagramComponent to be added
     */
    public void addComponent(ComponentBase cdc) {
//        if (cdc instanceof ClassComponent) {
//            cdc = (ClassDiagramComponent) cdc;
//        }
        classDiagramComponents.put(cdc.getName(), cdc);
    }

    /**
     * Removes a classDiagramComponent from PackageComponent's
     * classDiagramComponent collection.
     *
     * @param cdc - classDiagramComponent to be removed
     */
    public void removeComponent(ComponentBase cdc) {
        classDiagramComponents.remove(cdc.getName());
    }

    /**
     * Prints all classes that this PackageComponent includes in it into
     * standard output (console, in most cases).
     */
    public void printAllComponentsOnStdOut() {
        System.out.println("PackageComponent: " + this.getName());
        System.out.println("Contains these ClassDiagramComponents: ");
        int counter = 1;
        for (Map.Entry<String, ComponentBase> entry : classDiagramComponents.entrySet()) {
            String string = entry.getKey();
            ComponentBase classComponent = entry.getValue();
            System.out.println("Component no." + counter + ": \t" + classComponent.getName());
            counter++;
        }
    }

    @Override
    public void removeMemberFromContainer(MemberBase member) {
        // TODO: fix this when redoing the model!
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
