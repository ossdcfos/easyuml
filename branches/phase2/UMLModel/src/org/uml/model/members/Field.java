package org.uml.model.members;

import java.lang.reflect.Modifier;
import org.uml.model.Visibility;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.EnumComponent;

/**
 * Represents a class field (variables inside a class). 
 * It extends Member class and it is one of four possible Members.
 *
 * @author Uros
 * @see MemberBase
 * @see Literal
 * @see Method
 * @see Constructor
 * @see ClassComponent
 * @see EnumComponent
 *
 */
public class Field extends MemberBase {
    // sta ako je niz? da li treba koristiti Type?

    private String type;
    //these two should go to Member class, and provide nice API

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
     * Another way to set static, final or synchronized modifiers. Parses string
     * provided and sets adjacent bool modifier e.g. "static" sets isStatic to
     * true.
     *
     * @param modifier name to be set
     */
    public void setModifier(String modifier) {
        switch(modifier){
            case "static":
                setStatic(true);
                break;
            case "final":
                setFinal(true);
                break;
            case "synchronized":
                setSynchronized(true);
                break;
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
        StringBuilder result = new StringBuilder();
        result.append(Modifier.toString(modifiers)).append(" ");
        result = result.append(type).append(" ");
        result = result.append(getName());
        return result.toString();
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
        StringBuilder result = new StringBuilder();
        if (visibility != null && !visibility.equals(Visibility.PACKAGE)) {
            result = result.append(getVisibility().toString()).append(" ");
        }
        result.append(getSignatureForLabel());
        return result.toString();
    }
    
}
