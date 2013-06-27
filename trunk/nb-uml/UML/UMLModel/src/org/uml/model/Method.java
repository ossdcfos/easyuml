package org.uml.model;

import java.util.HashMap;

/**
 *
 * @author Uros
 */
public class Method extends Member {
    
    private Visibility visibility;
    private String returnType;
    private HashMap <String,MethodArgument> arguments;

    public Method(String name, String returnType, HashMap<String,MethodArgument> arguments) {
        super(name);
        this.returnType = returnType;
        this.arguments = arguments;
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

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
    
    
}
