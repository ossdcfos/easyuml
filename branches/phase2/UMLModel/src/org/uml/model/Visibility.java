package org.uml.model;

/**
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


}
