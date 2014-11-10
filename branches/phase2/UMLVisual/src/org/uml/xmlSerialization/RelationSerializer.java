/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import org.uml.model.relations.RelationComponent;

/**
 *
 * @author Stefan
 */
public interface RelationSerializer extends XmlSerializer{
    
    public void addRelationComponent(RelationComponent relation);
}
