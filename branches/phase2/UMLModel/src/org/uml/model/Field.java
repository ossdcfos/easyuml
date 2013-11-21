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
        private Type type;
        // these two should go to Member class, and provide nice API 
        private Visibility visibility;
        private boolean isStatic;
        private boolean isFinal;
        private boolean isAbstract;
        private boolean isSynchronised;
        
        // provide constructor which takes  declaringClass param
    public Field(String name, Type type, Visibility visibility) {
        super(name);
        this.type = type;
        this.visibility = visibility;
    }



    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
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

    public boolean isIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public boolean isIsSynchronised() {
        return isSynchronised;
    }

    public void setIsSynchronised(boolean isSynchronised) {
        this.isSynchronised = isSynchronised;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }
      
}
