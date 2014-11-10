/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import org.uml.model.ComponentBase;
import org.uml.model.InterfaceComponent;

/**
 * Interface's code generating class. Implements all necessary methods that are
 * used in order to generate code from InterfaceComponents.
 *
 * @author zoran
 */
class InterfaceCodeGenerator implements CodeGenerator {

    InterfaceComponent interfaceComponent;

    /**
     * Sets this object's interfaceComponent that is used for code generating.
     *
     * @param interfaceComponent which code needs to be generated
     */
    public void setInterfaceComponent(InterfaceComponent interfaceComponent) {
        this.interfaceComponent = interfaceComponent;
    }

    /**
     * Parameterless constructor that does not instantiate interfaceComponent
     * field.
     */
    public InterfaceCodeGenerator() {
    }

    /**
     * Generates code from interfaceComponent. Along with standard interface
     * definitions and descriptions, methods are also appended.
     *
     * @return interface's code as a String
     * @see InterfaceComponent
     */
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
        String end = "\n }";

        code += header + "\n";
        code += methods + "\n";
        code += end;
        return code;

    }

    /**
     * InterfaceComponent field is casted out of given component and this object's
     * interfaceComponent field is set to the value.
     *
     * @param component used for code generating
     */
    @Override
    public void setComponent(ComponentBase component) {
        interfaceComponent = (InterfaceComponent) component;
    }

}
