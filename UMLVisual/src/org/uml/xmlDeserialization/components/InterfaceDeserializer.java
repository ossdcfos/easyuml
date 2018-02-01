package org.uml.xmlDeserialization.components;

import org.uml.xmlDeserialization.members.MethodDeserializer;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import org.dom4j.Element;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.Visibility;
import org.uml.model.members.Method;
import org.uml.xmlDeserialization.XmlDeserializer;

/**
 *
 * @author stefanpetrovic
 */
public class InterfaceDeserializer implements XmlDeserializer {

    private InterfaceComponent interfaceComponent;

    public InterfaceDeserializer(InterfaceComponent component) {
        interfaceComponent = component;
    }

    /**
     * Fills in attributes of interfaceComponent object by reading attributes and
     * sub-nodes of the node input element.
     *
     * @param node is XML tree node whose attributes and sub-nodes represent
     * attributes of the interfaceComponent.
     */
    @Override
    public void deserialize(Element node) {
        String name = node.attributeValue("name");
        String packageName = node.attributeValue("package");
        String visibility = node.attributeValue("visibility");
        String isStatic = node.attributeValue("isStatic");

        int xPos = (int) Double.parseDouble(node.attributeValue("xPosition"));
        int yPos = (int) Double.parseDouble(node.attributeValue("yPosition"));

        if (name != null) interfaceComponent.setName(name);
        if (packageName != null) interfaceComponent.setParentPackage(packageName);
        if (visibility != null) interfaceComponent.setVisibility(Visibility.valueOf(visibility.toUpperCase()));
        if (isStatic != null) interfaceComponent.setStatic(Boolean.parseBoolean(isStatic));
        //interfaceComponent.setLocation(new Point(xPos, yPos));
        int width = 0;
        int height = 0;
        if (node.attribute("width") != null && node.attribute("height") != null) {
            width = (int) Double.parseDouble(node.attributeValue("width"));
            height = (int) Double.parseDouble(node.attributeValue("height"));
        }
        Rectangle bounds = new Rectangle(xPos,yPos,width,height);
        interfaceComponent.setBounds(bounds);
        
        Iterator<?> methodIterator = node.element("Methods").elementIterator("Method");
        while (methodIterator != null && methodIterator.hasNext()) {
            Element methodElement = (Element) methodIterator.next();
            Method method = new Method("untitledMethod", "void");
            MethodDeserializer md = new MethodDeserializer(method);
            md.deserialize(methodElement);
            interfaceComponent.addMethod(method);
        }
    }
}
