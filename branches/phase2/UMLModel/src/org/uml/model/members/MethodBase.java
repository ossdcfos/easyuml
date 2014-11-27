package org.uml.model.members;

import org.uml.model.Visibility;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Methods used inside Classes, Enumerations and Interfaces.
 *
 * @author Uros
 * @see ClassComponent
 * @see EnumComponent
 * @see InterfaceComponent
 * @see MemberBase
 * @see Literal
 * @see Field
 * @see Constructor
 */
public abstract class MethodBase extends MemberBase {

    protected String returnType;
    protected HashMap<String, MethodArgument> arguments;
    
    /**
     * Constructor with three parameters used to set name, type of return
     * argument and input arguments(variables) of the Method. Calls the super
     * constructor.
     *
     * @param name of the Method being modeled
     * @param returnType of the Method being modeled
     * @param arguments - collection of input arguments
     */
    public MethodBase(String name, String returnType, HashMap<String, MethodArgument> arguments) {
        super(name);
        this.returnType = returnType;
        this.arguments = arguments;
        this.visibility = Visibility.PUBLIC;
        //this.returnType="void";
    }

    /**
     * Constructor with parameter for setting Method's name. Only sets the name
     * parameter.
     * <p>
     * Calls super constructor.
     *
     * @param name of the method being modeled
     */
    public MethodBase(String name) {
        super(name);
        arguments = new HashMap<>();
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

    /**
     * Adds numerical representation of java Modifier enum's constants into
     * modifiers array.
     * <p>
     * Modifiers array can hold up to four modifier constants. Modifiers are
     * thoroughly explained in Member class.
     *
     * @param modifier to be added to modifiers array
     * @see java.lang.reflect.Modifier
     * @see MemberBase
     */
    public void addModifier(int modifier) {
        modifiers |= modifier;
    }

    /**
     * Removes given modifier integer from modifiers array.
     * <p>
     * Modifiers are thoroughly explained in Member class.
     *
     * @param modifier to be removed
     * @see Modifier
     */
    public void deleteModifier(int modifier) {
        modifiers &= ~modifier;
    }

    /**
     * Returns String representation of modifiers array, concatenated with one
     * blank space in between (" ").
     *
     * @return concatenated String representation of modifiers array
     */
    public String getModifiersAsString() {
        return Modifier.toString(modifiers);
    }

    public void resetModifiers() {
        modifiers = 0;
    }

    /**
     * Method used to check if Method object's modifier array contains the
     * modifier given as an input argument.
     * <p>
     * Used to check if Method's access and non-access modifiers.
     *
     * @param modifier to be checked
     * @return true if method has that modifier, negative if it doesn't
     */
    public boolean hasConcreteModifier(String modifier) {
        return Modifier.toString(modifiers).contains(modifier);
    }

    /**
     * Creates a string that can be used to represent Method inside UML model.
     * <p>
     * By concatenating strings a text is created, which can be used to
     * represent this Method in the UML diagram's class that holds it e.g.
     * "public static String firstMethod(int theNumber)".
     *
     * @return specially formed String representation of the Method
     */
    public String getSignatureForLabel() {
        StringBuffer result = new StringBuffer();
        result.append(Modifier.toString(modifiers));

        if(returnType != null) result = result.append(returnType).append(" ");
        result.append(getName()).append("(");
        String args = "";
        if (getArguments() != null) {
            for (MethodArgument argument : getArguments().values()) {
                args += argument.getType() + " " + argument.getName() + ", ";
            }
        }
        if (!args.equals("")) {
            args = args.substring(0, args.length() - 2);
        }
        result.append(args).append(")");
        return result.toString();
    }

    /**
     * Creates a string that represents Method's signature.
     * <p>
     * By concatenating strings a text is created, which can be used to
     * represent this Field in the UML diagram's class that holds it e.g.
     * "public static String firstMethod(int theNumber)".
     *
     *
     * @return specially formed Method's String representation
     */
    public String getSignature() {
        StringBuffer result = new StringBuffer();
        if (visibility != null && !Visibility.PACKAGE.equals(visibility)) {
            result = result.append(getVisibility().toString()).append(" ");
        }
        result.append(getSignatureForLabel());
        result.append(" {}\n");
        return result.toString();
    }

//    @Override
//    public int hashCode() {
//        int hash = 3;
//        hash = 17 * hash + super.hashCode();
//        hash = 17 * hash + Objects.hashCode(getSignature());
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (!super.equals(obj)) return false;
//        if (obj == null) return false;
//        if (getClass() != obj.getClass()) return false;
//        final MethodBase other = (MethodBase) obj;
//        if (!Objects.equals(this.getSignature(), other.getSignature())) return false;
//        return true;
//    }

    @Override
    public String toString() {
        return getSignature();
    }
    
}
