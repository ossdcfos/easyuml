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
        String code="";
        String abstractModifierStr="";
        if(classComponent.isIsAbstract()) {
        abstractModifierStr="abstract";
        }
        String header = "public " + abstractModifierStr + " class " + classComponent.getName() + " { ";
        ConstructorCodeGenerator ccg = new ConstructorCodeGenerator(classComponent.getConstructors());
        String constructors = ccg.generateCode();
        FieldCodeGenerator fcg = new FieldCodeGenerator(classComponent.getFields());
        String fields = fcg.generateCode();
        MethodCodeGenerator mcg = new MethodCodeGenerator(classComponent.getMethods());
        String methods = mcg.generateCode();
        String end="\n }";
        //classComponent.get
        
        code+=header+"\n";
        code+=constructors+"\n";
        code+=fields+"\n";
        code+=methods+"\n";
        code+=end;
        return code;
         
    } 

    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
        classComponent=(ClassComponent)component;
    }
    
}
