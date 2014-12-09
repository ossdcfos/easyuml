package org.uml.model.members;

import java.lang.reflect.Modifier;
import org.uml.model.Visibility;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.EnumComponent;

/**
 * Represents a class field (variables inside a class). It extends Member class
 * and it is one of four possible Members.
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
        if (isStatic) {
            addModifier(Modifier.STATIC);
        } else {
            removeModifier(Modifier.STATIC);
        }
        pcs.firePropertyChange("isStatic", Modifier.isStatic(oldModifiers), isStatic());
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
        if (isFinal) {
            addModifier(Modifier.FINAL);
        } else {
            removeModifier(Modifier.FINAL);
        }
        pcs.firePropertyChange("isFinal", Modifier.isFinal(oldModifiers), isFinal());
    }

    /**
     * Returns true if the field has transient modifier, false if it is not
     * transient
     *
     * @return true if field is transient, false if it isn't
     */
    public boolean isTransient() {
        return Modifier.isTransient(modifiers);
    }

    /**
     * Set this field's final transient to true or false
     *
     * @param isTransient - set true if the field is transient, false if it
     * isn't
     */
    public void setTransient(boolean isTransient) {
        int oldModifiers = modifiers;
        if (isTransient) {
            addModifier(Modifier.TRANSIENT);
        } else {
            removeModifier(Modifier.TRANSIENT);
        }
        pcs.firePropertyChange("isTransient", Modifier.isTransient(oldModifiers), isTransient());
    }

    /**
     * Returns true if the field has volatile modifier, false if it is not
     * volatile
     *
     * @return true if field is volatile, false if it isn't
     */
    public boolean isVolatile() {
        return Modifier.isVolatile(modifiers);
    }

    /**
     * Set this field's final volatile to true or false
     *
     * @param isVolatile - set true if the field is volatile, false if it isn't
     */
    public void setVolatile(boolean isVolatile) {
        int oldModifiers = modifiers;
        if (isVolatile) {
            addModifier(Modifier.VOLATILE);
        } else {
            removeModifier(Modifier.VOLATILE);
        }
        pcs.firePropertyChange("isVolatile", Modifier.isVolatile(oldModifiers), isVolatile());
    }

    @Override
    public String getSignatureWithoutModifiers() {
        StringBuilder result = new StringBuilder();
        result = result.append(type).append(" ");
        result = result.append(getName());
        return result.toString();
    }

    /**
     * Creates a string that can be used to represent Field inside UML model.
     * <p>
     * By concatenating strings a text is created, which can be used to
     * represent this Field in the UML diagram's class that holds it e.g.
     * "static int fieldNumberOne".
     *
     * @return specially formed String representation of the Field
     */
    public String getSignatureForLabel() {
        StringBuilder result = new StringBuilder();
        // removes static because it is rendered as underline
        result.append(Modifier.toString(modifiers).replace("static ", "").replace("static", "")).append(" ");
        result.append(getSignatureWithoutModifiers());
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
    public String getFullSignature() {
        StringBuilder result = new StringBuilder();
        if (visibility != null && !visibility.equals(Visibility.PACKAGE)) {
            result = result.append(getVisibility().toString()).append(" ");
        }
        result.append(getSignatureForLabel());
        return result.toString();
    }

    @Override
    public String deriveNewSignatureWithoutModifiersFromName(String newName) {
        StringBuilder result = new StringBuilder();
        result = result.append(type).append(" ");
        result = result.append(newName);
        return result.toString();
    }

    @Override
    public String deriveNewSignatureWithoutModifiersFromType(String newType) {
        StringBuilder result = new StringBuilder();
        result = result.append(newType).append(" ");
        result = result.append(getName());
        return result.toString();
    }

//    /**
//     * Another way to set static, final or synchronized modifiers. Parses string
//     * provided and sets adjacent bool modifier e.g. "static" sets isStatic to
//     * true.
//     *
//     * @param modifier name to be set
//     */
//    public void setModifier(String modifier) {
//        switch (modifier) {
//            case "static":
//                setStatic(true);
//                break;
//            case "final":
//                setFinal(true);
//                break;
//            case "transient":
//                setTransient(true);
//                break;
//            case "volatile":
//                setVolatile(true);
//                break;
//        }
//    }

}
