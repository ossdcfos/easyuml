/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import java.util.HashMap;
import java.util.Map;
import org.uml.model.Constructor;

/**
 *
 * @author Uros
 */
public class ConstructorCodeGenerator implements CodeGenerator{

    
    HashMap<String,Constructor> constructors;

    public ConstructorCodeGenerator(HashMap<String, Constructor> constructors) {
        this.constructors = constructors;
    }
    
    
    @Override
    public String generateCode() {
        String code="";
        for (Map.Entry<String, Constructor> entry : constructors.entrySet()) {
            
            String string = entry.getKey();
            Constructor constructor = entry.getValue();
            code += "public " + constructor.getName() + "("+constructor.getArguments()+")"+" {/n"+"";
            
        }
        
        return code;
        
        
    }
    
    
    
    
}
