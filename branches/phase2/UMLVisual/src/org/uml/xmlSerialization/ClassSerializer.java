package org.uml.xmlSerialization;

import org.dom4j.Element;
import org.openide.util.Exceptions;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;

/**
 *
 * @author Stefan
 */
public class ClassSerializer implements ClassDiagramComponentSerializer{

    private ClassComponent classComponent;
    
    /**
     * Sets the ClassComponent object that is going to be serialized.
     * @param component represents ClassComponent object to be serialized.
     */
    @Override
    public void addClassDiagramComponent(ComponentBase component) {
        try{
            classComponent = (ClassComponent) component;
        }catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to ClassComponent!");
            Exceptions.printStackTrace(e);
        }
    }

    /**
     * Serializes classComponent to XML by translating its fields into parameter node's attributes and subelements.
     * @param node represents the node that will contains serialized classComponent object.
     */
    @Override
    public void serialize(Element node) {        
        if (classComponent.getParentPackage() != null) node.addAttribute("package", classComponent.getParentPackage().getName());
        if (classComponent.isAbstract()) node.addAttribute("isAbstract", Boolean.toString(true));
        if (classComponent.getName() != null) node.addAttribute("name", classComponent.getName());
        if (classComponent.getVisibility() != null) node.addAttribute("visibility", classComponent.getVisibility().name().toLowerCase());
        Element constructors = node.addElement("Constructors");
        XmlSerializer serializer = new ConstructorSerializer(classComponent.getConstructors());
        serializer.serialize(constructors);
        Element fields = node.addElement("Fields");
        serializer = new FieldSerializer(classComponent.getFields());
        serializer.serialize(fields);
        Element methods = node.addElement("Methods");
        serializer = new MethodSerializer(classComponent.getMethods());
        serializer.serialize(methods);
   }
    
}
