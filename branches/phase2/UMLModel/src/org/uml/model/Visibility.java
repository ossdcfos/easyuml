package org.uml.model;

/**
 * Enumeration which implements all posible types of access modifiers - private, protected, public and package.
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


}
