package org.uml.xmlDeserialization;

import org.uml.xmlDeserialization.relations.ImplementsRelationDeserializer;
import org.uml.xmlDeserialization.relations.HasRelationDeserializer;
import org.uml.xmlDeserialization.relations.UseRelationDeserializer;
import org.uml.xmlDeserialization.relations.IsRelationDeserializer;
import org.uml.xmlDeserialization.components.InterfaceDeserializer;
import org.uml.xmlDeserialization.components.EnumDeserializer;
import org.uml.xmlDeserialization.components.ClassDeserializer;
import java.util.ArrayList;
import java.util.Iterator;
import org.dom4j.Element;
import org.uml.model.components.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.EnumComponent;
import org.uml.model.relations.HasBaseRelation;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.UseRelation;

/**
 *
 * @author Stefan
 */
public class ClassDiagramDeserializer implements XmlDeserializer {

    private final ClassDiagram classDiagram;

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
        
        ArrayList<ComponentBase> components = new ArrayList<>(classDiagram.getComponents());
        Element classDiagramRelations = node.element("ClassDiagramRelations");
        Iterator<?> isRelationIterator = classDiagramRelations.elementIterator("IsRelation");
        while (isRelationIterator != null && isRelationIterator.hasNext()) {
            Element isRelationNode = (Element) isRelationIterator.next();
            
            IsRelation relation = new IsRelation();
            IsRelationDeserializer deserializer = new IsRelationDeserializer(relation, components);
            deserializer.deserialize(isRelationNode);
            classDiagram.addRelation(relation);
        }
        
        Iterator<?> hasRelationIterator = classDiagramRelations.elementIterator("HasRelation");
        while (hasRelationIterator != null && hasRelationIterator.hasNext()) {
            Element hasRelationNode = (Element) hasRelationIterator.next();
            
            HasBaseRelation relation = HasRelationDeserializer.deserialize(hasRelationNode, components);
            classDiagram.addRelation(relation);
        }
        
        Iterator<?> useRelationIterator = classDiagramRelations.elementIterator("UseRelation");
        while (useRelationIterator != null && useRelationIterator.hasNext()) {
            Element useRelationNode = (Element) useRelationIterator.next();
            
            UseRelation relation = new UseRelation();
            UseRelationDeserializer deserializer = new UseRelationDeserializer(relation, components);
            deserializer.deserialize(useRelationNode);
            classDiagram.addRelation(relation);
        }
        
        Iterator<?> implementsRelationIterator = classDiagramRelations.elementIterator("ImplementsRelation");
        while (implementsRelationIterator != null && implementsRelationIterator.hasNext()) {
            Element implementsRelationNode = (Element) implementsRelationIterator.next();
            
            ImplementsRelation relation = new ImplementsRelation();
            ImplementsRelationDeserializer deserializer = new ImplementsRelationDeserializer(relation, components);
            deserializer.deserialize(implementsRelationNode);
            classDiagram.addRelation(relation);
        }
    }
}
