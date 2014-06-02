package org.uml.model;

/**
 * Used to describe Use(s) relation in UML class diagrams. Describes relation
 * used usually when an object depends of another one - most often the "using"
 * end in a relationship will be aware of the other end it's using, but it's not
 * always the case the "used" end is aware who is using it. Implemented usually
 * when there is an input variable in a class' method which is type of another
 * class.
 *
 * @author "NUGS"
 * @see RelationComponent
 * @see HasRelationComponent
 * @see IsRelationComponent
 * @see ImplementsRelationComponent
 */
public class UseRelationComponent extends RelationComponent {

    private CardinalityEnum cardinalitySource;
    private CardinalityEnum cardinalityTarget;

    /**
     * Returns the name of relation
     *
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
