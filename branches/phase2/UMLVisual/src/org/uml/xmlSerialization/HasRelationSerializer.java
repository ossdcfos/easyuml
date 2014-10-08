/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import org.dom4j.Element;
import org.uml.model.CardinalityEnum;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.HasRelationComponent;
import org.uml.model.RelationComponent;

/**
 *
 * @author Stefan
 */
public class HasRelationSerializer implements RelationSerializer{

    private HasRelationComponent hasRelationComponent;
    
    /**
     * Sets the hasRelationComponent object that is going to be serialized.
     * @param component represents HasRelationComponent object to be serialized.
     */
    @Override
    public void addRelationComponent(RelationComponent relation) {
        try{
            hasRelationComponent = (HasRelationComponent) relation;
        }catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to HasRelationComponent!");
            e.printStackTrace();
        }
    }

    /**
     * Serializes hasRelationComponent object to XML by translating its fields into parameter node's attributes and subelements.
     * @param node represents the node that will contain serialized hasRelationComponent object.
     */
    @Override
    public void serialize(Element node) {
        if (hasRelationComponent.getName() != null) node.addAttribute("name", hasRelationComponent.getName());
        ClassDiagramComponent sourceComponent = hasRelationComponent.getSource();
        ClassDiagramComponent targetComponent = hasRelationComponent.getTarget();
        if (sourceComponent != null) { 
            node.addAttribute("source", sourceComponent.getName());
        }else {
            node.addAttribute("source", "");
        }
        if (targetComponent != null) {
            node.addAttribute("target", targetComponent.getName());
        }else {
            node.addAttribute("target", "");
        }
        CardinalityEnum sourceCardinality = hasRelationComponent.getCardinalitySource();
        CardinalityEnum targetCardinality = hasRelationComponent.getCardinalityTarget();
        if (sourceCardinality != null) {
            node.addAttribute("sourceCardinality", sourceCardinality.toString());
        }else {
            node.addAttribute("sourceCardinality", CardinalityEnum.Zero2One.toString());
        }
        if (targetCardinality != null) {
            node.addAttribute("targetCardinality", targetCardinality.toString());
        }else {
            node.addAttribute("targetCardinality", CardinalityEnum.Zero2One.toString());
        }
        String collectionType = hasRelationComponent.getCollectionType();
        if (collectionType != null && !collectionType.equals("")) {
            node.addAttribute("collectionType", collectionType);
        }else if (targetCardinality.equals(CardinalityEnum.One2Many) || targetCardinality.equals(CardinalityEnum.Zero2Many)) {
            node.addAttribute("collectionType", "List");
        }
    }
    
}
