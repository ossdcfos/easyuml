package org.uml.xmlDeserialization.components;

import org.uml.xmlDeserialization.members.LiteralDeserializer;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import org.dom4j.Element;
import org.uml.model.GenerationSetting;
import org.uml.model.components.EnumComponent;
import org.uml.model.members.Literal;
import org.uml.model.Visibility;
import org.uml.xmlDeserialization.XmlDeserializer;

/**
 *
 * @author stefanpetrovic
 */
public class EnumDeserializer implements XmlDeserializer {

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
        String name = node.attributeValue("name");
        String packageName = node.attributeValue("package");
        String visibility = node.attributeValue("visibility");
        String generation = node.attributeValue("generation");
        int xPos = (int) Double.parseDouble(node.attributeValue("xPosition"));
        int yPos = (int) Double.parseDouble(node.attributeValue("yPosition"));

        if (name != null) enumComponent.setName(name);
        if (packageName != null) enumComponent.setParentPackage(packageName);
        if (visibility != null) enumComponent.setVisibility(Visibility.valueOf(visibility.toUpperCase()));
        if (generation != null) enumComponent.setGeneration(GenerationSetting.stringToGenerationSetting(generation));
        //enumComponent.setLocation(new Point(xPos, yPos));
        int width = 0;
        int height = 0;
        if (node.attribute("width") != null && node.attribute("height") != null) {
            width = (int) Double.parseDouble(node.attributeValue("width"));
            height = (int) Double.parseDouble(node.attributeValue("height"));
        }
        Rectangle bounds = new Rectangle(xPos,yPos,width,height);
        enumComponent.setBounds(bounds);
        
        Iterator<?> literalIterator = node.element("Literals").elementIterator("Literal");
        while (literalIterator != null && literalIterator.hasNext()) {
            Element literalElement = (Element) literalIterator.next();
            Literal literal = new Literal("untitledLiteral");
            LiteralDeserializer ld = new LiteralDeserializer(literal);
            ld.deserialize(literalElement);
            enumComponent.addLiteral(literal);
        }
    }
}
