package org.nugs.uml.model;

import java.util.HashMap;

/**
 *
 * @author zoran
 */
public class Method {
    
    private String name;
    private String returnType;
    private HashMap <String, Argument> arguments;

    public Method(String name, String returnType, HashMap<String, Argument> arguments) {
        this.name = name;
        this.returnType = returnType;
        this.arguments = arguments;
    }

    public HashMap<String, Argument> getArguments() {
        return arguments;
    }

    public void setArguments(HashMap<String, Argument> arguments) {
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReturnType() {
        return returnType;
    }

    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }
    
    
}
