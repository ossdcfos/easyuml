package org.uml.xmlSerialization;

import java.util.LinkedHashMap;
import org.dom4j.Element;
import org.uml.model.members.Field;

/**
 *
 * @author Stefan
 */
public class FieldSerializer implements XmlSerializer{

    private LinkedHashMap<String, Field> fields;

    public FieldSerializer(LinkedHashMap<String, Field> fields) {
        this.fields = fields;
    }
    
    /**
     * Converts fields objects to nodes of an XML tree.
     * Each object from fields is translated to a sub-node of the node input parameter.
     * Each attribute of every field object from fields is translated to an attribute of the corresponding XML node.
     * @param node represents empty XML node into which to put nodes representing fields.
     */
    @Override
    public void serialize(Element node) {
        for (Field field : fields.values()){
            Element fieldNode = node.addElement("Field");
            if (field.getName() != null) fieldNode.addAttribute("name", field.getName());
            if (field.getVisibility() != null) fieldNode.addAttribute("visibility", field.getVisibility().name().toLowerCase());
            if (field.getType() != null) fieldNode.addAttribute("type", field.getType());
            if (field.isStatic()) fieldNode.addAttribute("isStatic", Boolean.toString(field.isStatic()));
            if (field.isFinal()) fieldNode.addAttribute("isFinal", Boolean.toString(field.isFinal()));
            if (field.isTransient()) fieldNode.addAttribute("isTransient", Boolean.toString(field.isTransient()));
            if (field.isVolatile()) fieldNode.addAttribute("isVolatile", Boolean.toString(field.isTransient()));
        }
    }
    
}
