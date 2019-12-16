package org.uml.model.relations;

/**
 * Defines all four combinations of cardinalities.
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
            },
    /**
     * No cardinality
     */
    Nothing {
                @Override
                public String toString() {
                    return "";
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
        if (cardinalityEnum.equalsIgnoreCase("")) {
            return CardinalityEnum.Nothing;
        }
        return CardinalityEnum.Zero2One;
    }

//    public static final class CardinalityEnumConverter implements SingleValueConverter {
//
//        @Override
//        public String toString(Object o) {
//            CardinalityEnum cardinality = (CardinalityEnum) o;
//            return cardinality.toString();
//        }
//
//        @Override
//        public Object fromString(String string) {
//            return CardinalityEnum.parseString(string);
//        }
//
//        @Override
//        public boolean canConvert(Class type) {
//            return type.equals(CardinalityEnum.class);
//        }
//    }
}
