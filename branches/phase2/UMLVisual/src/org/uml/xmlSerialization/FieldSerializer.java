/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import java.util.HashMap;
import org.dom4j.Element;
import org.uml.model.Field;

/**
 *
 * @author Stefan
 */
public class FieldSerializer implements XmlSerializer{

    private HashMap<String, Field> fields;

    public FieldSerializer(HashMap<String, Field> fields) {
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
        for (Field field : fields.values()) {
           Element fieldNode = node.addElement("Field");
           if (field.getName() != null) fieldNode.addAttribute("name", field.getName());
           if (field.getVisibility() != null) fieldNode.addAttribute("visibility", field.getVisibility().toString());
           if (field.getType() != null) fieldNode.addAttribute("type", field.getType());
           if (field.isIsStatic()) fieldNode.addAttribute("isStatic", Boolean.toString(field.isIsStatic()));
           if (field.isIsFinal()) fieldNode.addAttribute("isFinal", Boolean.toString(field.isIsFinal()));
           if (field.isIsSynchronized()) fieldNode.addAttribute("isSynchronized", Boolean.toString(field.isIsSynchronized()));
        }
    }
    
}
