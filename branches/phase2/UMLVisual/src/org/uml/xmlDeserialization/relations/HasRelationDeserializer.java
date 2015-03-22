package org.uml.xmlDeserialization.relations;

import java.util.List;
import org.dom4j.Element;
import org.uml.model.relations.CardinalityEnum;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.AggregationRelation;
import org.uml.model.relations.CompositionRelation;
import org.uml.model.relations.HasBaseRelation;
import org.uml.model.relations.HasBaseRelation.Type;
import org.uml.model.relations.HasRelation;

/**
 *
 * @author stefanpetrovic
 */
public class HasRelationDeserializer {
    
    /**
     * Fills in attributes of relation object by reading attributes and
     * sub-nodes of the node input element.
     *
     * @param node is XML tree node whose attributes and sub-nodes represent
     * attributes of the relation object.
     */
    public static HasBaseRelation deserialize(Element node, List<ComponentBase> components) {
        HasBaseRelation relation;
        String name = node.attributeValue("name");
        String source = node.attributeValue("source");
        String target = node.attributeValue("target");
        Type type = Type.valueOf(node.attributeValue("type").toUpperCase());
        switch(type){
            case AGGREGATION:
                relation = new AggregationRelation();
                break;
            case COMPOSITION:
                relation = new CompositionRelation();
                break;
            case DEFAULT:
                relation = new HasRelation();
                break;
            default :
                relation = new HasRelation();
                break;
        }
        if (name != null) relation.setName(name);
        for (ComponentBase component : components) {
            if (source != null && component.getSignature().equals(source)) {
                relation.setSource(component);
            }
            if (target != null && component.getSignature().equals(target)) {
                relation.setTarget(component);
            }
        }
        
        String sourceCardinality = node.attributeValue("sourceCardinality");
        String targetCaridnality = node.attributeValue("targetCardinality");
        relation.setCardinalitySource(CardinalityEnum.parseString(sourceCardinality));
        relation.setCardinalityTarget(CardinalityEnum.parseString(targetCaridnality));
        
        String collectionType = node.attributeValue("collectionType");
        if (collectionType != null) relation.setCollectionType(collectionType);
        
        return relation;
    }
}
