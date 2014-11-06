package org.uml.model.members;

/**
 * Enumeration which implements all possible types of access modifiers - 
 * private, protected, public and package.
 * 
 * @author Uros
 */
public enum Visibility {

    PRIVATE {
        @Override
        public String toString(){
            return "private";
        }
    
    },
    PROTECTED{
        @Override
        public String toString(){
            return "protected";
        }
    
    },
    PUBLIC{
        @Override
        public String toString(){
            return "public";
        }
    
    },
    PACKAGE{
        @Override
        public String toString() {
            return "";
        }
    };

    /**
     * Converts a String object into adjacent Visibility element (element with 
     * the same name). Case insensitive.
     * 
     * @param value - string representation of modifier e.g. "public"
     * @return Visibility enumeration component
     */
    public static Visibility stringToVisibility(String value) {
        if("public".equalsIgnoreCase(value)) return PUBLIC;
        if("private".equalsIgnoreCase(value)) return PRIVATE;
        if("protected".equalsIgnoreCase(value)) return PROTECTED;
        if("package".equalsIgnoreCase(value)) return PACKAGE;
        return null;
    }

}
