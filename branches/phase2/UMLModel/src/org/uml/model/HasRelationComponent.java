package org.uml.model;

/**
 * Used to describe Has relation in UML class diagrams. Describes relation used
 * usually when an object has another object as its field.
 *
 * @author "NUGS"
 * @see RelationComponent
 * @see IsRelationComponent
 * @see UseRelationComponent
 * @see ImplementsRelationComponent
 */
public class HasRelationComponent extends RelationComponent {
    //Usually 0..*

    private CardinalityEnum cardinalitySource;
    //Usually 0..1
    private CardinalityEnum cardinalityTarget;
    /**
     * Can be List, ArrayList or LinkedList
     */
    private String collectionType;

    /**
     * Returns the name of relation.
     *
     * @return "Has"
     */
    @Override
    public String toString() {
        return "Has";
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

    public String getCollectionType() {
        return collectionType;
    }

    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }
}
