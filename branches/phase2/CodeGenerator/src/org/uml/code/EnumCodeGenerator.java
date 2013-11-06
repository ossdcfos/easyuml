/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;

/**
 *
 * @author zoran
 */
class EnumCodeGenerator implements CodeGenerator {

    EnumComponent enumComponent;
    
    public EnumCodeGenerator() {
    }

    @Override
    public String generateCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
       enumComponent = (EnumComponent) component;
    }
    
}
