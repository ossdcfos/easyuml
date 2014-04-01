/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import org.dom4j.Element;

/**
 *
 * @author Stefan
 */
public interface XmlSerializer {
    
    public void serialize(Element node);
}
