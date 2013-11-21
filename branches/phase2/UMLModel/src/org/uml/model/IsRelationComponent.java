package org.uml.model;

/**
 * Implementation of one of four possible RelationComponents.
 * @author "NUGS"
 * @see RelationComponent
 * @see UseRelationComponent
 * @see HasRelationComponent
 * @see ImplementsRelationComponent
 */
public class IsRelationComponent extends RelationComponent{

     /**
     * Returns name of relation
     * @return "Is"
     */
    @Override
    public String toString() {
        return "Is";
    }
    
}
