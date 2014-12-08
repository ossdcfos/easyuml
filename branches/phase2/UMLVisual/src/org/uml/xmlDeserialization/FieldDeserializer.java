package org.uml.xmlDeserialization;

import org.dom4j.Element;
import org.uml.model.members.Field;
import org.uml.model.Visibility;

/**
 *
 * @author Stefan
 */
public class FieldDeserializer implements XmlDeserializer{

    private Field field;

    public FieldDeserializer(Field field) {
        this.field = field;
    }
    
    /**
     * Fills in attributes of the field object by reading attributes and sub-nodes of the node input element.
     * @param node is XML tree node whose attributes and sub-nodes represent attributes of the field object.
     */
    @Override
    public void deserialize(Element node) {
        String name = node.attributeValue("name");
        String visibility = node.attributeValue("visibility");
        String type = node.attributeValue("type");
        String isStatic = node.attributeValue("isStatic");
        String isFinal = node.attributeValue("isFinal");
        String isTransient = node.attributeValue("isTransient");
        String isVolatile = node.attributeValue("isVolatile");
        if (name != null) field.setName(name);
        if (visibility != null) field.setVisibility(Visibility.valueOf(visibility.toUpperCase()));
        if (type != null) field.setType(type);
        if (isStatic != null) field.setStatic(Boolean.parseBoolean(isStatic));
        if (isFinal != null) field.setFinal(Boolean.parseBoolean(isFinal));
        if (isTransient != null) field.setTransient(Boolean.parseBoolean(isTransient));
        if (isVolatile != null) field.setTransient(Boolean.parseBoolean(isVolatile));
    }
    
}
