package org.uml.xmlSerialization.relations;

import org.dom4j.Element;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.RelationBase;

/**
 *
 * @author Stefan
 */
public class ImplementsRelationSerializer implements RelationSerializer {

    private ImplementsRelation implementsRelation;

    /**
     * Sets the implementsRelationComponent object that is going to be serialized.
     *
     * @param component represents implementsRelationComponent object to be serialized.
     */
    @Override
    public void setClassDiagramRelation(RelationBase relation) {
        try {
            implementsRelation = (ImplementsRelation) relation;
        } catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to ImplementsRelationComponent!");
            e.printStackTrace();
        }
    }

    /**
     * Serializes implementsRelationComponent object to XML by translating its fields into parameter node's attributes and subelements.
     *
     * @param node represents the node that will contain serialized ImplementsRelationComponent object.
     */
    @Override
    public void serialize(Element node) {
        if (implementsRelation.getName() != null) node.addAttribute("name", implementsRelation.getName());
        
        ComponentBase sourceComponent = implementsRelation.getSource();
        ComponentBase targetComponent = implementsRelation.getTarget();
        node.addAttribute("source", sourceComponent.getSignature());
        node.addAttribute("target", targetComponent.getSignature());
    }
}
