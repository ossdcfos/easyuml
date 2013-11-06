/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

/**
 *
 * @author "NUGS"
 */
public class UseRelationComponent extends RelationComponent{

    private CardinalityEnum cardinalitySource;
    private CardinalityEnum cardinalityTarget;
    
    @Override
    public String toString() {
        return "Use";
    }

    public CardinalityEnum getCardinalitySource() {
        return cardinalitySource;
    }

    public void setCardinalitySource(CardinalityEnum cardinalitySource) {
        this.cardinalitySource = cardinalitySource;
    }

    public CardinalityEnum getCardinalityTarget() {
        return cardinalityTarget;
    }

    public void setCardinalityTarget(CardinalityEnum cardinalityTarget) {
        this.cardinalityTarget = cardinalityTarget;
    }
    
}
