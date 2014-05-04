package org.uml.model;

/**
 * Implementation of one of four possible RelationComponents. Describes
 * Implements relation between UML objects (used usually when an object
 * <i>implements</i> another objects behavior; fields, methods, etc.).
 *
 * @author "NUGS"
 * @see RelationComponent
 * @see HasRelationComponent
 * @see IsRelationComponent
 * @see UseRelationComponent
 */
public class ImplementsRelationComponent extends RelationComponent {

    /**
     * Returns the name of relation.
     *
     * @return "Implements"
     */
    @Override
    public String toString() {
        return "Implements";
    }
}
