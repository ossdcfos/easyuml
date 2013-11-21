/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

/**
 * Contains all four possible types of RelationComponents.
 * @author zoran
 * @see RelationComponent
 * @see IsRelationComponent
 * @see HasRelationComponent
 * @see UseRelationComponent
 * @see ImplementsRelationComponent
 */

public enum CardinalityEnum {
    /**
     * "1..1" relation
     */
    One2One {
        @Override
        public String toString(){
            return "1..1";
        }
    }, 
    
    /**
     * "0..1" relation
     */
    Zero2One{
        @Override
        public String toString(){
            return "0..1";
        }
    }, 
    /**
     * "1..*" relation
     */
    One2Many{
        @Override
        public String toString(){
            return "1..*";
        }
    }, 
    /**
     * "0..*" relation
     */
    Zero2Many{
        @Override
        public String toString(){
            return "0..*";
        }
    };
}
