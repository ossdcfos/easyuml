/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlDeserialization;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import org.dom4j.Element;
import org.uml.model.ClassComponent;
import org.uml.model.Method;
import org.uml.model.MethodArgument;
import org.uml.model.Visibility;

/**
 *
 * @author Stefan
 */
public class MethodDeserializer implements XmlDeserializer{

    private Method method;

    public MethodDeserializer(Method method) {
        this.method = method;
    }
    
    /**
     * Fills in attributes of the method object by reading attributes and sub-nodes of the node input element.
     * @param node is XML tree node whose attributes and sub-nodes represent attributes of the method object.
     */
    @Override
    public void deserialize(Element node) {
        String name = node.attributeValue("name");
        String visibility = node.attributeValue("visibility");
        String type = node.attributeValue("returnType");
        String isStatic = node.attributeValue("isStatic");
        String isFinal = node.attributeValue("isFinal");
        String isAbstract = node.attributeValue("isAbstract");
        String isSynchronized = node.attributeValue("isSynchronized");
        if (name != null) method.setName(name);
        if (visibility != null) method.setVisibility(Visibility.stringToVisibility(visibility));
        if (type != null) method.setReturnType(type);
        if (isStatic != null) method.addModifier(Modifier.STATIC);
        if (isFinal != null) method.addModifier(Modifier.FINAL);
        if (isAbstract != null) method.addModifier(Modifier.ABSTRACT);
        if (isSynchronized != null) method.addModifier(Modifier.SYNCHRONIZED);
        HashMap<String, MethodArgument> arguments = new HashMap<String, MethodArgument>();
        Iterator argumentIterator = node.elementIterator("Argument");
        while (argumentIterator != null && argumentIterator.hasNext()) {
            Element argumentElement = (Element) argumentIterator.next();
            String argumentType = argumentElement.attributeValue("type");
            String argumentName = argumentElement.attributeValue("name");
            if (type != null && name != null) {
                Random r = new Random();
                int Low = 0;
                int High = 100;
                int R = r.nextInt(High - Low) + Low;
                arguments.put(Integer.toString(R), new MethodArgument(argumentType, argumentName));
            }
        }
        method.setArguments(arguments);
    }
    
}
