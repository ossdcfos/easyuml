/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.xmlSerialization;

import java.util.HashMap;
import org.dom4j.Element;
import org.uml.model.members.Constructor;
import org.uml.model.members.MethodArgument;

/**
 *
 * @author Stefan
 */
public class ConstructorSerializer implements XmlSerializer{

    private HashMap<String, Constructor> constructors;

    public ConstructorSerializer(HashMap<String, Constructor> constructors) {
        this.constructors = constructors;
    }
    /**
     * Converts constructors objects to nodes of an XML tree.
     * Each object from constructors is translated to a sub node of the node input parameter.
     * Each attribute of every constructor object from constructors is translated to an attribute of the corresponding XML node.
     * @param node represents empty XML node in which to put nodes representing constructors.
     */
    @Override
    public void serialize(Element node) {
        for (Constructor constructor : constructors.values()) {
            Element constructorNode = node.addElement("Constructor");
            if (constructor.getName() != null) constructorNode.addAttribute("name", constructor.getName());
            if (constructor.getVisibility()!= null) constructorNode.addAttribute("visibility", constructor.getVisibility().toString());
            if (constructor.getReturnType() != null) constructorNode.addAttribute("returnType", constructor.getReturnType());
            if (constructor.getArguments() != null) {
                for (MethodArgument argument : constructor.getArguments().values()) {
                    Element argumentNode = constructorNode.addElement("argument")
                            .addAttribute("type", argument.getType())
                            .addAttribute("name", argument.getName());
                }
            }
        }
    }
    
}
