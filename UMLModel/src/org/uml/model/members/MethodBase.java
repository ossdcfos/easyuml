package org.uml.model.members;

import org.uml.model.Visibility;
import java.util.LinkedHashSet;

/**
 * Base class for Methods and Constructors.
 *
 * @author Uros
 * @see Method
 * @see Constructor
 */
public abstract class MethodBase extends MemberBase {

    /**
     * Set of arguments of this method.
     */
    protected LinkedHashSet<MethodArgument> arguments;

    /**
     * Constructor sets the name and return type. Also sets Visibility to public
     * (as default) and initializes arguments set.
     *
     * @param name of the method
     * @param returnType of the method
     */
    protected MethodBase(String name, String returnType) {
        super(name);
        this.type = returnType;
        this.visibility = Visibility.PUBLIC;
        this.arguments = new LinkedHashSet<>();
    }

    /**
     * Returns the collection of arguments of this method.
     *
     * @return HashSet of arguments of this method
     */
    public LinkedHashSet<MethodArgument> getArguments() {
        return arguments;
    }

    /**
     * Sets the collection of arguments of this method.
     * Fires "arguments" property change event.
     *
     * @param newArguments of the method
     */
    public void setArguments(LinkedHashSet<MethodArgument> newArguments) {
        LinkedHashSet<MethodArgument> oldArguments = arguments;
        arguments = newArguments;
        pcs.firePropertyChange("arguments", oldArguments, newArguments);
    }

    /**
     * Returns the unique signature of this method.
     * It looks like "[returnType] name([arguments list])"
     *
     * @return unique signature of this member
     */
    @Override
    public String getSignature() {
        StringBuilder result = new StringBuilder();
        if (type != null) result.append(type).append(" ");
        result.append(getName()).append("(");
        if (getArguments() != null) {
            result.append(getArgumentsString(false));
        }
        result.append(")");
        return result.toString();
    }

    @Override
    public String getSimpleTypeSignature() {
        StringBuilder result = new StringBuilder();
        if (type != null) result.append(getSimpleType(type)).append(" ");
        result.append(getName()).append("(");
        if (getArguments() != null) {
            result.append(getArgumentsString(true));
        }
        result.append(")");
        return result.toString();
    }
    
    @Override
    public String getUMLSignature() {
        StringBuilder result = new StringBuilder();
        result.append(getName()).append("(");
        if (getArguments() != null) {
            result.append(getUMLArgumentsString(false));
        }
        result.append("): ");
        if (type != null) result.append(type);
        return result.toString();
    }

    @Override
    public String getSimpleTypeUMLSignature() {
        StringBuilder result = new StringBuilder();
        result.append(getName()).append("(");
        if (getArguments() != null) {
            result.append(getUMLArgumentsString(true));
        }
        result.append("): ");
        if (type != null) result.append(getSimpleType(type));
        return result.toString();
    }    

    @Override
    public String deriveSignatureFromName(String newName) {
        StringBuilder result = new StringBuilder();
        if (type != null) result = result.append(type).append(" ");
        result.append(newName).append("(");
        if (getArguments() != null) {
            result.append(getArgumentsString(false));
        }
        result.append(")");
        return result.toString();
    }

    @Override
    public String deriveSignatureFromType(String newType) {
        StringBuilder result = new StringBuilder();
        if (type != null) result = result.append(newType).append(" ");
        result.append(getName()).append("(");
        if (getArguments() != null) {
            result.append(getArgumentsString(false));
        }
        result.append(")");
        return result.toString();
    }

    /**
     * Returns the arguments list represented as a string.
     * @param isSimple whether simple type names should be used
     * @return 
     */
    private String getArgumentsString(boolean isSimple) {
        StringBuilder args = new StringBuilder();
        String delimiter = "";
        for (MethodArgument argument : getArguments()) {
            String argType = isSimple ? getSimpleType(argument.getType()) : argument.getType();
            args.append(delimiter).append(argType).append(" ").append(argument.getName());
            delimiter = ", ";
        }
        return args.toString();
    }
    
    private String getUMLArgumentsString(boolean isSimple) {
        StringBuilder args = new StringBuilder();
        String delimiter = "";
        for (MethodArgument argument : getArguments()) {
            String argType = isSimple ? getSimpleType(argument.getType()) : argument.getType();
            args.append(delimiter).append(argument.getName()).append(": ").append(argType);
            delimiter = ", ";
        }
        return args.toString();
    }    
}
