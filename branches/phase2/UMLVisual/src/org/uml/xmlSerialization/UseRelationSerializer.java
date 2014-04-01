/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import org.dom4j.Element;
import org.uml.model.RelationComponent;
import org.uml.model.UseRelationComponent;

/**
 *
 * @author Stefan
 */
public class UseRelationSerializer implements RelationSerializer{

    private UseRelationComponent useRelationComponent;
    
    @Override
    public void addRelationComponent(RelationComponent relation) {
        try{
            useRelationComponent = (UseRelationComponent) relation;
        }catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to UseRelationComponent!");
            e.printStackTrace();
        }
    }

    @Override
    public void serialize(Element node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
