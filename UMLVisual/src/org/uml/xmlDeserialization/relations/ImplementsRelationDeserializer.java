package org.uml.xmlDeserialization.relations;

import java.util.List;
import org.dom4j.Element;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.ImplementsRelation;
import org.uml.xmlDeserialization.XmlDeserializer;

/**
 *
 * @author stefanpetrovic
 */
public class ImplementsRelationDeserializer implements XmlDeserializer {

    private ImplementsRelation relation;
    private List<ComponentBase> components;

    public ImplementsRelationDeserializer(ImplementsRelation relation, List<ComponentBase> components) {
        this.relation = relation;
        this.components = components;
    }

    /**
     * Fills in attributes of relation object by reading attributes and
     * sub-nodes of the node input element.
     *
     * @param node is XML tree node whose attributes and sub-nodes represent
     * attributes of the relation object.
     */
    @Override
    public void deserialize(Element node) {
        String name = node.attributeValue("name");
        String source = node.attributeValue("source");
        String target = node.attributeValue("target");
        
        if (name != null) relation.setName(name);
        for (ComponentBase component : components) {
            if (source != null && component.getSignature().equals(source)) {
                relation.setSource(component);
            } else if (target != null && component.getSignature().equals(target)) {
                relation.setTarget(component);
            }
        }
    }
}
