package org.uml.model;

/**
 *
 * @author Uros
 */
public class Field {
    
        private String name;
        private String type;
        private Visibility visibility;
        private boolean isStatic;

    public Field(String name, String type, Visibility visibility) {
        this.name = name;
        this.type = type;
        this.visibility = visibility;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }
        
        
}
