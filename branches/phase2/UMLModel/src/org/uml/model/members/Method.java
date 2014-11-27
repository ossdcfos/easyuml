package org.uml.model.members;

import java.util.HashMap;
import org.uml.model.Visibility;

/**
 *
 * @author Boris
 */
public class Method extends MethodBase {

    public Method(String name){
        super(name);
        arguments = new HashMap<>();
    }
    
    public Method(String name, String returnType, HashMap<String, MethodArgument> arguments) {
        super(name, returnType, arguments);
    }

    /**
     * Creates a string that represents Method's signature.
     * <p>
     * By concatenating strings a text is created, which can be used to
     * represent this Field in the UML diagram's class that holds it e.g.
     * "public static String firstMethod(int theNumber)". THIS IS VERSION FOR
     * INTERFACES
     *
     * @return specially formed Method's String representation
     */
    public String getSignatureForInterfaces() {
        StringBuffer result = new StringBuffer();
        if (visibility != null && !Visibility.PACKAGE.equals(visibility)) {
            result = result.append(getVisibility().toString()).append(" ");
        }
        result.append(getSignatureForLabel());
        result.append(";\n");
        return result.toString();
    }
    
}
