/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlDeserialization;

import org.dom4j.Element;
import org.uml.model.members.Literal;

/**
 *
 * @author stefanpetrovic
 */
public class LiteralDeserializer implements XmlDeserializer{

    private Literal literal;

    public LiteralDeserializer(Literal literal) {
        this.literal = literal;
    }
    
    /**
     * Fills in attributes of literal object by reading attributes and
     * sub-nodes of the node input element.
     *
     * @param node is XML tree node whose attributes and sub-nodes represent
     * attributes of the literal object.
     */
    @Override
    public void deserialize(Element node) {
        String literalName = node.attributeValue("name");
        if (literalName != null) literal.setName(literalName);
    }
    
}
