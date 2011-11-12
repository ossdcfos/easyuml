package org.nugs.uml.model;

public enum Visibility {
    PRIVATE("private"), 
    PROTECTD("protected"),
    PUBLIC("public");
    
    private String stringLabel;
    
    private Visibility(String stringLabel) {
        this.stringLabel = stringLabel;
    }
    
    public String toString() {
        return stringLabel;
    }    
}
