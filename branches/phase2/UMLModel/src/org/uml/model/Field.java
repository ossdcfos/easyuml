package org.uml.model;

import java.lang.reflect.Type;



/**
 * Implementation of Field, one of 4 possible Members.
 * @author Uros
 * @see Member
 * @see Literal
 * @see Method
 * @see Constructor
 */
public class Field extends Member /* treba da nasledjuje member a u membberu da bude visibility*/{
        // sta ako je niz? da li treba koristiti Type?
        private String type;
         //these two should go to Member class, and provide nice API 
        private boolean isStatic;
        private boolean isFinal;
        private boolean isSynchronized;
        
        // provide constructor which takes  declaringClass param
    public Field(String name, String type, Visibility visibility) {
        super(name);
        this.type = type;
        this.visibility = visibility;
    }
    
    public boolean isIsStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public boolean isIsFinal() {
        return isFinal;
    }

    public void setIsFinal(boolean isFinal) {
        this.isFinal = isFinal;
    }

    public boolean isIsSynchronized() {
        return isSynchronized;
    }

    public void setIsSynchronized(boolean isSynchronized) {
        this.isSynchronized = isSynchronized;
    }
     
    public void setModifier(String modifier) {
        if("static".equals(modifier)) {
            isStatic = true;
            return;
        }
        if("final".equals(modifier)) {
            isFinal = true;
            return;
        }
        if("synchronized".equals(modifier)) {
            isSynchronized = true;
        }
        
    }
    
    public void resetModifiers() {
        setIsFinal(false);
        setIsStatic(false);
        setIsSynchronized(false);
    }
    

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getSignatureForLabel() {
        String result = "";
        if(isStatic) {
            result = result.concat("static ");
        }
        if(isFinal) {
            result = result.concat("final ");
        }
        if(isSynchronized) {
            result = result.concat("synchronized ");
        }
        //potrebno je implementirati Type za filed
        //result = result.concat(type + " ");
        result = result.concat(type + " ");
        result = result.concat(getName() + "\n");
        return result;
    }

    public String getSignature() {
        String result = "";
        if(visibility != null && !visibility.equals(Visibility.PACKAGE)) {
            result = result.concat(getVisibility().toString() + " ");
        }
        if(isStatic) {
            result = result.concat("static ");
        }
        if(isFinal) {
            result = result.concat("final ");
        }
        if(isSynchronized) {
            result = result.concat("synchronized ");
        }
        //potrebno je implementirati Type za filed
        //result = result.concat(type + " ");
        result = result.concat(type + " ");
        result = result.concat(getName() + ";\n");
        return result;
    }
      
}
