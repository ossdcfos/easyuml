package org.uml.model.members;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * Methods used inside Classes, Enumerations and Interfaces.
 *
 * @author Uros
 * @see ClassComponent
 * @see EnumComponent
 * @see InterfaceComponent
 * @see Member
 * @see Literal
 * @see Field
 * @see Constructor
 */
public class Method extends Member {

    private String returnType;
    private HashMap<String, MethodArgument> arguments;
    private int[] modifiers = new int[4]; // koji je cetvrti odifier? - abstract

    /**
     * Constructor with three parameters used to set name, type of return
     * argument and input arguments(variables) of the Method. Calls the super
     * constructor.
     *
     * @param name of the Method being modeled
     * @param returnType of the Method being modeled
     * @param arguments - collection of input arguments
     */
    public Method(String name, String returnType, HashMap<String, MethodArgument> arguments) {
        super(name);
        this.returnType = returnType;
        this.arguments = arguments;
        visibility = Visibility.PUBLIC;
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

    /**
     * Adds numerical representation of java Modifier enum's constants into
     * modifiers array.
     * <p>
     * Modifiers array can hold up to four modifier constants. Modifiers are
     * thoroughly explained in Member class.
     *
     * @param modifier to be added to modifiers array
     * @see java.lang.reflect.Modifier
     * @see Member
     */
    public void addModifier(int modifier) {
        boolean isModifierAllreadyAdded = false;
        int emptySpot = -1;
        for (int i = 0; i < modifiers.length; i++) {
            if (modifiers[i] <= 0 && emptySpot == -1) {
                emptySpot = i;
            }
            if (modifiers[i] == modifier) {
                isModifierAllreadyAdded = true;
                break;
            }
        }
        if (!isModifierAllreadyAdded) {
            modifiers[emptySpot] = modifier;
        }
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
        for (int i = 0; i < modifiers.length; i++) {
            if (modifiers[i] == modifier) {
                modifiers[i] = -1;
            }
        }
    }

    public int[] getModifiers() {
        return modifiers;
    }

    /**
     * Returns String representation of modifiers array, concatenated with one
     * blank space in between (" ").
     *
     * @return concatenated String representation of modifiers array
     */
    public String getModifiersAsString() {
        String result = "";
        for (int i = 0; i < modifiers.length; i++) {
            if (modifiers[i] > 0) {
                result = result.concat(Modifier.toString(modifiers[i]) + " ");
            }
        }
        return result;
    }

    public void setModifiers(int[] modifiers) {
        this.modifiers = modifiers;
    }

    public void resetModifiers() {
        for (int i = 0; i < modifiers.length; i++) {
            modifiers[i] = -1;
        }
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
        boolean hasModifier = false;
        for (int i = 0; i < modifiers.length; i++) {
            if (modifiers[i] > 0 && Modifier.toString(modifiers[i]).equals(modifier)) {
                hasModifier = true;
                break;
            }
        }
        return hasModifier;
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
        String result = "";
        for (int i = 0; i < 4; i++) {
            if (modifiers[i] > 0) {
                result = result.concat(Modifier.toString(modifiers[i]) + " ");
            }
        }

        result = result.concat(returnType + " ");
        result = result.concat(getName() + "(");
        String args = "";
        if (getArguments() != null) {
            for (Map.Entry<String, MethodArgument> entry1 : getArguments().entrySet()) {
                MethodArgument argument = entry1.getValue();
                args += argument.getType() + " " + argument.getName() + ", ";
            }
        }
        if (!args.equals("")) {
            args = args.substring(0, args.length() - 2);
        }
        result = result.concat(args + ")");
        return result;
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
        String result = "";
        if (visibility != null && !Visibility.PACKAGE.equals(visibility)) {
            result = result.concat(getVisibility().toString() + " ");
        }
        for (int i = 0; i < 4; i++) {
            if (modifiers[i] > 0) {
                result = result.concat(Modifier.toString(modifiers[i]) + " ");
            }
        }

        result = result.concat(returnType + " ");
        result = result.concat(getName() + "(");
        String args = "";
        if (getArguments() != null) {
            for (Map.Entry<String, MethodArgument> entry1 : getArguments().entrySet()) {
                MethodArgument argument = entry1.getValue();
                args += argument.getType() + " " + argument.getName() + ", ";
            }
        }
        if (!args.equals("")) {
            args = args.substring(0, args.length() - 2);
        }
        result = result.concat(args + ") {} \n");
        return result;
    }

    /**
     * Creates a string that represents Method's signature.
     * <p>
     * By concatenating strings a text is created, which can be used to
     * represent this Field in the UML diagram's class that holds it e.g.
     * "public static String firstMethod(int theNumber)". THIS IS VERSION FOR
     * INTERFACES
     *
     * @return specially formed Method's String representation
     */
    public String getSignatureForInterfaces() {
        String result = "";
        if (visibility != null && !Visibility.PACKAGE.equals(visibility)) {
            result = result.concat(getVisibility().toString() + " ");
        }
        for (int i = 0; i < 4; i++) {
            if (modifiers[i] > 0) {
                result = result.concat(Modifier.toString(modifiers[i]) + " ");
            }
        }

        result = result.concat(returnType + " ");
        result = result.concat(getName() + "(");
        String args = "";
        if (getArguments() != null) {
            for (Map.Entry<String, MethodArgument> entry1 : getArguments().entrySet()) {
                MethodArgument argument = entry1.getValue();
                args += argument.getType() + " " + argument.getName() + ", ";
            }
        }
        if (!args.equals("")) {
            args = args.substring(0, args.length() - 2);
        }
        result = result.concat(args + "); \n");
        return result;
    }
}
