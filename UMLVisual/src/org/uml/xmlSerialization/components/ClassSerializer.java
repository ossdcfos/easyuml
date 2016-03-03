package org.uml.xmlSerialization.components;

import org.dom4j.Element;
import org.openide.util.Exceptions;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.xmlSerialization.members.ConstructorSerializer;
import org.uml.xmlSerialization.members.FieldSerializer;
import org.uml.xmlSerialization.members.MethodSerializer;
import org.uml.xmlSerialization.XmlSerializer;

/**
 *
 * @author Stefan
 */
public class ClassSerializer implements ComponentSerializer {

    private ClassComponent classComponent;

    /**
     * Sets the ClassComponent object that is going to be serialized.
     *
     * @param component represents ClassComponent object to be serialized.
     */
    @Override
    public void setClassDiagramComponent(ComponentBase component) {
        try {
            classComponent = (ClassComponent) component;
        } catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to ClassComponent!");
            Exceptions.printStackTrace(e);
        }
    }

    /**
     * Serializes classComponent to XML by translating its fields into parameter node's attributes and subelements.
     *
     * @param node represents the node that will contains serialized classComponent object.
     */
    @Override
    public void serialize(Element node) {
        if (classComponent.getName() != null) node.addAttribute("name", classComponent.getName());
        if (classComponent.getParentPackage() != null) node.addAttribute("package", classComponent.getParentPackage());
        if (classComponent.getVisibility() != null) node.addAttribute("visibility", classComponent.getVisibility().name().toLowerCase());
        if (classComponent.isAbstract()) node.addAttribute("isAbstract", Boolean.toString(classComponent.isAbstract()));
        if (classComponent.isStatic()) node.addAttribute("isStatic", Boolean.toString(classComponent.isStatic()));
        if (classComponent.isFinal()) node.addAttribute("isFinal", Boolean.toString(classComponent.isFinal()));

        Element fields = node.addElement("Fields");
        XmlSerializer serializer = new FieldSerializer(classComponent.getFields());
        serializer.serialize(fields);

        Element constructors = node.addElement("Constructors");
        serializer = new ConstructorSerializer(classComponent.getConstructors());
        serializer.serialize(constructors);

        Element methods = node.addElement("Methods");
        serializer = new MethodSerializer(classComponent.getMethods());
        serializer.serialize(methods);
    }
}
