package org.uml.model;

/**
 * Implementation of the members which can be fields, methods, constructors and literals.
 * This should also be a member as in reflection api.
 * put all this stuff in subclasses
 * @author zoran
 * @see Field
 * @see Method
 * @see Constructor
 * @see Literal
 */
public class Member { 
    private String name;
    private int modifier;
    private String modifiers; //modifiers are implemented as Strings, it is possible to later be changed to enum
    private ClassDiagramComponent declaringClass;

    /**
     * Default constructor.
     * @param name of member
     */
    public Member(String name) {
        this.name = name;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }
    
    public ClassDiagramComponent getDeclaringClass() {
        return declaringClass;
    }

    public void setDeclaringClass(ClassDiagramComponent declaringClass) {
        this.declaringClass = declaringClass;
    }
    
    public String getModifiers() {
        return modifiers;
    }

    public void setModifiers(String modifiers) {
        this.modifiers = modifiers;
    }
    
    
    
    
}
