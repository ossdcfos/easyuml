package org.uml.model;

/**
 * Enumeration which represent all types of access modifiers -
 * private, protected, public and package.
 *
 * @author Uros
 */
public enum Visibility {

    PRIVATE {
                @Override
                public String toString() {
                    return "private";
                }

            },
    PROTECTED {
                @Override
                public String toString() {
                    return "protected";
                }

            },
    PUBLIC {
                @Override
                public String toString() {
                    return "public";
                }

            },
    PACKAGE {
                @Override
                public String toString() {
                    return "package";
                }
            };

    /**
     * Converts a String object into the corresponding Visibility element 
     * (element with the same name). Case insensitive. Used here, as Enum.valueOf(String string)
     * is case sensitive, so this provides more flexibility and cleaner code.
     * 
     * @param value - string representation of a modifier e.g. "public"
     * @return Visibility enumeration component. Returns PACKAGE as default value.
     */
    public static Visibility stringToVisibility(String value) {
        if(value.equalsIgnoreCase(PUBLIC.toString())) return PUBLIC;
        else if(value.equalsIgnoreCase(PRIVATE.toString())) return PRIVATE;
        else if(value.equalsIgnoreCase(PROTECTED.toString())) return PROTECTED;
        else if(value.equalsIgnoreCase(PACKAGE.toString())) return PACKAGE;
        else return PACKAGE;
    }
}
