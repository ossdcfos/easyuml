package org.uml.model;

/**
 * Implementation of one of four possible RelationComponents. Describes Is
 * relation between UML objects (used usually when an object <i>extends</i> another
 * one).
 *
 * @author "NUGS"
 * @see RelationComponent
 * @see HasRelationComponent
 * @see UseRelationComponent
 * @see ImplementsRelationComponent
 */
public class IsRelationComponent extends RelationComponent {

    /**
     * Returns the name of relation.
     *
     * @return "Is"
     */
    @Override
    public String toString() {
        return "Is";
    }
}
