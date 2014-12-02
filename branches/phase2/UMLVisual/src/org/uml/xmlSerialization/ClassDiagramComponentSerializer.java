package org.uml.xmlSerialization;

import org.uml.model.components.ComponentBase;

/**
 *
 * @author Stefan
 */
public interface ClassDiagramComponentSerializer extends XmlSerializer{
    
    public void addClassDiagramComponent(ComponentBase component);
}
