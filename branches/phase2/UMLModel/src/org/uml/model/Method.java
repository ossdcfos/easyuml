package org.uml.model;

import java.util.HashMap;

/**
 * Implementation of Method, one of 4 possible Members.
 * @author Uros
 * @see Member
 * @see Field
 * @see Literal
 * @see Constructor
 */
public class Method extends Member {
    
    private String returnType;
    private HashMap <String,MethodArgument> arguments;

    public Method(String name, String returnType, HashMap<String,MethodArgument> arguments) {
        super(name);
        this.returnType = returnType;
        this.arguments = arguments;
        visibility = Visibility.PUBLIC;
        //this.returnType="void";
    }

    public Method(String name) {
        super(name);
        arguments = new HashMap<String, MethodArgument>();
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
