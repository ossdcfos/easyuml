package org.uml.model;

/**
 * Enumeration which implements all possible types of access modifiers - private, protected, public and package.
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

    
    public static Visibility stringToVisibility(String value) {
        if("public".equalsIgnoreCase(value)) return PUBLIC;
        if("private".equalsIgnoreCase(value)) return PRIVATE;
        if("proteced".equalsIgnoreCase(value)) return PROTECTED;
        if("package".equalsIgnoreCase(value)) return PACKAGE;
        return null;
    }

}
