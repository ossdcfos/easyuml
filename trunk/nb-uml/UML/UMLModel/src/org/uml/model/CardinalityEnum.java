/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.model;

/**
 *
 * @author zoran
 */
public enum CardinalityEnum {
    One2One {
        @Override
        public String toString(){
            return "1..1";
        }
    }, Zero2One{
        @Override
        public String toString(){
            return "0..1";
        }
    }, One2Many{
        @Override
        public String toString(){
            return "1..*";
        }
    }, Zero2Many{
        @Override
        public String toString(){
            return "0..*";
        }
    };
}
