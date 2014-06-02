package org.uml.model;

/**
 * Representation of enum's elements - Literals (constants). Is one of
 * four possible Members
 *
 * @author Jelena
 * @see EnumComponent
 * @see Member
 * @see Field
 * @see Method
 * @see Constructor
 */
public class Literal extends Member {

    /**
     * Default constructor, only sets the name.
     *
     * @param name of the Literal
     * @see Member
     */
    public Literal(String name) {
        super(name);
    }
}
