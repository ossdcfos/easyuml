package org.uml.model.members;

import java.util.HashMap;

/**
 * Constructors from UML Class diagrams. Used to represent class
 * constructors (both with and without parameters).
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
     * Constructor with parameter for setting constructor's name. Only sets the
     * name parameter.
     * <p>
     * Calls super constructor.
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
     * constructor.
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

    /**
     * Returns the return type of the constructor
     *
     * @return String representation of constructor's return type
     */
    public String getReturnType() {
        return returnType;
    }

    /**
     * Sets the return type of this constructor
     *
     * @param returnType of the constructor
     */
    public void setReturnType(String returnType) {
        this.returnType = returnType;
    }

    /**
     * Returns all arguments that this constructor uses
     *
     * @return HashMap of constructor's arguments
     */
    public HashMap<String, MethodArgument> getArguments() {
        return arguments;
    }

    /**
     * Sets arguments to this constructor
     *
     * @param arguments that the constructor uses
     */
    public void setArguments(HashMap<String, MethodArgument> arguments) {
        this.arguments = arguments;
    }
}
