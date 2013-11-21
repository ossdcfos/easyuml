package org.uml.model;

import java.util.HashMap;

/**
 * Implementation of Constructor, one of 4 possible Members.
 * @author zoran
 * @see Member
 * @see Literal
 * @see Method
 * @see Field
 */
public class Constructor extends Member {

    private String returnType;
    private HashMap <String,MethodArgument> arguments;

    /**
     * Constructor with parameter name which calls super constructor.
     * @param name 
     */
    public Constructor(String name) {
        super(name);
    }
    
    /**
     * Constructor with more parameters, also calls super constructor
     * @param name of Constructor
     * @param returnType
     * @param arguments collection of arguments
     */
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
