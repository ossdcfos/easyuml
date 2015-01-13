package org.uml.model.members;

import java.lang.reflect.Modifier;

/**
 *
 * @author Boris
 */
public class Method extends MethodBase {

    public Method(String name, String returnType) {
        super(name, returnType);
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(modifiers);
    }

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
        if (isSynchronized) {
            addModifier(Modifier.SYNCHRONIZED);
        } else {
            removeModifier(Modifier.SYNCHRONIZED);
        }
        pcs.firePropertyChange("isSynchronized", Modifier.isSynchronized(oldModifiers), isSynchronized());
    }
    
    @Override
    public boolean allowedToAddModifier(int modifier) {
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
        }
        return true;
    }

//    /**
//     * Creates a string that represents Method's signature.
//     * <p>
//     * By concatenating strings a text is created, which can be used to
//     * represent this Field in the UML diagram's class that holds it e.g.
//     * "public static String firstMethod(int theNumber)". THIS IS VERSION FOR
//     * INTERFACES
//     *
//     * @return specially formed Method's String representation
//     */
//    public String getSignatureForInterfaces() {
//        StringBuffer result = new StringBuffer();
//        if (visibility != null && !Visibility.PACKAGE.equals(visibility)) {
//            result = result.append(getVisibility().toString()).append(" ");
//        }
//        result.append(getSignatureForLabel());
//        result.append(";\n");
//        return result.toString();
//    }
}
