package org.uml.xmlDeserialization;

//import com.thoughtworks.xstream.converters.Converter;
//import com.thoughtworks.xstream.converters.MarshallingContext;
//import com.thoughtworks.xstream.converters.UnmarshallingContext;
//import com.thoughtworks.xstream.io.HierarchicalStreamReader;
//import com.thoughtworks.xstream.io.HierarchicalStreamWriter;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.dom4j.Element;
import org.uml.model.components.ClassComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.components.PackageComponent;
import org.uml.model.Visibility;
import org.uml.model.members.Method;

/**
 *
 * @author Stefan
 */
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
//        String packageName = node.attributeValue("package");
        String className = node.attributeValue("name");
        String isAbstract = node.attributeValue("isAbstract");
        String visibility = node.attributeValue("visibility");
        int xPos = (int) Double.parseDouble(node.attributeValue("xPosition"));
        int yPos = (int) Double.parseDouble(node.attributeValue("yPosition"));
        String widthString = node.attributeValue("width");
        String heightString = node.attributeValue("height");
        String xOffString = node.attributeValue("xOff");
        String yOffString = node.attributeValue("yOff");

        classComponent.setPosition(new Point(xPos, yPos));
        if (widthString != null) {
            int width = Integer.parseInt(widthString);
            int height = Integer.parseInt(heightString);
            int xOff = Integer.parseInt(xOffString);
            int yOff = Integer.parseInt(yOffString);
            classComponent.setBounds(new Rectangle(xOff, yOff, width, height));
        }
//        if (packageName != null) {
//            classComponent.setParentPackage(new PackageComponent(classComponent.getParentDiagram(), packageName));
//        }
        if (className != null) {
            classComponent.setName(className);
        }
        if (isAbstract != null) {
            classComponent.setAbstract(Boolean.parseBoolean(isAbstract));
        }
        if (visibility != null) {
            classComponent.setVisibility(Visibility.valueOf(visibility.toUpperCase()));
        }

        Iterator<?> constructorIterator = node.element("Constructors").elementIterator("Constructor");
        while (constructorIterator != null && constructorIterator.hasNext()) {
            Element constructorElement = (Element) constructorIterator.next();
            Constructor constructor = new Constructor(null);
            ConstructorDeserializer cd = new ConstructorDeserializer(constructor);
            cd.deserialize(constructorElement);
            classComponent.addConstructor(constructor);
        }

        Iterator<?> fieldIterator = node.element("Fields").elementIterator("Field");
        while (fieldIterator != null && fieldIterator.hasNext()) {
            Element fieldElement = (Element) fieldIterator.next();
            Field field = new Field("unknownField", "Object", Visibility.PACKAGE);
            FieldDeserializer fd = new FieldDeserializer(field);
            fd.deserialize(fieldElement);
            classComponent.addField(field);
        }

        Iterator<?> methodIterator = node.element("Methods").elementIterator("Method");
        while (methodIterator != null && methodIterator.hasNext()) {
            Element methodElement = (Element) methodIterator.next();
            Method method = new Method(null);
            MethodDeserializer md = new MethodDeserializer(method);
            md.deserialize(methodElement);
            classComponent.addMethod(method);
        }

    }
}
