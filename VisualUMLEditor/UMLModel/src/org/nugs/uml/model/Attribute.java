package org.nugs.uml.model;

/**
 *
 * @author zoran
 */
public class Attribute {
    
    private String name;
    private String type;
    private Visibility visibility;
    private boolean isStatic;

    public Attribute(String name, String type, Visibility visibility) {
        this.name = name;
        this.type = type;
        this.visibility = visibility;
    }
    
    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
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
    
    
    
}
