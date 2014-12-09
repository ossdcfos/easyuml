package org.uml.model.members;

import org.uml.model.Visibility;

/**
 * Enum's elements - Literals (constants).
 *
 * @author Jelena
 * @see EnumComponent
 * @see MemberBase
 * @see Field
 * @see Method
 * @see Constructor
 */
public class Literal extends MemberBase {

    /**
     * Default constructor, only sets the name.
     *
     * @param name of the Literal
     * @see MemberBase
     */
    public Literal(String name) {
        super(name);
        this.type = getDeclaringComponent().getName();
        this.visibility = Visibility.PUBLIC;
    }

    @Override
    public String getSignatureWithoutModifiers() {
        return type+" "+name;
    }

    @Override
    public String toString() {
        return getSignatureWithoutModifiers();
    }

    @Override
    public String deriveNewSignatureWithoutModifiersFromName(String newName) {
        return type+" "+newName;
    }

    @Override
    public String deriveNewSignatureWithoutModifiersFromType(String newType) {
        //TODO Remove this
        return newType+" "+name;
    }
    
}
