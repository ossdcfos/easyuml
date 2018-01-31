package org.uml.model.members;

import org.uml.model.components.ClassComponent;
import org.uml.model.components.EnumComponent;

/**
 * Constructors from UML Class diagrams. Used to represent class constructors
 * (both with and without parameters).
 *
 * @author zoran
 * @see MemberBase
 * @see Literal
 * @see Field
 * @see MethodBase
 * @see ClassComponent
 * @see EnumComponent
 */
public class Constructor extends MethodBase {

    /**
     * Constructor with parameter for setting constructor's name.
     * Name is the type for the constructor, so the type is set to null.
     * <p>
     * Calls super constructor.
     *
     * @param name of the constructor
     */
    public Constructor(String name) {
        super(name, null);
    }

    /**
     * Always returns false. Cannot add any non-access modifier to constructor.
     * @param modifier
     * @return 
     */
    @Override
    public boolean allowedToAddModifier(int modifier) {
        return false;
    }

    @Override
    public String getUMLSignature() {
        StringBuilder result = new StringBuilder();
        result.append(getName()).append("(");
        if (getArguments() != null) {
            result.append(getUMLArgumentsString(false));
        }
        result.append(")");
        return result.toString();
    }

    @Override
    public String getSimpleTypeUMLSignature() {
        StringBuilder result = new StringBuilder();
        result.append(getName()).append("(");
        if (getArguments() != null) {
            result.append(getUMLArgumentsString(true));
        }
        result.append(")");
        return result.toString();
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
    @Override
    public String getLabelText(boolean isSimpleTypeNames) {
        StringBuilder result = new StringBuilder();
        if (isSimpleTypeNames) result.append(getSimpleTypeUMLSignature());
        else result.append(getUMLSignature());
        return result.toString();
    }
}
