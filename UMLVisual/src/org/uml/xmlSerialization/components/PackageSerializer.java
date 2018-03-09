package org.uml.xmlSerialization.components;

import org.dom4j.Element;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.PackageComponent;

/**
 *
 * @author Stefan
 */
public class PackageSerializer implements ComponentSerializer {

    private PackageComponent packageComponent;

    /**
     * Sets the packageComponent object that is going to be serialized.
     *
     * @param component represents enumComponent object to be serialized.
     */
    @Override
    public void setClassDiagramComponent(ComponentBase component) {
        try {
            packageComponent = (PackageComponent) component;
        } catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to PackageComponent!");
            e.printStackTrace();
        }
    }

    /**
     * Serializes - object to XML by translating its fields into parameter node's attributes and subelements.
     *
     * @param node represents the node that will contain serialized - object.
     */
    @Override
    public void serialize(Element node) {
        if (packageComponent.getName() != null) node.addAttribute("name", packageComponent.getName());
        if (packageComponent.getParentPackage() != null) node.addAttribute("package", packageComponent.getParentPackage());
        if (packageComponent.getVisibility() != null) node.addAttribute("visibility", packageComponent.getVisibility().name().toLowerCase());
        if (packageComponent.getGeneration()!= null) node.addAttribute("generation", packageComponent.getGeneration().toString());
    }
}
