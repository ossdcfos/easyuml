/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import org.dom4j.Element;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;

/**
 *
 * @author Stefan
 */
public class ClassSerializer implements ClassDiagramComponentSerializer{

    private ClassComponent classComponent;
    
    @Override
    public void addClassDiagramComponent(ClassDiagramComponent component) {
        try{
            classComponent = (ClassComponent) component;
        }catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to ClassComponent!");
            e.printStackTrace();
        }
    }

    @Override
    public void serialize(Element node) {
        
        if (classComponent.getName() != null) node.addAttribute("name", classComponent.getName());
        if (classComponent.getVisibility() != null) node.addAttribute("visibility", classComponent.getVisibility().toString());
        if (classComponent.getParentDiagram() != null) node.addAttribute("parentDiagram", classComponent.getParentDiagram().toString());
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
