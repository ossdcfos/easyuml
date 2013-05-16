/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import java.util.HashMap;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;
import org.uml.model.Field;
import org.uml.model.InterfaceComponent;



/**
 *
 * @author zoran
 */
public class ClassDiagramCodeGenerator implements CodeGenerator {
    
    private static ClassDiagramCodeGenerator instance;
    ClassDiagram classDiagram;

    HashMap <Class, CodeGenerator> generators;
    
    public ClassDiagramCodeGenerator() {
        this.generators = new HashMap<Class, CodeGenerator>();
        generators.put(ClassComponent.class, new ClassCodeGenerator());
        generators.put(InterfaceComponent.class, new InterfaceCodeGenerator());
        generators.put(EnumComponent.class, new EnumCodeGenerator());    
    }
    
    public static ClassDiagramCodeGenerator getInstance() {
        if (instance == null) {
            instance = new ClassDiagramCodeGenerator();
        }
        
        return instance;                
    }

    public void setClassDiagram(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
    }
    
    
    
    
    @Override
    public String generateCode() {
        StringBuilder sb = new StringBuilder();
        
        for(ClassDiagramComponent comp : classDiagram.getComponents().values() ) {
            CodeGenerator codeGen = generators.get(comp.getClass());
            String code =codeGen.generateCode();
            sb.append(code);
        }
        
        return sb.toString();
    }
    
    
    
}
