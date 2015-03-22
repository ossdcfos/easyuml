package org.uml.xmlDeserialization.members;

import java.util.Iterator;
import java.util.LinkedHashSet;
import org.dom4j.Element;
import org.uml.model.members.Constructor;
import org.uml.model.members.MethodArgument;
import org.uml.model.Visibility;
import org.uml.xmlDeserialization.XmlDeserializer;

/**
 *
 * @author Stefan
 */
public class ConstructorDeserializer implements XmlDeserializer{

    
    private Constructor constructor;

    public ConstructorDeserializer(Constructor constructor) {
        this.constructor = constructor;
    }
    /**
     * Fills in attributes of the constructor object by reading attributes and sub-nodes of the node input element.
     * @param node is XML tree node whose attributes and sub-nodes represent attributes of the constructor object.
     */
    @Override
    public void deserialize(Element node) {
        String visibility = node.attributeValue("visibility");
        if (visibility != null) constructor.setVisibility(Visibility.valueOf(visibility.toUpperCase()));
        
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
        constructor.setArguments(arguments);
    }
}
