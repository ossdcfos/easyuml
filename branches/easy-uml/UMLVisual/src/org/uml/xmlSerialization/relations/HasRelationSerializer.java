package org.uml.xmlSerialization.relations;

import org.dom4j.Element;
import org.uml.model.relations.CardinalityEnum;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.HasBaseRelation;
import org.uml.model.relations.RelationBase;

/**
 *
 * @author Stefan
 */
public class HasRelationSerializer implements RelationSerializer {

    private HasBaseRelation hasRelationComponent;

    /**
     * Sets the hasRelationComponent object that is going to be serialized.
     *
     * @param relation represents HasRelationComponent object to be serialized.
     */
    @Override
    public void setClassDiagramRelation(RelationBase relation) {
        try {
            hasRelationComponent = (HasBaseRelation) relation;
        } catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to HasRelationComponent!");
            e.printStackTrace();
        }
    }

    /**
     * Serializes hasRelationComponent object to XML by translating its fields into parameter node's attributes and subelements.
     *
     * @param node represents the node that will contain serialized hasRelationComponent object.
     */
    @Override
    public void serialize(Element node) {
        if (hasRelationComponent.getName() != null) node.addAttribute("name", hasRelationComponent.getName());
        ComponentBase sourceComponent = hasRelationComponent.getSource();
        ComponentBase targetComponent = hasRelationComponent.getTarget();
        node.addAttribute("source", sourceComponent.getSignature());
        node.addAttribute("target", targetComponent.getSignature());
        
        node.addAttribute("type", hasRelationComponent.getType().toString());

        CardinalityEnum sourceCardinality = hasRelationComponent.getCardinalitySource();
        CardinalityEnum targetCardinality = hasRelationComponent.getCardinalityTarget();
        // TODO fix this without null, default value
        if (sourceCardinality != null) {
            node.addAttribute("sourceCardinality", sourceCardinality.toString());
        } else {
            node.addAttribute("sourceCardinality", CardinalityEnum.Zero2One.toString());
        }
        
        if (targetCardinality != null) {
            node.addAttribute("targetCardinality", targetCardinality.toString());
        } else {
            node.addAttribute("targetCardinality", CardinalityEnum.Zero2One.toString());
        }
        
        String collectionType = hasRelationComponent.getCollectionType();
        if (collectionType != null && !collectionType.equals("")) {
            node.addAttribute("collectionType", collectionType);
        } else if (targetCardinality.equals(CardinalityEnum.One2Many) || targetCardinality.equals(CardinalityEnum.Zero2Many)) {
            node.addAttribute("collectionType", "List");
        }
    }
}
