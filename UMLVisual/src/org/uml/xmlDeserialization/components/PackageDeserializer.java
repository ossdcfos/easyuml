package org.uml.xmlDeserialization.components;

import java.awt.Point;
import java.awt.Rectangle;
import org.dom4j.Element;
import org.uml.model.GenerationSetting;
import org.uml.model.Visibility;
import org.uml.model.components.PackageComponent;
import org.uml.xmlDeserialization.XmlDeserializer;

/**
 *
 * @author stefanpetrovic
 */
public class PackageDeserializer implements XmlDeserializer {

    private PackageComponent packageComponent;

    public PackageDeserializer(PackageComponent packageComponent) {
        this.packageComponent = packageComponent;
    }

    /**
     * Fills in attributes of packageComponent object by reading attributes and
     * sub-nodes of the node input element.
     *
     * @param node is XML tree node whose attributes and sub-nodes represent
     * attributes of the packageComponent.
     */
    @Override
    public void deserialize(Element node) {
        String name = node.attributeValue("name");
        String packageName = node.attributeValue("package");
        String visibility = node.attributeValue("visibility");
        String generation = node.attributeValue("generation");
        int xPos = (int) Double.parseDouble(node.attributeValue("xPosition"));
        int yPos = (int) Double.parseDouble(node.attributeValue("yPosition"));

        if (name != null) packageComponent.setName(name);
        if (packageName != null) packageComponent.setParentPackage(packageName);
        if (visibility != null) packageComponent.setVisibility(Visibility.valueOf(visibility.toUpperCase()));
        if (generation != null) packageComponent.setGeneration(GenerationSetting.stringToGenerationSetting(generation));
        //packageComponent.setLocation(new Point(xPos, yPos));
        int width = 0;
        int height = 0;
        if (node.attribute("width") != null && node.attribute("height") != null) {
            width = (int) Double.parseDouble(node.attributeValue("width"));
            height = (int) Double.parseDouble(node.attributeValue("height"));
        }
        Rectangle bounds = new Rectangle(xPos,yPos,width,height);
        packageComponent.setBounds(bounds);
    }
}
