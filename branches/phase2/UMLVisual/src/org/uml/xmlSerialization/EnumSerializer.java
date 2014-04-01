/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import org.dom4j.Element;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;

/**
 *
 * @author Stefan
 */
public class EnumSerializer implements ClassDiagramComponentSerializer{

    private EnumComponent enumComponent;
    @Override
    public void addClassDiagramComponent(ClassDiagramComponent component) {
        try{
            enumComponent = (EnumComponent) component;
        }catch (ClassCastException e) {
            System.out.println("You have tried to cast invalid type to ClassComponent!");
            e.printStackTrace();
        }
    }

    @Override
    public void serialize(Element node) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
