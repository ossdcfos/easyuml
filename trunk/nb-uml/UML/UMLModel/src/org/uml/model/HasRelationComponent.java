package org.uml.model;

/**
 *
 * @author "NUGS"
 */
public class HasRelationComponent extends RelationComponent{

    CardinalityEnum cardinalitySource;
    CardinalityEnum cardinalityTarget;
    
    
    @Override
    public String toString() {
        return "Has";
    }
    
}
