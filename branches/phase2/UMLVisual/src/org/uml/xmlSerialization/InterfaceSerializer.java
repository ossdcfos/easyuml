/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import org.dom4j.Element;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.InterfaceComponent;

/**
 *
 * @author Stefan
 */
public class InterfaceSerializer implements ClassDiagramComponentSerializer{

    private InterfaceComponent interfaceComponent;
    
    @Override
    public void addClassDiagramComponent(ClassDiagramComponent component) {
        try{
            interfaceComponent = (InterfaceComponent) component;
        }catch(ClassCastException e) {
            System.out.println("You have tried to cast invalid type to InterfaceComponent!");
            e.printStackTrace();
        }
    }

    @Override
    public void serialize(Element node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
