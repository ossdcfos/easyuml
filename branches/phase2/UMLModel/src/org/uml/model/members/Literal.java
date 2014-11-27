package org.uml.model.members;

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
    }
}
