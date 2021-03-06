package org.uml.xmlDeserialization.members;

import java.util.Iterator;
import java.util.LinkedHashSet;
import org.dom4j.Element;
import org.uml.model.members.MethodArgument;
import org.uml.model.Visibility;
import org.uml.model.members.Method;
import org.uml.xmlDeserialization.XmlDeserializer;

/**
 *
 * @author Stefan
 */
public class MethodDeserializer implements XmlDeserializer {

    private Method method;

    public MethodDeserializer(Method method) {
        this.method = method;
    }

    /**
     * Fills in attributes of the method object by reading attributes and sub-nodes of the node input element.
     *
     * @param node is XML tree node whose attributes and sub-nodes represent attributes of the method object.
     */
    @Override
    public void deserialize(Element node) {
        String name = node.attributeValue("name");
        String type = node.attributeValue("returnType");
        String visibility = node.attributeValue("visibility");
        String isAbstract = node.attributeValue("isAbstract");
        String isStatic = node.attributeValue("isStatic");
        String isFinal = node.attributeValue("isFinal");
        String isSynchronized = node.attributeValue("isSynchronized");

        if (name != null) method.setName(name);
        if (type != null) method.setType(type);
        if (visibility != null) method.setVisibility(Visibility.valueOf(visibility.toUpperCase()));
        if (isAbstract != null) method.setAbstract(Boolean.parseBoolean(isAbstract));
        if (isStatic != null) method.setStatic(Boolean.parseBoolean(isStatic));
        if (isFinal != null) method.setFinal(Boolean.parseBoolean(isFinal));
        if (isSynchronized != null) method.setSynchronized(Boolean.parseBoolean(isSynchronized));

        LinkedHashSet<MethodArgument> arguments = new LinkedHashSet<>();
        Iterator<?> argumentIterator = node.elementIterator("Argument");
        while (argumentIterator != null && argumentIterator.hasNext()) {
            Element argumentElement = (Element) argumentIterator.next();
            String argumentType = argumentElement.attributeValue("type");
            String argumentName = argumentElement.attributeValue("name");
            if (argumentType != null && argumentName != null) {
                MethodArgument methodArgument = new MethodArgument(argumentType, argumentName);
                arguments.add(methodArgument);
            }
        }
        method.setArguments(arguments);
    }
}
