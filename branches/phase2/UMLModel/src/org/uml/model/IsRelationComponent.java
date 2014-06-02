package org.uml.model;

/**
 * Used to describe Is relation in UML class diagrams. Describes relation used
 * usually when an object <i>extends</i>
 * another one.
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
