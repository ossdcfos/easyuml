package org.uml.xmlDeserialization;

import java.awt.Point;
import java.util.Iterator;
import org.dom4j.Element;
import org.uml.model.components.EnumComponent;
import org.uml.model.members.Literal;
import org.uml.model.Visibility;

/**
 *
 * @author stefanpetrovic
 */
public class EnumDeserializer implements XmlDeserializer{

    private EnumComponent enumComponent;

    public EnumDeserializer(EnumComponent enumComponent) {
        this.enumComponent = enumComponent;
    }
    
    /**
     * Fills in attributes of eunmComponent object by reading attributes and
     * sub-nodes of the node input element.
     *
     * @param node is XML tree node whose attributes and sub-nodes represent
     * attributes of the enumComponent.
     */
    @Override
    public void deserialize(Element node) {
        String packageName = node.attributeValue("package");
        String className = node.attributeValue("name");
        String visibility = node.attributeValue("visibility");
        int xPos = (int) Double.parseDouble(node.attributeValue("xPosition"));
        int yPos = (int) Double.parseDouble(node.attributeValue("yPosition"));
        enumComponent.setLocation(new Point(xPos, yPos));
        if (packageName != null) {
            enumComponent.setParentPackage(packageName);
        }
        if (className != null) {
            enumComponent.setName(className);
        }
        if (visibility != null) {
            enumComponent.setVisibility(Visibility.valueOf(visibility.toUpperCase()));
        }
        
        Iterator<?> literalIterator = node.element("Literals").elementIterator("Literal");
        while (literalIterator != null && literalIterator.hasNext()) {
            Element literalElement = (Element) literalIterator.next();
            Literal literal = new Literal(null);
            LiteralDeserializer ld = new LiteralDeserializer(literal);
            ld.deserialize(literalElement);
            enumComponent.addLiteral(literal);
            literal.setDeclaringComponent(enumComponent);
        }
    }
    
}
