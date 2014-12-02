package org.uml.xmlDeserialization;

import java.util.HashMap;
import org.dom4j.Element;
import org.uml.model.members.Constructor;
import org.uml.model.members.MethodArgument;
import org.uml.model.Visibility;

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
        String name = node.attributeValue("name");
        String visibility = node.attributeValue("visibility");
        String returnType = node.attributeValue("returnType");
        if (name != null) constructor.setName(name);
        if (visibility != null) constructor.setVisibility(Visibility.valueOf(visibility));
        if (!node.elements().isEmpty()) {
            HashMap<String, MethodArgument> arguments = new HashMap<>();
            MethodArgumentsDeserializer mad = new MethodArgumentsDeserializer(arguments);
            mad.deserialize(node);
            constructor.setArguments(arguments);
        }
    }
    
}
