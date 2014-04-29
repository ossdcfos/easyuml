/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlDeserialization;

import java.awt.Point;
import java.util.Iterator;
import org.dom4j.Element;
import org.uml.model.Constructor;
import org.uml.model.EnumComponent;
import org.uml.model.Field;
import org.uml.model.Literal;
import org.uml.model.Method;
import org.uml.model.PackageComponent;
import org.uml.model.Visibility;

/**
 *
 * @author stefanpetrovic
 */
public class EnumDeserializer implements XmlDeserializer{

    private EnumComponent enumComponent;

    public EnumDeserializer(EnumComponent enumComponent) {
        this.enumComponent = enumComponent;
    }
    
    /**
     * Fills in attributes of eunmComponent object by reading attributes and
     * sub-nodes of the node input element.
     *
     * @param node is XML tree node whose attributes and sub-nodes represent
     * attributes of the enumComponent.
     */
    @Override
    public void deserialize(Element node) {
        String packageName = node.attributeValue("package");
        String className = node.attributeValue("name");
        String visibility = node.attributeValue("visibility");
        int xPos = (int) Double.parseDouble(node.attributeValue("xPosition"));
        int yPos = (int) Double.parseDouble(node.attributeValue("yPosition"));
        enumComponent.setPosition(new Point(xPos, yPos));
        if (packageName != null) {
            enumComponent.setParentPackage(new PackageComponent(packageName));
        }
        if (className != null) {
            enumComponent.setName(className);
        }
        if (visibility != null) {
            enumComponent.setVisibility(Visibility.stringToVisibility(visibility));
        }
        
        Iterator literalIterator = node.element("Literals").elementIterator("Literal");
        while (literalIterator != null && literalIterator.hasNext()) {
            Element literalElement = (Element) literalIterator.next();
            Literal literal = new Literal(null);
            LiteralDeserializer ld = new LiteralDeserializer(literal);
            ld.deserialize(literalElement);
            enumComponent.addLiteral(literal);
            literal.setDeclaringClass(enumComponent);
        }
        
        Iterator constructorIterator = node.element("Constructors").elementIterator("Constructor");
        while (constructorIterator != null && constructorIterator.hasNext()) {
            Element constructorElement = (Element) constructorIterator.next();
            Constructor constructor = new Constructor(null);
            ConstructorDeserializer cd = new ConstructorDeserializer(constructor);
            cd.deserialize(constructorElement);
            enumComponent.addConstructor(constructor);
            constructor.setDeclaringClass(enumComponent);
        }
        Iterator fieldIterator = node.element("Fields").elementIterator("Field");
        while (fieldIterator != null && fieldIterator.hasNext()) {
            Element fieldElement = (Element) fieldIterator.next();
            Field field = new Field("unknownField", "Object", Visibility.PACKAGE);
            FieldDeserializer fd = new FieldDeserializer(field);
            fd.deserialize(fieldElement);
            enumComponent.addField(field);
            field.setDeclaringClass(enumComponent);
        }

        Iterator methodIterator = node.element("Methods").elementIterator("Method");
        while (methodIterator != null && methodIterator.hasNext()) {
            Element methodElement = (Element) methodIterator.next();
            Method method = new Method(null);
            MethodDeserializer md = new MethodDeserializer(method);
            md.deserialize(methodElement);
            enumComponent.addMethod(method);
            method.setDeclaringClass(enumComponent);
        }
    }
    
}
