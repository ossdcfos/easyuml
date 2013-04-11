/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

/**
 *
 * @author "NUGS"
 */
public class UseRelationComponent extends RelationComponent{

    CardinalityEnum cardinalitySource;
    CardinalityEnum cardinalityTarget;
    
    @Override
    public String toString() {
        return "Use";
    }
    
}
