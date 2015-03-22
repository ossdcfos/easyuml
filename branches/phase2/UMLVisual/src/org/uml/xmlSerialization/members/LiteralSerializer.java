package org.uml.xmlSerialization.members;

import java.util.LinkedHashSet;
import org.dom4j.Element;
import org.uml.model.members.Literal;
import org.uml.xmlSerialization.XmlSerializer;

/**
 *
 * @author stefanpetrovic
 */
public class LiteralSerializer implements XmlSerializer {

    private LinkedHashSet<Literal> literals;

    public LiteralSerializer(LinkedHashSet<Literal> literal) {
        this.literals = literal;
    }

    /**
     * Serializes literal object to XML by translating its fields into parameter node's attributes and subelements.
     *
     * @param node represents the node that will contain serialized literal object.
     */
    @Override
    public void serialize(Element node) {
        for (Literal literal : literals) {
            Element literalNode = node.addElement("Literal")
                    .addAttribute("name", literal.getName());
        }
    }
}
