package org.uml.xmlSerialization.components;

import org.dom4j.Element;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.EnumComponent;
import org.uml.xmlSerialization.members.LiteralSerializer;
import org.uml.xmlSerialization.XmlSerializer;

/**
 *
 * @author Stefan
 */
public class EnumSerializer implements ComponentSerializer {

    private EnumComponent enumComponent;

    /**
     * Sets the enumComponent object that is going to be serialized.
     *
     * @param component represents enumComponent object to be serialized.
     */
    @Override
    public void setClassDiagramComponent(ComponentBase component) {
        try {
            enumComponent = (EnumComponent) component;
        } catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to ClassComponent!");
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
        if (enumComponent.getName() != null) node.addAttribute("name", enumComponent.getName());
        if (enumComponent.getParentPackage() != null) node.addAttribute("package", enumComponent.getParentPackage());
        if (enumComponent.getVisibility() != null) node.addAttribute("visibility", enumComponent.getVisibility().name().toLowerCase());
        if (enumComponent.getGeneration()!= null) node.addAttribute("generation", enumComponent.getGeneration().toString());

        Element literals = node.addElement("Literals");
        XmlSerializer serializer = new LiteralSerializer(enumComponent.getLiterals());
        serializer.serialize(literals);
    }
}
