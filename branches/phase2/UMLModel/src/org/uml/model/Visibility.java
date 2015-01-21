package org.uml.model;

//import com.thoughtworks.xstream.annotations.XStreamConverter;
//import com.thoughtworks.xstream.converters.SingleValueConverter;

/**
 * Enumeration which implements all possible types of access modifiers -
 * private, protected, public and package.
 *
 * @author Uros
 */
//@XStreamConverter(Visibility.VisibilityConverter.class)
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

//    /**
//     * Converts a String object into adjacent Visibility element (element with 
//     * the same name). Case insensitive.
//     * 
//     * @param value - string representation of modifier e.g. "public"
//     * @return Visibility enumeration component
//     */
//    public static Visibility stringToVisibility(String value) {
//        if("public".equalsIgnoreCase(value)) return PUBLIC;
//        if("private".equalsIgnoreCase(value)) return PRIVATE;
//        if("protected".equalsIgnoreCase(value)) return PROTECTED;
//        if("package".equalsIgnoreCase(value)) return PACKAGE;
//        return null;
//    }
//    public static class VisibilityConverter implements SingleValueConverter {
//
//        @Override
//        public String toString(Object o) {
//            Visibility visiblity = (Visibility) o;
//            return visiblity.toString();
//        }
//
//        @Override
//        public Object fromString(String string) {
//            return Visibility.valueOf(string);
//        }
//
//        @Override
//        public boolean canConvert(Class type) {
//            return type.equals(Visibility.class);
//        }
//    }
}
