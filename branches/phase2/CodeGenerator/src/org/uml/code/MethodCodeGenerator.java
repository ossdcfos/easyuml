/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import java.util.HashMap;
import java.util.Map;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.Method;
import org.uml.model.MethodArgument;
/**
 *
 * @author Uros
 */
public class MethodCodeGenerator implements CodeGenerator{

    HashMap<String,Method> methods = new HashMap<String,Method>();
    public MethodCodeGenerator(HashMap<String,Method> methods) {
    
        this.methods=methods;
    }
    
    
    
    @Override
    public String generateCode() {
        String methodsString="";
        for (Map.Entry<String, Method> entry : methods.entrySet()) {
            Method method = entry.getValue(); 
            String arguments = "";
            if(method.getArguments()!=null){
                for (Map.Entry<String, MethodArgument> entry1 : method.getArguments().entrySet()) {
                    MethodArgument argument = entry1.getValue();
                    arguments+= argument.getType() + " " + argument.getName()+ ", ";
                }
            }
            if(!arguments.equals("")){
                arguments = arguments.substring(0,arguments.length()-2);
            }
                methodsString+= method.getVisibility() + "" + method.getModifiers() + "" + method.getReturnType() + " " + method.getName()+ "(" + arguments + ") {} \n";
            
        }
        return methodsString;
    }

    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
       
    }
    
}
