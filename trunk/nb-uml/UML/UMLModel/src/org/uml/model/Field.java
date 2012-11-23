package org.uml.model;

/**
 *
 * @author Uros
 */
public class Field extends Member /* treba da nasledjuje member a u membberu da bude visibility*/{
    
        private String type;
        private Visibility visibility;
        private boolean isStatic;

    public Field(String name, String type, Visibility visibility) {
        super(name);
        this.type = type;
        this.visibility = visibility;
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
