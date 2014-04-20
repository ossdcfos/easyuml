/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import org.dom4j.Element;
import org.uml.model.Method;
import org.uml.model.MethodArgument;

/**
 *
 * @author Stefan
 */
public class MethodSerializer implements XmlSerializer{

    private HashMap<String, Method> methods;

    public MethodSerializer(HashMap<String, Method> methods) {
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
            if (method.getVisibility()!= null) methodNode.addAttribute("visibility", method.getVisibility().toString());
            int[] modifiers = method.getModifiers();
            if (modifiers != null) {
                for (int i = 0; i < modifiers.length; i++) {
                    if (modifiers[i] > 0 ) {
                        String modType = null;
                        switch(modifiers[i]) {
                            case Modifier.STATIC : 
                                modType = "isStatic";
                                break;
                            case Modifier.FINAL :
                                modType = "isFinal";
                                break;
                            case Modifier.ABSTRACT :
                                modType = "isAbstract";
                                break;
                            case Modifier.SYNCHRONIZED :
                                modType = "isSynchronized";
                                break;
                        }
                        methodNode.addAttribute(modType, "true");
                    }
                }
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
