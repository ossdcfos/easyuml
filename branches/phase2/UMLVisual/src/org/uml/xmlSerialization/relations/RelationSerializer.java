package org.uml.xmlSerialization.relations;

import org.uml.model.relations.RelationBase;
import org.uml.xmlSerialization.XmlSerializer;

/**
 *
 * @author Stefan
 */
public interface RelationSerializer extends XmlSerializer{
    
    public void setClassDiagramRelation(RelationBase relation);
}
