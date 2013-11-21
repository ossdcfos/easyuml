package org.uml.model;

/**
 * Implementation of one of four possible RelationComponents.
 * @author "NUGS"
 * @see RelationComponent
 * @see IsRelationComponent
 * @see HasRelationComponent
 * @see ImplementsRelationComponent
 */
public class UseRelationComponent extends RelationComponent{

    private CardinalityEnum cardinalitySource;
    private CardinalityEnum cardinalityTarget;
    
     /**
     * Returns name of relation
     * @return "Use"
     */
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
