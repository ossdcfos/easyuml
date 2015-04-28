package org.uml.xmlSerialization.components;

import org.uml.model.components.ComponentBase;
import org.uml.xmlSerialization.XmlSerializer;

/**
 *
 * @author Stefan
 */
public interface ComponentSerializer extends XmlSerializer {

    public void setClassDiagramComponent(ComponentBase component);
}
