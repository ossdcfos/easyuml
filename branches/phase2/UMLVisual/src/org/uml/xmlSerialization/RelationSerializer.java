package org.uml.xmlSerialization;

import org.uml.model.relations.RelationBase;

/**
 *
 * @author Stefan
 */
public interface RelationSerializer extends XmlSerializer{
    
    public void addRelationComponent(RelationBase relation);
}
