package org.uml.model;

/**
 * Contains all four possible types of RelationComponents.
 *
 * @author zoran
 * @see RelationComponent
 * @see IsRelationComponent
 * @see HasRelationComponent
 * @see UseRelationComponent
 * @see ImplementsRelationComponent
 */
public enum CardinalityEnum {

    /**
     * "1..1" relation
     */
    One2One {
        @Override
        public String toString() {
            return "1..1";
        }
    },
    /**
     * "0..1" relation
     */
    Zero2One {
        @Override
        public String toString() {
            return "0..1";
        }
    },
    /**
     * "1..*" relation
     */
    One2Many {
        @Override
        public String toString() {
            return "1..*";
        }
    },
    /**
     * "0..*" relation
     */
    Zero2Many {
        @Override
        public String toString() {
            return "0..*";
        }
    };

    /**
     * Converts a String object into adjacent CardinalityEnum element (element
     * with the same meaning). Case insensitive.
     *
     * @param cardinalityEnum
     * @return
     */
    public static CardinalityEnum parseString(String cardinalityEnum) {
        if (cardinalityEnum.equalsIgnoreCase("1..1")) {
            return CardinalityEnum.One2One;
        }
        if (cardinalityEnum.equalsIgnoreCase("0..1")) {
            return CardinalityEnum.Zero2One;
        }
        if (cardinalityEnum.equalsIgnoreCase("1..*")) {
            return CardinalityEnum.One2Many;
        }
        if (cardinalityEnum.equalsIgnoreCase("0..*")) {
            return CardinalityEnum.Zero2Many;
        }
        return CardinalityEnum.Zero2One;
    }
}
