package org.uml.model;

import java.util.HashMap;

/**
 *
 * @author Uros
 */
public class Method {
    
    private String name;
    private String returnType;
    private HashMap <String,MethodArgument> arguments;

    public Method(String name, String returnType, HashMap<String,MethodArgument> arguments) {
        this.name = name;
        this.returnType = returnType;
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

    public HashMap<String, MethodArgument> getArguments() {
        return arguments;
    }

    public void setArguments(HashMap<String, MethodArgument> arguments) {
        this.arguments = arguments;
    }
    
    
}
