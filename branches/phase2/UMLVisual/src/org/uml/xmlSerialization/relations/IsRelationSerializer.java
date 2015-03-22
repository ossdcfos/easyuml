package org.uml.xmlSerialization.relations;

import org.dom4j.Element;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.RelationBase;

/**
 *
 * @author Stefan
 */
public class IsRelationSerializer implements RelationSerializer {

    private IsRelation isRelation;

    /**
     * Sets the isRelationComponent object that is going to be serialized.
     *
     * @param relation represents IsRelation object to be serialized.
     */
    @Override
    public void setClassDiagramRelation(RelationBase relation) {
        try {
            isRelation = (IsRelation) relation;
        } catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to IsRelationComponent!");
            e.printStackTrace();
        }
    }

    /**
     * Serializes isRelationComponent object to XML by translating its fields into parameter node's attributes and subelements.
     *
     * @param node represents the node that will contain serialized isRelationComponent object.
     */
    @Override
    public void serialize(Element node) {
        if (isRelation.getName() != null) node.addAttribute("name", isRelation.getName());
        
        ComponentBase sourceComponent = isRelation.getSource();
        ComponentBase targetComponent = isRelation.getTarget();
        node.addAttribute("source", sourceComponent.getSignature());
        node.addAttribute("target", targetComponent.getSignature());
    }
}
