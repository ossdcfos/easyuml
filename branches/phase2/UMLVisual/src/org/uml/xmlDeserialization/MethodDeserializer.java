package org.uml.xmlDeserialization;

import java.util.HashMap;
import java.util.Iterator;
import org.dom4j.Element;
import org.uml.model.members.MethodArgument;
import org.uml.model.Visibility;
import org.uml.model.members.Method;

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
        if (visibility != null) method.setVisibility(Visibility.valueOf(visibility.toUpperCase()));
        if (type != null) method.setType(type);
        if (isStatic != null) method.setStatic(Boolean.parseBoolean(isStatic));
        if (isFinal != null) method.setFinal(Boolean.parseBoolean(isFinal));
        if (isAbstract != null) method.setAbstract(Boolean.parseBoolean(isAbstract));
        if (isSynchronized != null) method.setSynchronized(Boolean.parseBoolean(isSynchronized));
        
        HashMap<String, MethodArgument> arguments = new HashMap<>();
        Iterator<?> argumentIterator = node.elementIterator("Argument");
        while (argumentIterator != null && argumentIterator.hasNext()) {
            Element argumentElement = (Element) argumentIterator.next();
            String argumentType = argumentElement.attributeValue("type");
            String argumentName = argumentElement.attributeValue("name");
            if (type != null && name != null) {
                MethodArgument methodArgument = new MethodArgument(type, name);
                arguments.put(methodArgument.getName(), methodArgument);
            }
        }
        method.setArguments(arguments);
    }
    
}
