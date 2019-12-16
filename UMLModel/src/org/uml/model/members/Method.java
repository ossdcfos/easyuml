package org.uml.model.members;

import java.lang.reflect.Modifier;

/**
 * Represents a method (methods inside a class or an interface).
 *
 * @author Boris Perović
 * @see MethodBase
 * @see Field
 * @see Constructor
 * @see Literal
 */
public class Method extends MethodBase implements Cloneable {

    /**
     * Constructor that sets the name, return type and visibility of the method.
     *
     * @param name of the method
     * @param returnType of the method
     */
    public Method(String name, String returnType) {
        super(name, returnType);
    }

    /**
     * Returns true if abstract modifier bit is set, false if not.
     *
     * @return true if method is abstract
     */
    public boolean isAbstract() {
        return Modifier.isAbstract(modifiers);
    }

    /**
     * Sets the abstract modifier to true or false. Fires "isAbstract" property change event.
     *
     * @param isAbstract true if the method is abstract, false if not
     */
    public void setAbstract(boolean isAbstract) {
        int oldModifiers = modifiers;
        if (isAbstract) {
            addModifier(Modifier.ABSTRACT);
        } else {
            removeModifier(Modifier.ABSTRACT);
        }
        pcs.firePropertyChange("isAbstract", Modifier.isAbstract(oldModifiers), isAbstract());
    }

    /**
     * Returns true if static modifier bit is set, false if not.
     *
     * @return true if method is static
     */
    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    /**
     * Sets the static modifier to true or false. Fires "isStatic" property change event.
     *
     * @param isStatic true if the method is static, false if not
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
     * Returns true if final modifier bit is set, false if not.
     *
     * @return true if method is final
     */
    public boolean isFinal() {
        return Modifier.isFinal(modifiers);
    }

    /**
     * Sets the final modifier to true or false. Fires "isFinal" property change event.
     *
     * @param isFinal true if the method is final, false if not
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
     * Returns true if synchronized modifier bit is set, false if not.
     *
     * @return true if method is synchronized
     */
    public boolean isSynchronized() {
        return Modifier.isSynchronized(modifiers);
    }

    /**
     * Sets the method modifier to true or false. Fires "isSynchronized" property change event.
     *
     * @param isSynchronized true if the method is synchornized, false if not
     */
    public void setSynchronized(boolean isSynchronized) {
        int oldModifiers = modifiers;
        if (isSynchronized) {
            addModifier(Modifier.SYNCHRONIZED);
        } else {
            removeModifier(Modifier.SYNCHRONIZED);
        }
        pcs.firePropertyChange("isSynchronized", Modifier.isSynchronized(oldModifiers), isSynchronized());
    }

    @Override
    public boolean allowedToAddModifier(int modifier) {
        if ((modifier & Modifier.methodModifiers()) == 0) return false;
        switch (modifier) {
            case Modifier.STATIC:
                if (isAbstract()) return false;
                break;
            case Modifier.FINAL:
                if (isAbstract()) return false;
                break;
            case Modifier.ABSTRACT:
                if (isStatic() || isFinal() || isSynchronized()) return false;
                break;
            case Modifier.SYNCHRONIZED:
                if (isAbstract()) return false;
                break;
            default:
                return false;
        }
        return true;
    }

    @Override
    public String getLabelText(boolean isSimpleTypeNames) {
        StringBuilder result = new StringBuilder();
        // removes static because it is rendered as underline
        if (isStatic()) result.append(Modifier.toString(modifiers).replace("static ", "").trim().replace("\\s+", " ")).append(" ");
        if (isSimpleTypeNames) result.append(getSimpleTypeUMLSignature());
        else result.append(getUMLSignature());
        return result.toString();
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
