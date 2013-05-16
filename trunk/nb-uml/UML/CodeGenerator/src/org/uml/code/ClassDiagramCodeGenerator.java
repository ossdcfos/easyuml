/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.Field;



/**
 *
 * @author zoran
 */
public class ClassDiagramCodeGenerator {
    
    private static ClassDiagramCodeGenerator instance;

    public ClassDiagramCodeGenerator() {
    
    
    }
    
    public static ClassDiagramCodeGenerator getInstance() {
        if (instance == null) {
            instance = new ClassDiagramCodeGenerator();
        }
        
        return instance;                
    }
    
    
    public String generateCode(ClassDiagram classDiagram) {
        StringBuilder sb = new StringBuilder();
        
        for(ClassDiagramComponent comp : classDiagram.getComponents().values() ) {
        //    String code = generateCode(comp);
            sb.append(sb);
        }
        
        return sb.toString();
    }
    
    private String generateCode(ClassComponent classComp) {
        // get class template from somewhere ...
        for(Field field : classComp.getFields().values()) {
            
        }
        
        return "";
    }
    
    
}
