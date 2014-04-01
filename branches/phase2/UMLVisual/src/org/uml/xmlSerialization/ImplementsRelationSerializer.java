/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import org.dom4j.Element;
import org.uml.model.ImplementsRelationComponent;
import org.uml.model.RelationComponent;

/**
 *
 * @author Stefan
 */
public class ImplementsRelationSerializer implements RelationSerializer{

    private ImplementsRelationComponent implementsRelationComponent;
    
    @Override
    public void addRelationComponent(RelationComponent relation) {
        try{
            implementsRelationComponent = (ImplementsRelationComponent) relation;
        }catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to ImplementsRelationComponent!");
            e.printStackTrace();
        }
    }

    @Override
    public void serialize(Element node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
