package org.uml.model;

import java.util.HashMap;

/**
 * Implementation of Constructor, one of four possible Members. It is used to
 * represent class constructors (both with and without parameters).
 *
 * @author zoran
 * @see Member
 * @see Literal
 * @see Field
 * @see Method
 * @see ClassComponent
 * @see EnumComponent
 */
public class Constructor extends Member {

    private String returnType;
    private HashMap<String, MethodArgument> arguments;

    /**
     * Constructor with parameter for setting Constructor's name. Only sets the
     * name parameter.
     * <p>
     * Calls super Constructor.
     *
     * @param name
     * @see Member
     */
    public Constructor(String name) {
        super(name);
    }

    /**
     * Constructor with three parameters used to set name, type of return
     * argument and input arguments(variables) of constructor. Calls the super
     * Constructor.
     *
     * @param name of the Constructor
     * @param returnType of Constructor
     * @param arguments - collection of arguments
     */
    public Constructor(String name, String returnType, HashMap<String, MethodArgument> arguments) {
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
