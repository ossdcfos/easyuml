package org.uml.model;

/**
 * Implementation of Literal, one of four possible Members. It is used to
 * represent enum elements (constants).
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
