/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;

/**
 *
 * @author zoran
 */
public class ClassCodeGenerator implements CodeGenerator {

    ClassComponent classComponent;
    
    public ClassCodeGenerator( ) {
    }

    public void setClassComponent(ClassComponent classComponent) {
        this.classComponent = classComponent;
    }
   
    @Override
    public String generateCode() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
