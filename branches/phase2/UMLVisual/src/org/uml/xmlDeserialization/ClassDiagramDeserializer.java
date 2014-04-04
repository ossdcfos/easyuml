/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlDeserialization;

import java.awt.Point;
import java.util.Iterator;
import org.dom4j.Element;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author Stefan
 */
public class ClassDiagramDeserializer implements XmlDeserializer { 

    private ClassDiagram classDiagram;
    private ClassDiagramScene scene;
    
    public ClassDiagramDeserializer(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
    }
    
    /**
     * Fills in attributes of classDiagram object by reading attributes and sub-nodes of the node input element.
     * @param node is XML tree node whose attributes and sub-nodes represent attributes of the classComponent.
     */
    @Override
    public void deserialize(Element node) {
        classDiagram.setName(node.attributeValue("name"));
        Iterator componentsIterator = node.element("ClassDiagramComponents").elementIterator("Class");
        while (componentsIterator != null && componentsIterator.hasNext()) {
            Element componentNode = (Element) componentsIterator.next();
            
                ClassComponent component = new ClassComponent();
                ClassDeserializer cd = new ClassDeserializer(component);
                cd.deserialize(componentNode);
                classDiagram.addComponent(component);
                component.setParentDiagram(classDiagram);
                int xPos = (int) Double.parseDouble(componentNode.attributeValue("xPosition"));
                int yPos = (int) Double.parseDouble(componentNode.attributeValue("yPosition"));
                component.setPosition(new Point(xPos, yPos));
            
        }
    }
    
}
