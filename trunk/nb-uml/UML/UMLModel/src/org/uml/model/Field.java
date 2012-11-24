package org.uml.model;

/**
 *
 * @author Uros
 */
public class Field extends Member /* treba da nasledjuje member a u membberu da bude visibility*/{
    
        private String type;
        // these two should go to Member class, and provide nice API 
        private Visibility visibility;  
        private boolean isStatic;

        // provide constructor which takes  declaringClass param
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
