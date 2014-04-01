/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlDeserialization;

import java.util.Iterator;
import org.dom4j.Element;
import org.uml.model.ClassComponent;
import org.uml.model.Constructor;
import org.uml.model.Field;
import org.uml.model.Method;
import org.uml.model.Visibility;

/**
 *
 * @author Stefan
 */
public class ClassDeserializer implements XmlDeserializer{
    
    private ClassComponent classComponent;

    public ClassDeserializer(ClassComponent classComponent) {
        this.classComponent = classComponent;
    }
    /**
     * Fills in attributes of classComponent object by reading attributes and sub-nodes of the node input element.
     * @param node is XML tree node whose attributes and sub-nodes represent attributes of the classComponent.
     */
    @Override
    public void deserialize(Element node) {
        String className = node.attributeValue("name");
        String visibility = node.attributeValue("visibility");
        if (className != null) {
            classComponent.setName(className);
        }
        if (visibility != null) {
           classComponent.setVisibility(Visibility.stringToVisibility(node.attributeValue("visibility")));
        }
        Iterator constructorIterator = node.element("Constructors").elementIterator("Constructor");
        while (constructorIterator != null && constructorIterator.hasNext()) {
            Element constructorElement = (Element) constructorIterator.next();
            Constructor constructor = new Constructor(null);
            ConstructorDeserializer cd = new ConstructorDeserializer(constructor);
            cd.deserialize(constructorElement);
            classComponent.addConstructor(constructor);
            classComponent.addMember(constructor);
            constructor.setDeclaringClass(classComponent);
        }
        Iterator fieldIterator = node.element("Fields").elementIterator("Field");
        while (fieldIterator != null && fieldIterator.hasNext()) {
            Element fieldElement = (Element) fieldIterator.next();
            Field field = new Field(null, null, null);
            FieldDeserializer fd = new FieldDeserializer(field);
            fd.deserialize(fieldElement);
            classComponent.addField(field);
            classComponent.addMember(field);
            field.setDeclaringClass(classComponent);
        }
        
        Iterator methodIterator = node.element("Methods").elementIterator("Method");
        while (methodIterator != null && fieldIterator.hasNext()) {
            Element methodElement = (Element) methodIterator.next();
            Method method = new Method(null);
            MethodDeserializer md = new MethodDeserializer(method);
            md.deserialize(methodElement);
            classComponent.addMethod(method);
            classComponent.addMember(method);
            method.setDeclaringClass(classComponent);
        }
        
    }
    
}
