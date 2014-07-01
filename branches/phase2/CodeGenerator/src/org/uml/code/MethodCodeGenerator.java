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
 * Method's code generating class.
 *
 * @author Uros
 */
public class MethodCodeGenerator implements CodeGenerator {

    HashMap<String, Method> methods = new HashMap<String, Method>();

    /**
     * Constructor that sets this object's methods HasMap to the given value.
     *
     * @param methods which code needs to be generated
     */
    public MethodCodeGenerator(HashMap<String, Method> methods) {
        this.methods = methods;
    }

    /**
     * Generates code for all methods inside this object's methods HashMap.
     * Utilises Method's getSignature method to properly generate code.
     *
     * @return code of the generated methods.
     * @see Method
     */
    @Override
    public String generateCode() {
        String methodsString = "";
        for (Map.Entry<String, Method> entry : methods.entrySet()) {
            Method method = entry.getValue();
            methodsString += method.getSignature();
        }
        return methodsString;
    }
    //this method must be implemented because of the CodeGenerator interface, although it is never used
    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
    }
    
    /**
     * Generates code used only for appending code to Interface components.
     * 
     * @return specially generated code for interfaces
     */
    String generateCodeMethodsForInterfaces() {
        String methodsString = "";
        for (Map.Entry<String, Method> entry : methods.entrySet()) {
            Method method = entry.getValue();
            methodsString += method.getSignatureForInterfaces();
        }
        return methodsString;
    }
}
