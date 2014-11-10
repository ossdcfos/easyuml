/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlDeserialization;

import java.awt.Point;
import java.util.Iterator;
import org.dom4j.Element;
import org.uml.model.ClassComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.members.Literal;
import org.uml.model.members.Method;
import org.uml.model.PackageComponent;
import org.uml.model.members.Visibility;

/**
 *
 * @author Stefan
 */
public class ClassDeserializer implements XmlDeserializer {

    private ClassComponent classComponent;

    public ClassDeserializer(ClassComponent classComponent) {
        this.classComponent = classComponent;
    }

    /**
     * Fills in attributes of classComponent object by reading attributes and
     * sub-nodes of the node input element.
     *
     * @param node is XML tree node whose attributes and sub-nodes represent
     * attributes of the classComponent.
     */
    @Override
    public void deserialize(Element node) {
        String packageName = node.attributeValue("package");
        String className = node.attributeValue("name");
        String isAbstract = node.attributeValue("isAbstract");
        String visibility = node.attributeValue("visibility");
        int xPos = (int) Double.parseDouble(node.attributeValue("xPosition"));
        int yPos = (int) Double.parseDouble(node.attributeValue("yPosition"));
        classComponent.setPosition(new Point(xPos, yPos));
        if (packageName != null) {
            classComponent.setParentPackage(new PackageComponent(packageName));
        }
        if (className != null) {
            classComponent.setName(className);
        }
        if (isAbstract != null) {
            classComponent.setAbstract(Boolean.parseBoolean(isAbstract));
        }
        if (visibility != null) {
            classComponent.setVisibility(Visibility.stringToVisibility(visibility));
        }
        
        Iterator<?> constructorIterator = node.element("Constructors").elementIterator("Constructor");
        while (constructorIterator != null && constructorIterator.hasNext()) {
            Element constructorElement = (Element) constructorIterator.next();
            Constructor constructor = new Constructor(null);
            ConstructorDeserializer cd = new ConstructorDeserializer(constructor);
            cd.deserialize(constructorElement);
            classComponent.addConstructor(constructor);
            constructor.setDeclaringClass(classComponent);
        }
        
        Iterator<?> fieldIterator = node.element("Fields").elementIterator("Field");
        while (fieldIterator != null && fieldIterator.hasNext()) {
            Element fieldElement = (Element) fieldIterator.next();
            Field field = new Field("unknownField", "Object", Visibility.PACKAGE);
            FieldDeserializer fd = new FieldDeserializer(field);
            fd.deserialize(fieldElement);
            classComponent.addField(field);
            field.setDeclaringClass(classComponent);
        }

        Iterator<?> methodIterator = node.element("Methods").elementIterator("Method");
        while (methodIterator != null && methodIterator.hasNext()) {
            Element methodElement = (Element) methodIterator.next();
            Method method = new Method(null);
            MethodDeserializer md = new MethodDeserializer(method);
            md.deserialize(methodElement);
            classComponent.addMethod(method);
            method.setDeclaringClass(classComponent);
        }

    }
}
