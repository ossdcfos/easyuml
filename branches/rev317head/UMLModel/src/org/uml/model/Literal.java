/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

/**
 * Implementation of Literal, one of 4 possible Members.
 * @author Jelena
 * @see Member
 * @see Field
 * @see Method
 * @see Constructor
 */
public class Literal extends Member{
    
    /**
     * Default constructor.
     * @param name
     */
    public Literal(String name) {
        super(name);
    }
}
