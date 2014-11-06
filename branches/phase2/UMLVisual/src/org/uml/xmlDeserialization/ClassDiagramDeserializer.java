/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlDeserialization;

import java.util.ArrayList;
import java.util.Iterator;
import org.dom4j.Element;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;
import org.uml.model.relations.HasBaseRelationComponent;
import org.uml.model.relations.ImplementsRelationComponent;
import org.uml.model.InterfaceComponent;
import org.uml.model.relations.AggregationRelationComponent;
import org.uml.model.relations.IsRelationComponent;
import org.uml.model.relations.UseRelationComponent;

/**
 *
 * @author Stefan
 */
public class ClassDiagramDeserializer implements XmlDeserializer {

    private ClassDiagram classDiagram;

    public ClassDiagramDeserializer(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
    }

    /**
     * Fills in attributes of classDiagram object by reading attributes and
     * sub-nodes of the node input element.
     *
     * @param node is XML tree node whose attributes and sub-nodes represent
     * attributes of the classComponent.
     */
    @Override
    public void deserialize(Element node) {
        classDiagram.setName(node.attributeValue("name"));

        Element classDiagramComponents = node.element("ClassDiagramComponents");
        Iterator<?> classIterator = classDiagramComponents.elementIterator("Class");
        while (classIterator != null && classIterator.hasNext()) {
            Element componentNode = (Element) classIterator.next();

            ClassComponent component = new ClassComponent();
            ClassDeserializer cd = new ClassDeserializer(component);
            cd.deserialize(componentNode);
            classDiagram.addComponent(component);
        }

        Iterator<?> interfaceIterator = classDiagramComponents.elementIterator("Interface");
        while (interfaceIterator != null && interfaceIterator.hasNext()) {
            Element interfaceNode = (Element) interfaceIterator.next();

            InterfaceComponent component = new InterfaceComponent();
            InterfaceDeserializer id = new InterfaceDeserializer(component);
            id.deserialize(interfaceNode);
            classDiagram.addComponent(component);
        }
        
        Iterator<?> enumIterator = classDiagramComponents.elementIterator("Enum");
        while (enumIterator != null && enumIterator.hasNext()) {
            Element enumNode = (Element) enumIterator.next();
            
            EnumComponent component = new EnumComponent();
            EnumDeserializer ed = new EnumDeserializer(component);
            ed.deserialize(enumNode);
            classDiagram.addComponent(component);
        }
        
        ArrayList<ClassDiagramComponent> components = new ArrayList<>(classDiagram.getComponents().values());
        Element classDiagramRelations = node.element("ClassDiagramRelations");
        Iterator<?> isRelationIterator = classDiagramRelations.elementIterator("IsRelation");
        while (isRelationIterator != null && isRelationIterator.hasNext()) {
            Element isRelationNode = (Element) isRelationIterator.next();
            
            IsRelationComponent relation = new IsRelationComponent();
            IsRelationDeserializer deserializer = new IsRelationDeserializer(relation, components);
            deserializer.deserialize(isRelationNode);
            classDiagram.addRelation(relation);
        }
        
        Iterator<?> hasRelationIterator = classDiagramRelations.elementIterator("HasRelation");
        while (hasRelationIterator != null && hasRelationIterator.hasNext()) {
            Element hasRelationNode = (Element) hasRelationIterator.next();
            
            // TODO needs to support composition
            HasBaseRelationComponent relation = new AggregationRelationComponent();
            HasRelationDeserializer deserializer = new HasRelationDeserializer(relation, components);
            deserializer.deserialize(hasRelationNode);
            classDiagram.addRelation(relation);
        }
        
        Iterator<?> useRelationIterator = classDiagramRelations.elementIterator("UseRelation");
        while (useRelationIterator != null && useRelationIterator.hasNext()) {
            Element useRelationNode = (Element) useRelationIterator.next();
            
            UseRelationComponent relation = new UseRelationComponent();
            UseRelationDeserializer deserializer = new UseRelationDeserializer(relation, components);
            deserializer.deserialize(useRelationNode);
            classDiagram.addRelation(relation);
        }
        
        Iterator<?> implementsRelationIterator = classDiagramRelations.elementIterator("ImplementsRelation");
        while (implementsRelationIterator != null && implementsRelationIterator.hasNext()) {
            Element implementsRelationNode = (Element) implementsRelationIterator.next();
            
            ImplementsRelationComponent relation = new ImplementsRelationComponent();
            ImplementsRelationDeserializer deserializer = new ImplementsRelationDeserializer(relation, components);
            deserializer.deserialize(implementsRelationNode);
            classDiagram.addRelation(relation);
        }
    }
}
