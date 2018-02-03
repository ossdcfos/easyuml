package org.uml.xmlDeserialization.components;

import org.uml.xmlDeserialization.members.ConstructorDeserializer;
import org.uml.xmlDeserialization.members.FieldDeserializer;
import org.uml.xmlDeserialization.members.MethodDeserializer;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import org.dom4j.Element;
import org.uml.model.GetterSetterGeneration;
import org.uml.model.components.ClassComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.Visibility;
import org.uml.model.members.Method;
import org.uml.xmlDeserialization.XmlDeserializer;

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
        String name = node.attributeValue("name");
        String packageName = node.attributeValue("package");
        String visibility = node.attributeValue("visibility");
        String isAbstract = node.attributeValue("isAbstract");
        String isStatic = node.attributeValue("isStatic");
        String isFinal = node.attributeValue("isFinal");
        String gettersGeneration = node.attributeValue("gettersGeneration");
        String settersGeneration = node.attributeValue("settersGeneration");
        int xPos = (int) Double.parseDouble(node.attributeValue("xPosition"));
        int yPos = (int) Double.parseDouble(node.attributeValue("yPosition"));
        
        if (name != null) classComponent.setName(name);
        if (packageName != null) classComponent.setParentPackage(packageName);
        if (visibility != null) classComponent.setVisibility(Visibility.valueOf(visibility.toUpperCase()));
        if (isAbstract != null) classComponent.setAbstract(Boolean.parseBoolean(isAbstract));
        if (isStatic != null) classComponent.setStatic(Boolean.parseBoolean(isStatic));
        if (isFinal != null) classComponent.setFinal(Boolean.parseBoolean(isFinal));
        if (gettersGeneration != null) classComponent.setGetterGeneration(GetterSetterGeneration.stringToGetterSetterGeneration(gettersGeneration));
        if (settersGeneration != null) classComponent.setSetterGeneration(GetterSetterGeneration.stringToGetterSetterGeneration(settersGeneration));
        
        int width = 0;
        int height = 0;
        if (node.attribute("width") != null && node.attribute("height") != null) {
            width = (int) Double.parseDouble(node.attributeValue("width"));
            height = (int) Double.parseDouble(node.attributeValue("height"));
        }
        Rectangle bounds = new Rectangle(xPos,yPos,width,height);
        classComponent.setBounds(bounds);
        
        Iterator<?> fieldIterator = node.element("Fields").elementIterator("Field");
        while (fieldIterator != null && fieldIterator.hasNext()) {
            Element fieldElement = (Element) fieldIterator.next();
            Field field = new Field("unknownField", "Object", Visibility.PACKAGE);
            FieldDeserializer fd = new FieldDeserializer(field);
            fd.deserialize(fieldElement);
            classComponent.addField(field);
        }

        Iterator<?> constructorIterator = node.element("Constructors").elementIterator("Constructor");
        while (constructorIterator != null && constructorIterator.hasNext()) {
            Element constructorElement = (Element) constructorIterator.next();
            Constructor constructor = new Constructor(name);
            ConstructorDeserializer cd = new ConstructorDeserializer(constructor);
            cd.deserialize(constructorElement);
            classComponent.addConstructor(constructor);
        }

        Iterator<?> methodIterator = node.element("Methods").elementIterator("Method");
        while (methodIterator != null && methodIterator.hasNext()) {
            Element methodElement = (Element) methodIterator.next();
            Method method = new Method("untitledMethod", "void");
            MethodDeserializer md = new MethodDeserializer(method);
            md.deserialize(methodElement);
            classComponent.addMethod(method);
        }
    }
}

// //XStream map converter
//class MapToAttributesConverter implements Converter {
//
//    public MapToAttributesConverter() {
//    }
//
//    @Override
//    public boolean canConvert(Class type) {
//        return Map.class.isAssignableFrom(type);
//    }
//
//    @Override
//    public void marshal(Object o, HierarchicalStreamWriter writer, MarshallingContext mc) {
//        Map<String, Field> map = (Map<String, Field>) o;
//        for (Map.Entry<String, Field> entry : map.entrySet()) {
//            
//        }
//    }
//
//    @Override
//    public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext uc) {
//        Map<String, String> map = new HashMap<>();
//        for (int i = 0; i < reader.getAttributeCount(); i++) {
//            String key = reader.getAttributeName(i);
//            String value = reader.getAttribute(key);
//            map.put(key, value);
//        }
//        return map;
//    }
//}