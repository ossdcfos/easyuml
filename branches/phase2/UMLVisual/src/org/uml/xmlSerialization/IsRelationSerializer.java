/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import org.dom4j.Element;
import org.uml.model.IsRelationComponent;
import org.uml.model.RelationComponent;

/**
 *
 * @author Stefan
 */
public class IsRelationSerializer implements RelationSerializer{
 
    private IsRelationComponent isRelationComponent;
    
    @Override
    public void addRelationComponent(RelationComponent relation) {
        try{
            isRelationComponent = (IsRelationComponent) relation;
        }catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to IsRelationComponent!");
            e.printStackTrace();
        }
    }
    
    @Override
    public void serialize(Element node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
