package org.uml.model.components;

import java.util.LinkedHashSet;
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
// TODO use or delete
public class PackageComponent extends ComponentBase {

    private LinkedHashSet<ComponentBase> classDiagramComponents;

    /**
     * Default constructor only specifying parent diagram. Sets the name to default value.
     */
    public PackageComponent() {
        this("UntitledPackage");
    }

    /**
     * Constructor with parameter which sets PackageComponent's name and
     * instantiates classDiagramComponents collection.
     *
     * @param name of PackageComponent
     */
    public PackageComponent(String name) {
        super(name);
        classDiagramComponents = new LinkedHashSet<>();
    }

    public LinkedHashSet<ComponentBase> getComponents() {
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
        classDiagramComponents.add(cdc);
    }

    /**
     * Removes a classDiagramComponent from PackageComponent's
     * classDiagramComponent collection.
     *
     * @param cdc - classDiagramComponent to be removed
     */
    public void removeComponent(ComponentBase cdc) {
        classDiagramComponents.remove(cdc);
    }

    /**
     * Prints all classes that this PackageComponent includes in it into
     * standard output (console, in most cases).
     */
    public void printAllComponentsOnStdOut() {
        System.out.println("PackageComponent: " + this.getName());
        System.out.println("Contains these ClassDiagramComponents: ");
        int counter = 1;
        for (ComponentBase component : classDiagramComponents) {
            System.out.println("Component no." + counter + ": \t" + component.getName());
            counter++;
        }
    }

    @Override
    public void removeMember(MemberBase member) {
        // TODO: fix this when redoing the model!
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getName() {
        return name;
    }
}
