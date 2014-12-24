package org.uml.code;

import java.util.LinkedHashSet;
import org.uml.model.components.ComponentBase;
import org.uml.model.members.Method;
import org.uml.model.members.MethodBase;

/**
 * Method's code generating class.
 *
 * @author Uros
 */
public class MethodCodeGenerator implements CodeGenerator {

    LinkedHashSet<Method> methods = new LinkedHashSet<>();

    /**
     * Constructor that sets this object's methods HasMap to the given value.
     *
     * @param methods which code needs to be generated
     */
    public MethodCodeGenerator(LinkedHashSet<Method> methods) {
        this.methods = methods;
    }

    /**
     * Generates code for all methods inside this object's methods HashSet.
     * Utilises Method's getSignature method to properly generate code.
     *
     * @return code of the generated methods.
     * @see MethodBase
     */
    @Override
    public String generateCode() {
        StringBuilder methodsString = new StringBuilder();
        for (Method method : methods) {
//            methodsString.append(method.getFullSignature());
        }
        return methodsString.toString();
    }
    //this method must be implemented because of the CodeGenerator interface, although it is never used
    @Override
    public void setComponent(ComponentBase component) {
    }
    
    /**
     * Generates code used only for appending code to Interface components.
     * 
     * @return specially generated code for interfaces
     */
    String generateCodeMethodsForInterfaces() {
        String methodsString = "";
        for (Method method : methods) {
//            methodsString += method.getSignatureForInterfaces();
        }
        return methodsString;
    }
}
