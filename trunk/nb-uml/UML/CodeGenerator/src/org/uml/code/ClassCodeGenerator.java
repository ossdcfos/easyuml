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
        String isAbstract="";
        if(classComponent.isIsAbstract()) {
        isAbstract="abstract";
        }
        String header = "public " + isAbstract + " class " + classComponent.getName() + " { ";
        ConstructorCodeGenerator ccg = new ConstructorCodeGenerator(classComponent.getConstructors());
        String constructors = ccg.generateCode();
        FieldCodeGenerator fcg = new FieldCodeGenerator(classComponent.getFields());
        String fields = fcg.generateCode();
        MethodCodeGenerator mcg = new MethodCodeGenerator(classComponent.getMethods());
        String methods = mcg.generateCode();
        String end="\n }";
        //classComponent.get
        return code;
         
    } 
    
}
