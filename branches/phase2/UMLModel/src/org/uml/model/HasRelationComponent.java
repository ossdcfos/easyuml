package org.uml.model;

/**
 * Has relation in UML class diagrams. Describes relation used usually when an
 * object has another object as its field.
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

    /**
     * Returns a CardinalityEnum element that describes the cardinality at
     * source side of a relation
     *
     * @return cardinality at source
     * @see CardinalityEnum
     */
    public CardinalityEnum getCardinalitySource() {
        return cardinalitySource;
    }

    /**
     * Sets the Cardinality at source side of a relation
     *
     * @param cardinalitySource cardinality at source of a relation
     * @see CardinalityEnum
     */
    public void setCardinalitySource(CardinalityEnum cardinalitySource) {
        this.cardinalitySource = cardinalitySource;
    }

    /**
     * Returns a CardinalityEnum element that describes the cardinality at
     * target side of a relation
     *
     * @return cardinality at target
     * @see CardinalityEnum
     */
    public CardinalityEnum getCardinalityTarget() {
        return cardinalityTarget;
    }

    /**
     * Sets the Cardinality at target side of a relation
     *
     * @param cardinalitySource cardinality at target of a relation
     * @see CardinalityEnum
     */
    public void setCardinalityTarget(CardinalityEnum cardinalityTarget) {
        this.cardinalityTarget = cardinalityTarget;
    }

    /**
     * Returns String description of a collection used to contain a field by
     * which the relation is made.
     * <p>
     * For example, if ClassOne has a List of ClassTwo objects as a field, this
     * method would return "List".
     *
     * @return string description (representation) of a collection
     */
    public String getCollectionType() {
        return collectionType;
    }

    /**
     * Sets string representation of a collection in which target class type
     * field is contained in source class, described by this relation.
     * <p>
     * For example, if ClassOne has a List of ClassTwo objects as a field,
     * "List" should be passed as a parameter.
     *
     * @param collectionType
     */
    public void setCollectionType(String collectionType) {
        this.collectionType = collectionType;
    }
}
