package org.uml.model;

import java.lang.reflect.Type;

/**
 * Used to represent class fields (variables inside a class). Is one of four
 * possible Members.
 *
 * @author Uros
 * @see Member
 * @see Literal
 * @see Method
 * @see Constructor
 * @see ClassComponent
 * @see EnumComponent
 *
 */
public class Field extends Member /* treba da nasledjuje member a u membberu da bude visibility*/ {
    // sta ako je niz? da li treba koristiti Type?

    private String type;
    //these two should go to Member class, and provide nice API 
    private boolean isStatic;
    private boolean isFinal;
    private boolean isSynchronized;

    /**
     * Constructor that sets the name, type and visibility of the Field.
     *
     * @param name of the field
     * @param type of the field
     * @param visibility of the field
     */
    public Field(String name, String type, Visibility visibility) {
        super(name);
        this.type = type;
        this.visibility = visibility;
    }

    /**
     * Returns true if the field has static modifier, false if it is not static
     *
     * @return true if field is static, false if it isn't
     */
    public boolean isStatic() {
        return isStatic;
    }

    /**
     * Set this field's static modifier to true or false
     *
     * @param isStatic - set true if the field is static, false if it isn't
     */
    public void setStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    /**
     * Returns true if the field has final modifier, false if it is not final
     *
     * @return true if field is final, false if it isn't
     */
    public boolean isFinal() {
        return isFinal;
    }

    /**
     * Set this field's final modifier to true or false
     *
     * @param isFinal - set true if the field is final, false if it isn't
     */
    public void setFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    /**
     * Returns true if the field has synchronized modifier, false if it is not
     * synchronized
     *
     * @return true if field is synchronized, false if it isn't
     */
    public boolean isSynchronized() {
        return isSynchronized;
    }

    /**
     * Set this field's final synchronized to true or false
     *
     * @param isSynchronized - set true if the field is synchronized, false if
     * it isn't
     */
    public void setSynchronized(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }

    /**
     * Another way to set static, final or synchronized modifiers. Parses string
     * provided and sets adjacent bool modifier e.g. "static" sets isStatic to
     * true.
     *
     * @param modifier name to be set
     */
    public void setModifier(String modifier) {
        if ("static".equals(modifier)) {
            isStatic = true;
            return;
        }
        if ("final".equals(modifier)) {
            isFinal = true;
            return;
        }
        if ("synchronized".equals(modifier)) {
            isSynchronized = true;
        }

    }

    /**
     * Sets isFinal, isStatic and isSynchronized variables (modifiers) to false.
     */
    public void resetModifiers() {
        setFinal(false);
        setStatic(false);
        setSynchronized(false);
    }

    /**
     * Returns type of the field (variable)
     *
     * @return field's type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets field's type
     *
     * @param type of this field
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Creates a string that can be used to represent Field inside UML model.
     * <p>
     * By concatenating strings a text is created, which can be used to
     * represent this Field in the UML diagram's class that holds it e.g.
     * "static int fieldNuberOne".
     *
     * @return specially formed String representation of the Field
     */
    public String getSignatureForLabel() {
        String result = "";
        if (isStatic) {
            result = result.concat("static ");
        }
        if (isFinal) {
            result = result.concat("final ");
        }
        if (isSynchronized) {
            result = result.concat("synchronized ");
        }
        result = result.concat(type + " ");
        result = result.concat(getName());
        return result;
    }

    /**
     * Creates a string that represents Field's signature.
     * <p>
     * By concatenating strings a text is created, which can be used to
     * represent this Field in the UML diagram's class that holds it e.g.
     * "static int fieldNuberOne".
     *
     * @return specially formed Field's String representation
     */
    public String getSignature() {
        String result = "";
        if (visibility != null && !visibility.equals(Visibility.PACKAGE)) {
            result = result.concat(getVisibility().toString() + " ");
        }
        if (isStatic) {
            result = result.concat("static ");
        }
        if (isFinal) {
            result = result.concat("final ");
        }
        if (isSynchronized) {
            result = result.concat("synchronized ");
        }
        //potrebno je implementirati Type za filed
        //result = result.concat(type + " ");
        result = result.concat(type + " ");
        result = result.concat(getName() + ";\n");
        return result;
    }
}
