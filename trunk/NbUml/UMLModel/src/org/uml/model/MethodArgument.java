package org.uml.model;

/**
 *
 * @author Uros
 */
public class MethodArgument {
    
    String type;
    String name;

    public MethodArgument(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MethodArgument{" + "type=" + type + ", name=" + name + '}';
    }


    
}
