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
     * Returns true if the field has static modifier, false if it is not static
     *
     * @return true if field is static, false if it isn't
     */
    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    /**
     * Set this field's static modifier to true or false
     *
     * @param isStatic - set true if the field is static, false if it isn't
     */
    public void setStatic(boolean isStatic) {
        int oldModifiers = modifiers;
        if(isStatic) {
            modifiers |= Modifier.STATIC;
        } else {
            modifiers &= ~Modifier.STATIC;
        }
        fire("isStatic", Modifier.isStatic(oldModifiers), isStatic());
    }

    /**
     * Returns true if the field has final modifier, false if it is not final
     *
     * @return true if field is final, false if it isn't
     */
    public boolean isFinal() {
        return Modifier.isFinal(modifiers);
    }

    /**
     * Set this field's final modifier to true or false
     *
     * @param isFinal - set true if the field is final, false if it isn't
     */
    public void setFinal(boolean isFinal) {
        int oldModifiers = modifiers;
        if(isFinal) {
            modifiers |= Modifier.FINAL;
        } else {
            modifiers &= ~Modifier.FINAL;
        }
        fire("isFinal", Modifier.isFinal(oldModifiers), isFinal());
    }

    /**
     * Returns true if the field has synchronized modifier, false if it is not
     * synchronized
     *
     * @return true if field is synchronized, false if it isn't
     */
    public boolean isSynchronized() {
        return Modifier.isSynchronized(modifiers);
    }

    /**
     * Set this field's final synchronized to true or false
     *
     * @param isSynchronized - set true if the field is synchronized, false if
     * it isn't
     */
    public void setSynchronized(boolean isSynchronized) {
        int oldModifiers = modifiers;
        if(isSynchronized) {
            modifiers |= Modifier.SYNCHRONIZED;
        } else {
            modifiers &= ~Modifier.SYNCHRONIZED;
        }
        fire("isSynchronized", Modifier.isSynchronized(oldModifiers), isSynchronized());
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
