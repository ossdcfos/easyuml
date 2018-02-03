package org.uml.model.members;

import java.lang.reflect.Modifier;
import java.util.LinkedHashSet;
import org.uml.model.GetterSetterGeneration;
import org.uml.model.Visibility;
import org.uml.model.components.ClassComponent;

/**
 * Represents a field (fields inside a class).
 *
 * @author Uros
 * @see Constructor
 * @see Method
 * @see Literal
 */
public class Field extends MemberBase {

    /**
     * Getters automatic generation setting
     */
    private GetterSetterGeneration      getterGeneration;
    /**
     * Setters automatic generation setting
     */
    private GetterSetterGeneration      setterGeneration;
        
    /**
     * Constructor that sets the name, type and visibility of the field.
     *
     * @param name of the field
     * @param type of the field
     * @param visibility of the field
     */
    public Field(String name, String type, Visibility visibility) {
        super(name);
        this.type = type;
        this.visibility = visibility;
        this.getterGeneration = GetterSetterGeneration.AUTO;
        this.setterGeneration = GetterSetterGeneration.AUTO;
    }

    /**
     * Returns true if static modifier bit is set, false if not.
     *
     * @return true if field is static
     */
    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    /**
     * Sets the static modifier to true or false. Fires "isStatic" property change event.
     *
     * @param isStatic true if the field is static, false if not
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
     * @return true if field is final
     */
    public boolean isFinal() {
        return Modifier.isFinal(modifiers);
    }

    /**
     * Sets the final modifier to true or false. Fires "isFinal" property change event.
     *
     * @param isFinal true if the field is final, false if not
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
     * Returns true if transient modifier bit is set, false if not.
     *
     * @return true if field is transient
     */
    public boolean isTransient() {
        return Modifier.isTransient(modifiers);
    }

    /**
     * Sets the transient modifier to true or false. Fires "isTransient" property change event.
     *
     * @param isTransient true if the field is transient, false if not
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
     * Returns true if volatile modifier bit is set, false if not.
     *
     * @return true if field is volatile
     */
    public boolean isVolatile() {
        return Modifier.isVolatile(modifiers);
    }

    /**
     * Sets the volatile modifier to true or false. Fires "isVolatile" property change event.
     *
     * @param isVolatile true if the field is volatile, false if not
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

    /**
     * Returns the unique signature of this field.
     * Consists of type and name.
     *
     * @return unique signature of this member
     */
    @Override
    public String getSignature() {
        StringBuilder result = new StringBuilder();
        result = result.append(type).append(" ");
        result = result.append(getName());
        return result.toString();
    }

    @Override
    protected String getSimpleTypeSignature() {
        StringBuilder result = new StringBuilder();
        String simpleType = getSimpleType(type);
        result = result.append(simpleType).append(" ");
        result = result.append(getName());
        return result.toString();
    }
    
    @Override
    public String getUMLSignature() {
        StringBuilder result = new StringBuilder();
        result = result.append(getName()).append(": ");
        result = result.append(type);
        return result.toString();
    }

    @Override
    protected String getSimpleTypeUMLSignature() {
        StringBuilder result = new StringBuilder();
        result = result.append(getName()).append(": ");
        result = result.append(getSimpleType(type));
        return result.toString();
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
    public String deriveSignatureFromName(String newName) {
        StringBuilder result = new StringBuilder();
        result = result.append(type).append(" ");
        result = result.append(newName);
        return result.toString();
    }

    @Override
    public String deriveSignatureFromType(String newType) {
        StringBuilder result = new StringBuilder();
        result = result.append(newType).append(" ");
        result = result.append(getName());
        return result.toString();
    }

    @Override
    public boolean allowedToAddModifier(int modifier) {
        if ((modifier & Modifier.fieldModifiers()) == 0) return false;
        else return true;
    }
    
    public GetterSetterGeneration getGetterGeneration() {
        return getterGeneration;
    }

    public void setGetterGeneration(GetterSetterGeneration generateGetters) {
        this.getterGeneration = generateGetters;
    }

    public GetterSetterGeneration getSetterGeneration() {
        return setterGeneration;
    }

    public void setSetterGeneration(GetterSetterGeneration generateSetters) {
        this.setterGeneration = generateSetters;
    }
    
    /**
     * Returns true if this field need a getter to be auto generated.
     * 
     * This depends on this field setting; if auto it will depends on
     * its class; if auto it will depends on diagram setting
     * 
     * @return 
     */
    public boolean getterGenerationRequested() {
        GetterSetterGeneration setting = getterGeneration;
        if (setting == GetterSetterGeneration.AUTO) {
            setting = ((ClassComponent)getDeclaringComponent()).getInheritedGetterGeneration();
        }
        if (setting == GetterSetterGeneration.DISABLED) {
            return false;
        }
        if (isStatic()) {
            return false;
        }
        if (setting == GetterSetterGeneration.NOTPUBLIC) {
            return visibility != Visibility.PUBLIC;
        }
        if (setting == GetterSetterGeneration.PRIVATE) {
            return visibility == Visibility.PRIVATE;
        }
        if (setting == GetterSetterGeneration.PROTECTED) {
            return visibility == Visibility.PROTECTED;
        }
        return false;
    }

    public Method createGetter() {
        String methodName = "get"+name.substring(0,1).toUpperCase()+name.substring(1);
        return new Method(methodName,type);
    }
    
    public Method createSetter() {
        String methodName = "set"+name.substring(0,1).toUpperCase()+name.substring(1);
        Method method = new Method(methodName,"void");
        LinkedHashSet<MethodArgument> args = new LinkedHashSet();
        args.add(new MethodArgument(type,name));
        method.setArguments(args);
        return method;
    }
        
}
