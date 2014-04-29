/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

/**
 * Implementation of one of four possible RelationComponents.
 * @author "NUGS"
 * @see RelationComponent
 * @see UseRelationComponent
 * @see HasRelationComponent
 * @see IsRelationComponent
 */
public class ImplementsRelationComponent extends RelationComponent{

     /**
     * Returns name of relation
     * @return "Is"
     */
    @Override
    public String toString() {
        return "Implements";
    }
    
}
