package org.uml.xmlSerialization.components;

import org.dom4j.Element;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.InterfaceComponent;
import org.uml.xmlSerialization.members.MethodSerializer;
import org.uml.xmlSerialization.XmlSerializer;

/**
 *
 * @author Stefan
 */
public class InterfaceSerializer implements ComponentSerializer {

    private InterfaceComponent interfaceComponent;

    /**
     * Sets the interfaceComponent object that is going to be serialized.
     *
     * @param component represents interfaceComponent object to be serialized.
     */
    @Override
    public void setClassDiagramComponent(ComponentBase component) {
        try {
            interfaceComponent = (InterfaceComponent) component;
        } catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to InterfaceComponent!");
            e.printStackTrace();
        }
    }

    /**
     * Serializes interfaceComponent object to XML by translating its fields into parameter node's attributes and subelements.
     *
     * @param node represents the node that will contain serialized interfaceComponent object.
     */
    @Override
    public void serialize(Element node) {
        if (interfaceComponent.getName() != null) node.addAttribute("name", interfaceComponent.getName());
        if (interfaceComponent.getParentPackage() != null) node.addAttribute("package", interfaceComponent.getParentPackage());
        if (interfaceComponent.getVisibility() != null) node.addAttribute("visibility", interfaceComponent.getVisibility().name().toLowerCase());
        if (interfaceComponent.isStatic()) node.addAttribute("isStatic", Boolean.toString(interfaceComponent.isStatic()));

        Element methods = node.addElement("Methods");
        XmlSerializer serializer = new MethodSerializer(interfaceComponent.getMethods());
        serializer.serialize(methods);
    }
}
