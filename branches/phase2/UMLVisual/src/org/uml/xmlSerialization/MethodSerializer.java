package org.uml.xmlSerialization;

import java.lang.reflect.Modifier;
import java.util.LinkedHashMap;
import org.dom4j.Element;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;

/**
 *
 * @author Stefan
 */
public class MethodSerializer implements XmlSerializer{

    private LinkedHashMap<String, Method> methods;

    public MethodSerializer(LinkedHashMap<String, Method> methods) {
        this.methods = methods;
    }
    
    /**
     * Converts methods objects to nodes of an XML tree.
     * Each object from methods is translated to a sub node of the node input parameter.
     * Each attribute of every method object from methods is translated to an attribute of the corresponding XML node.
     * @param node represents empty XML node into which to put nodes representing constructors.
     */
    @Override
    public void serialize(Element node) {
        for (Method method : methods.values()) {
            Element methodNode = node.addElement("Method");
            if (method.getName() != null) methodNode.addAttribute("name", method.getName());
            if (method.getVisibility()!= null) methodNode.addAttribute("visibility", method.getVisibility().name().toLowerCase());
            int modifiers = method.getModifiers();
            if((modifiers | Modifier.ABSTRACT) != 0){
                methodNode.addAttribute("isAbstract", "true");
            }
            if((modifiers | Modifier.STATIC) != 0){
                methodNode.addAttribute("isStatic", "true");
            }
            if((modifiers | Modifier.FINAL) != 0){
                methodNode.addAttribute("isFinal", "true");
            }
            if((modifiers | Modifier.SYNCHRONIZED) != 0){
                methodNode.addAttribute("isSynchronized", "true");
            }
            if (method.getReturnType() != null) methodNode.addAttribute("returnType", method.getReturnType());
            if (method.getArguments() != null) {
                for (MethodArgument argument : method.getArguments().values()) {
                    Element argumentNode = methodNode.addElement("Argument")
                            .addAttribute("type", argument.getType())
                            .addAttribute("name", argument.getName());
                }
            }
        }
    }
    
}
