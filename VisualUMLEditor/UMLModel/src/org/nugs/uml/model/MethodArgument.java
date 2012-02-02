package org.nugs.uml.model;


public class MethodArgument {
    String type;
    String name;

    public MethodArgument(String type, String name) {
        this.type = type;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Argument{" + "type=" + type + ", name=" + name + '}';
    }
    
}
