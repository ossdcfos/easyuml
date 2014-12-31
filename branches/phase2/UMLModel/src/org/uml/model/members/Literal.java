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
        this.visibility = Visibility.PUBLIC;
    }

    @Override
    public String getSignature() {
        return name;
    }

    @Override
    public String getLabelText() {
        return getSignature();
    }

    @Override
    public String deriveSignatureFromName(String newName) {
        return newName;
    }

    //TODO Remove this maybe, Literal cannot change type
    @Override
    public String deriveSignatureFromType(String newType) {
        return name;
    }

}
