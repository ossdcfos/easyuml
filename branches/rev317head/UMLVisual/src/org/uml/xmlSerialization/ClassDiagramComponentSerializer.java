/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import org.uml.model.ClassDiagramComponent;

/**
 *
 * @author Stefan
 */
public interface ClassDiagramComponentSerializer extends XmlSerializer{
    
    public void addClassDiagramComponent(ClassDiagramComponent component);
}
