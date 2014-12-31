package org.uml.model.members;

import org.uml.model.Visibility;
import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.EnumComponent;
import org.uml.model.components.InterfaceComponent;

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

    protected LinkedHashSet<MethodArgument> arguments;

    /**
     * Constructor with three parameters used to set name, type of return
     * argument and input arguments(variables) of the Method. Calls the super
     * constructor.
     *
     * @param name of the Method being modeled
     * @param type of the Method being modeled
     */
    protected MethodBase(String name, String type) {
        super(name);
        this.type = type;
        this.visibility = Visibility.PUBLIC;
        this.arguments = new LinkedHashSet<>();
    }

    public LinkedHashSet<MethodArgument> getArguments() {
        return arguments;
    }

    public void setArguments(LinkedHashSet<MethodArgument> newArguments) {
        LinkedHashSet<MethodArgument> oldArguments = arguments;
        arguments = newArguments;
        pcs.firePropertyChange("arguments", oldArguments, newArguments);
    }

    @Override
    public String getSignature() {
        StringBuilder result = new StringBuilder();
        if (type != null) result.append(type).append(" ");
        result.append(getName()).append("(");
        String args = "";
        if (getArguments() != null) {
            for (MethodArgument argument : getArguments()) {
                args += argument.getType() + " " + argument.getName() + ", ";
            }
            if (!args.equals("")) {
                args = args.substring(0, args.length() - 2);
            }
        }
        result.append(args).append(")");
        return result.toString();
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
    @Override
    public String getLabelText() {
        StringBuilder result = new StringBuilder();
        // removes static because it is rendered as underline
        if ((modifiers & ~Modifier.STATIC) != 0) result.append(Modifier.toString(modifiers).replace("static ", "").replace("static", "")).append(" ");
        result.append(getSignature());
        return result.toString();
    }

    @Override
    public String deriveSignatureFromType(String newType){
        StringBuilder result = new StringBuilder();
        if (type != null) result = result.append(newType).append(" ");
        result.append(getName()).append("(");
        String args = "";
        if (getArguments() != null) {
            for (MethodArgument argument : getArguments()) {
                args += argument.getType() + " " + argument.getName() + ", ";
            }
            if (!args.equals("")) {
                args = args.substring(0, args.length() - 2);
            }
        }
        result.append(args).append(")");
        return result.toString();
    }

    @Override
    public String deriveSignatureFromName(String newName){
        StringBuilder result = new StringBuilder();
        if (type != null) result = result.append(type).append(" ");
        result.append(newName).append("(");
        String args = "";
        if (getArguments() != null) {
            for (MethodArgument argument : getArguments()) {
                args += argument.getType() + " " + argument.getName() + ", ";
            }
            if (!args.equals("")) {
                args = args.substring(0, args.length() - 2);
            }
        }
        result.append(args).append(")");
        return result.toString();
    }

//    /**
//     * Creates a string that represents Method's signature.
//     * <p>
//     * By concatenating strings a text is created, which can be used to
//     * represent this Field in the UML diagram's class that holds it e.g.
//     * "public static String firstMethod(int theNumber)".
//     *
//     *
//     * @return specially formed Method's String representation
//     */
//    public String getFullSignature() {
//        StringBuilder result = new StringBuilder();
//        if (visibility != null && !Visibility.PACKAGE.equals(visibility))
//            result = result.append(getVisibility().toString()).append(" ");
//        if(modifiers != 0) result.append(Modifier.toString(modifiers)).append(" ");
//        result.append(getSignature());
//        result.append(" {}\n");
//        return result.toString();
//    }

//    /**
//     * Method used to check if Method object's modifier array contains the
//     * modifier given as an input argument.
//     * <p>
//     * Used to check if Method's access and non-access modifiers.
//     *
//     * @param modifier to be checked
//     * @return true if method has that modifier, negative if it doesn't
//     */
//    public boolean hasConcreteModifier(String modifier) {
//        return Modifier.toString(modifiers).contains(modifier);
//    }
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
//    @Override
//    public String toString() {
//        return getSignature();
//    }
}
