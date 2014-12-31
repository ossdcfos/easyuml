package org.uml.model.relations;

import java.util.Objects;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;

/**
 * Has relation in UML class diagrams. Describes relation used usually when an
 * object has another object as its field.
 *
 * @author "NUGS"
 * @see RelationBase
 * @see IsRelationComponent
 * @see UseRelationComponent
 * @see ImplementsRelationComponent
 */
public abstract class HasBaseRelation extends RelationBase {
    //Usually 0..*

    public static enum Type {

        AGGREGATION {
                    @Override
                    public String toString() {
                        return "Aggregation";
                    }
                },
        COMPOSITION {
                    @Override
                    public String toString() {
                        return "Composition";
                    }
                }
    }

    private CardinalityEnum cardinalitySource;
    //Usually 0..1

    private CardinalityEnum cardinalityTarget;
    /**
     * Can be List, ArrayList or LinkedList
     */
    private String collectionType;
    private Type type;

    // if not composition, than it is aggregation
    protected HasBaseRelation(Type type) {
        this.type = type;
    }

    /**
     * Returns the name of relation.
     *
     * @return "Has"
     */
    @Override
    public String toString() {
        return "Has ("+type+")";
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
     * @param cardinalityTarget cardinality at target of a relation
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

    public boolean isComposition() {
        return type == Type.COMPOSITION;
    }

    @Override
    public boolean canConnect(ComponentBase source, ComponentBase target) {
        Class<?> sc = source.getClass();

        if (sc == ClassComponent.class) return true;

        return false;
    }

    // TODO see hashes
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.source);
        hash = 53 * hash + Objects.hashCode(this.target);
        hash = 53 * hash + Objects.hashCode(this.cardinalitySource);
        hash = 53 * hash + Objects.hashCode(this.cardinalityTarget);
        hash = 53 * hash + Objects.hashCode(this.collectionType);
        hash = 53 * hash + Objects.hashCode(this.type);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (!super.equals(obj)) return false;
        HasBaseRelation other = (HasBaseRelation) obj;
        if (!Objects.equals(this.name, other.name)) return false;
        if (getClass() != obj.getClass()) return false;
        if (this.type != other.type) return false;
        return true;
    }

}
