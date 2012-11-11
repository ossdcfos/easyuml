package org.uml.model;

/**
 *
 * @author Uros
 */
public enum Visibility {
 
        PRIVATE("private"),
        PROTECTED("protected"),
        PUBLIC("public");
        
        private String stringLabel;

    private Visibility(String stringLabel) {
        this.stringLabel = stringLabel;
    }


    @Override
    public String toString() {
        return stringLabel;
    }
        
        
}
