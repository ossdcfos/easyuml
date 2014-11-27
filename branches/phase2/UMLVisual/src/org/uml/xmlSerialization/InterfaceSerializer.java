/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import org.dom4j.Element;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.InterfaceComponent;

/**
 *
 * @author Stefan
 */
public class InterfaceSerializer implements ClassDiagramComponentSerializer{

    private InterfaceComponent interfaceComponent;
    
    /**
     * Sets the interfaceComponent object that is going to be serialized.
     * @param component represents interfaceComponent object to be serialized.
     */
    @Override
    public void addClassDiagramComponent(ComponentBase component) {
        try{
            interfaceComponent = (InterfaceComponent) component;
        }catch(ClassCastException e) {
            System.out.println("You have tried to cast invalid type to InterfaceComponent!");
            e.printStackTrace();
        }
    }

    /**
     * Serializes interfaceComponent object to XML by translating its fields into parameter node's attributes and subelements.
     * @param node represents the node that will contain serialized interfaceComponent object.
     */
    @Override
    public void serialize(Element node) {
        if (interfaceComponent.getParentPackage() != null) node.addAttribute("package", interfaceComponent.getParentPackage().getName());
        if (interfaceComponent.getName() != null) node.addAttribute("name", interfaceComponent.getName());
        if (interfaceComponent.getVisibility() != null) node.addAttribute("visibility", interfaceComponent.getVisibility().name().toLowerCase());
        Element methods = node.addElement("Methods");
        XmlSerializer serializer = new MethodSerializer(interfaceComponent.getMethods());
        serializer.serialize(methods);
    }
    
}
