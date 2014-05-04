package org.uml.model;

/**
 * Implementation of one of four possible RelationComponents. Describes Has
 * relation between UML objects (used usually when an object has another object
 * as its field).
 *
 * @author "NUGS"
 * @see RelationComponent
 * @see IsRelationComponent
 * @see UseRelationComponent
 * @see ImplementsRelationComponent
 */
public class HasRelationComponent extends RelationComponent {

    private CardinalityEnum cardinalitySource;
    private CardinalityEnum cardinalityTarget;
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
