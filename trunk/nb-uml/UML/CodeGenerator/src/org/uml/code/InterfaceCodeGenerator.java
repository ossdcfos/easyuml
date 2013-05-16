/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import org.uml.model.ClassComponent;
import org.uml.model.InterfaceComponent;
import org.uml.model.Method;

/**
 *
 * @author zoran
 */
class InterfaceCodeGenerator implements CodeGenerator {
    
    InterfaceComponent interfaceComponent;

    public void setInterfaceComponent(InterfaceComponent interfaceComponent) {
        this.interfaceComponent = interfaceComponent;
    }
    
    

    public InterfaceCodeGenerator() {
    }

    @Override
    public String generateCode() {
        for(Method method : interfaceComponent.getMethods().values()) {
          //methodCodeGenerator.generateCode();  
        }
        return "";
    }
    
}
