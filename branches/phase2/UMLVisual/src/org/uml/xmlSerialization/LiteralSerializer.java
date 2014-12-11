package org.uml.xmlSerialization;

import java.util.LinkedHashSet;
import org.dom4j.Element;
import org.uml.model.members.Literal;

/**
 *
 * @author stefanpetrovic
 */
public class LiteralSerializer implements XmlSerializer{

    private LinkedHashSet<Literal> literal;

    public LiteralSerializer(LinkedHashSet<Literal> literal) {
        this.literal = literal;
    }
    
    /**
     * Serializes literal object to XML by translating its fields into parameter node's attributes and subelements.
     * @param node represents the node that will contain serialized literal object.
     */
    @Override
    public void serialize(Element node) {
        for (Literal l : literal) {
            Element literalNode = node.addElement("Literal");
            if (literalNode != null) literalNode.addAttribute("name", l.getName());
        }
    }
    
}
