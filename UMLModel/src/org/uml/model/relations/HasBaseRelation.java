package org.uml.model.relations;

import java.util.Objects;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;

/**
 * Base relation for has relations in UML class diagrams. Describes relation
 * used when an object has another object as its field.
 *
 * @author NUGS
 * @see AggregationRelation
 * @see CompositionRelation
 * @see HasRelation
 */
public abstract class HasBaseRelation extends RelationBase {

    /**
     * Enum describing the type of Has relation. Can be Default, Aggregation or
     * Composition.
     */
    public static enum Type {

        DEFAULT {
                    @Override
                    public String toString() {
                        return "Default";
                    }
                },
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

    /**
     * Type of the has relation.
     */
    private final Type type;

    /**
     * Cardinality of the source component in the relation.
     */
    private CardinalityEnum cardinalitySource;

    /**
     * Cardinality of the target component in the relation.
     */
    private CardinalityEnum cardinalityTarget;

    /**
     * Type of the collection if cardinality >1 is used. Can be List, ArrayList
     * or LinkedList by default, TODO but we should also enable other types.
     */
    private String collectionType;

    /**
     * Protected constructor used in subclasses to create a new Has relation.
     * Sets the type of relation to the given type.
     *
     * @param type of has relation
     */
    protected HasBaseRelation(Type type) {
        this.type = type;
    }

    /**
     * Returns type of has relation.
     *
     * @return type of has relation
     */
    public Type getType() {
        return type;
    }

    /**
     * Returns the name of the relation.
     *
     * @return "Has"
     */
    @Override
    public String toString() {
        return "Has (" + type + ")";
    }

    /**
     * Returns a CardinalityEnum element that describes the cardinality at
     * source side of the relation.
     *
     * @return cardinality at source
     * @see CardinalityEnum
     */
    public CardinalityEnum getCardinalitySource() {
        return cardinalitySource;
    }

    /**
     * Sets the Cardinality at source side of the relation
     *
     * @param newCardinalitySource cardinality at source of a relation
     * @see CardinalityEnum
     */
    public void setCardinalitySource(CardinalityEnum newCardinalitySource) {
        CardinalityEnum oldCardinalitySource = this.cardinalitySource;
        this.cardinalitySource = newCardinalitySource;
        pcs.firePropertyChange("cardinalitySource", oldCardinalitySource, newCardinalitySource);

    }

    /**
     * Returns a CardinalityEnum element that describes the cardinality at
     * target side of the relation
     *
     * @return cardinality at target
     * @see CardinalityEnum
     */
    public CardinalityEnum getCardinalityTarget() {
        return cardinalityTarget;
    }

    /**
     * Sets the Cardinality at target side of the relation
     *
     * @param newCardinalityTarget cardinality at target of a relation
     * @see CardinalityEnum
     */
    public void setCardinalityTarget(CardinalityEnum newCardinalityTarget) {
        CardinalityEnum oldCardinalityTarget = this.cardinalityTarget;
        this.cardinalityTarget = newCardinalityTarget;
        pcs.firePropertyChange("cardinalityTarget", oldCardinalityTarget, newCardinalityTarget);
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
     * Sets string representation of a collection in which the target class type
     * field is contained in source class.
     * <p>
     * For example, if ClassOne has a List of ClassTwo objects as a field,
     * "List" should be passed as a parameter.
     *
     * @param newCollectionType
     */
    public void setCollectionType(String newCollectionType) {
        String oldCollectionType = this.collectionType;
        this.collectionType = newCollectionType;
        pcs.firePropertyChange("collectionType", oldCollectionType, newCollectionType);
    }

    @Override
    public boolean canConnect(ComponentBase source, ComponentBase target) {
        Class<?> sc = source.getClass();

        if (sc == ClassComponent.class) {
            return true;
        }

        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.source);
        hash = 67 * hash + Objects.hashCode(this.target);
        hash = 67 * hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof HasBaseRelation)) {
            return false;
        }
        final RelationBase other = (RelationBase) obj;
        if (!Objects.equals(this.source, other.source)) {
            return false;
        }
        if (!Objects.equals(this.target, other.target)) {
            return false;
        }
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }

    public String getFieldSignature() {
        String fieldType = getFieldType();
        String fieldName;
        // Field name is relation name if it exists
        if (!getName().equals("")) {
            fieldName = getName();
        } // Else field name is lowercase type (class name)
        else {
            fieldName = (fieldType.substring(0, 1)).toLowerCase() + fieldType.substring(1);
        }

        StringBuilder fieldSignature = new StringBuilder();
        fieldSignature.append(fieldType).append(" ").append(fieldName);
        // If the cardinality is multiple add an s to the end of the name.
        if (getCardinalityTarget().equals(CardinalityEnum.One2Many) || getCardinalityTarget().equals(CardinalityEnum.Zero2Many)) {
            if (!getName().equals("")) {
                fieldSignature.append("s");
            }
        }
        return fieldSignature.toString();
    }

    public String deriveFieldSignatureFromName(String name) {
        String fieldType = getFieldType();
        String fieldName = name;

        StringBuilder fieldSignature = new StringBuilder();
        fieldSignature.append(fieldType).append(" ").append(fieldName);
        // If the cardinality is multiple add an s to the end of the name.
        if (getCardinalityTarget().equals(CardinalityEnum.One2Many) || getCardinalityTarget().equals(CardinalityEnum.Zero2Many)) {
            if (!getName().equals("")) {
                fieldSignature.append("s");
            }
        }
        return fieldSignature.toString();
    }

    private String getFieldType() {
        StringBuilder fieldType = new StringBuilder();
        // If the cardinality is multiple, generate appropriate collection
        if (getCardinalityTarget().equals(CardinalityEnum.One2Many) || getCardinalityTarget().equals(CardinalityEnum.Zero2Many)) {
            fieldType.append(getCollectionType()).append("<").append(target.getName()).append(">");
        } // Else generate the ordinary type
        else {
            fieldType.append(fieldType);
        }
        return fieldType.toString();
    }
}
