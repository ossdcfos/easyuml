package org.uml.xmlDeserialization;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import org.dom4j.Element;
import org.uml.model.members.MethodArgument;

/**
 *
 * @author Stefan
 */
public class MethodArgumentsDeserializer implements XmlDeserializer{

    private HashMap<String, MethodArgument> arguments;

    public MethodArgumentsDeserializer(HashMap<String, MethodArgument> arguments) {
        this.arguments = arguments;
    }
    
    /**
     * Reads the node parameter, creates individual MethodArgument object and fills the arguments collection.
     * @param node is XML tree node whose attributes and sub-nodes represent arguments collection.
     */
    @Override
    public void deserialize(Element node) {
        Iterator<?> argumentIterator = node.elementIterator("Constructor");
        while (argumentIterator != null && argumentIterator.hasNext()) {
            Element argument = (Element) argumentIterator.next();
            String type = argument.attributeValue("type");
            String name = argument.attributeValue("name");
            if (type != null && name != null) {
                Random r = new Random();
                int Low = 0;
                int High = 100;
                int R = r.nextInt(High - Low) + Low;
                arguments.put(Integer.toString(R), new MethodArgument(type, name));
            }
        }
    }
    
}
