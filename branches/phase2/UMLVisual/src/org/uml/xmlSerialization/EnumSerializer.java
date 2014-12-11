package org.uml.xmlSerialization;

import org.dom4j.Element;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.EnumComponent;

/**
 *
 * @author Stefan
 */
public class EnumSerializer implements ClassDiagramComponentSerializer{

    private EnumComponent enumComponent;
    
    /**
     * Sets the enumComponent object that is going to be serialized.
     * @param component represents enumComponent object to be serialized.
     */
    @Override
    public void addClassDiagramComponent(ComponentBase component) {
        try{
            enumComponent = (EnumComponent) component;
        }catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to ClassComponent!");
            e.printStackTrace();
        }
    }

    /**
     * Serializes - object to XML by translating its fields into parameter node's attributes and subelements.
     * @param node represents the node that will contain serialized - object.
     */
    @Override
    public void serialize(Element node) {
        if (enumComponent.getParentPackage() != null) node.addAttribute("package", enumComponent.getParentPackage());
        if (enumComponent.getName() != null) node.addAttribute("name", enumComponent.getName());
        if (enumComponent.getVisibility() != null) node.addAttribute("visibility", enumComponent.getVisibility().name().toLowerCase());
        
        Element literals = node.addElement("Literals");
        XmlSerializer serializer = new LiteralSerializer(enumComponent.getLiterals());
        serializer.serialize(literals);
        
//        Element constructors = node.addElement("Constructors");
//        serializer = new ConstructorSerializer(enumComponent.getConstructors());
//        serializer.serialize(constructors);
//        
//        Element fields = node.addElement("Fields");
//        serializer = new FieldSerializer(enumComponent.getFields());
//        serializer.serialize(fields);
//        
//        Element methods = node.addElement("Methods");
//        serializer = new MethodSerializer(enumComponent.getMethods());
//        serializer.serialize(methods);
    }
    
}
