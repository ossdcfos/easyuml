/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import org.dom4j.Element;
import org.uml.model.CardinalityEnum;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.RelationComponent;
import org.uml.model.UseRelationComponent;

/**
 *
 * @author Stefan
 */
public class UseRelationSerializer implements RelationSerializer{

    private UseRelationComponent useRelationComponent;
    
    @Override
    public void addRelationComponent(RelationComponent relation) {
        try{
            useRelationComponent = (UseRelationComponent) relation;
        }catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to UseRelationComponent!");
            e.printStackTrace();
        }
    }

    /**
     * Serializes useRelationComponent object to XML by translating its fields into parameter node's attributes and subelements.
     * @param node represents the node that will contain serialized useRelationComponent object.
     */
    @Override
    public void serialize(Element node) {
        if (useRelationComponent.getName() != null) node.addAttribute("name", useRelationComponent.getName());
        ClassDiagramComponent sourceComponent = useRelationComponent.getSource();
        ClassDiagramComponent targetComponent = useRelationComponent.getTarget();
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
        CardinalityEnum sourceCardinality = useRelationComponent.getCardinalitySource();
        CardinalityEnum targetCardinality = useRelationComponent.getCardinalityTarget();
        if (sourceCardinality != null) node.addAttribute("sourceCardinality", sourceCardinality.toString());
        if (targetCardinality != null) node.addAttribute("targetCardinality", targetCardinality.toString());
    }
    
}
