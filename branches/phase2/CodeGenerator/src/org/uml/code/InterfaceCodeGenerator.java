/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;
import org.uml.model.ClassDiagramComponent;
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
//        for(Method method : interfaceComponent.getMethods().values()) {
//          //methodCodeGenerator.generateCode();  
//        }
//        return "";
        String code = "";
        String header = "public " + "interface " + interfaceComponent.getName() + " { ";
        MethodCodeGenerator mcg = new MethodCodeGenerator(interfaceComponent.getMethods());
//        String methods = mcg.generateCode();
        String methods = mcg.generateCodeMethodsForInterfaces();
        String end="\n }";
        
        code+=header+"\n";
        code+=methods+"\n";
        code+=end;
        return code;
        
    }

    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
        interfaceComponent=(InterfaceComponent) component;
    }
    
}
