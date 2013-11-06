package org.uml.model;

import java.util.HashMap;

/**
 *
 * @author zoran
 */
public class Constructor extends Member {

    private String returnType;
    private HashMap <String,MethodArgument> arguments;

    public Constructor(String name) {
        super(name);
    }
    
    public Constructor(String name, String returnType, HashMap<String,MethodArgument> arguments) {
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
    
}
