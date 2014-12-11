package org.uml.xmlSerialization;

import java.util.LinkedHashSet;
import org.dom4j.Element;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;

/**
 *
 * @author Stefan
 */
public class MethodSerializer implements XmlSerializer {

    private LinkedHashSet<Method> methods;

    public MethodSerializer(LinkedHashSet<Method> methods) {
        this.methods = methods;
    }

    /**
     * Converts methods objects to nodes of an XML tree. Each object from
     * methods is translated to a sub node of the node input parameter. Each
     * attribute of every method object from methods is translated to an
     * attribute of the corresponding XML node.
     *
     * @param node represents empty XML node into which to put nodes
     * representing constructors.
     */
    @Override
    public void serialize(Element node) {
        for (Method method : methods) {
            Element methodNode = node.addElement("Method");
            if (method.getName() != null) methodNode.addAttribute("name", method.getName());
            if (method.getVisibility() != null) methodNode.addAttribute("visibility", method.getVisibility().name().toLowerCase());
            if (method.getType() != null) methodNode.addAttribute("returnType", method.getType());
            if (method.isStatic()) methodNode.addAttribute("isStatic", Boolean.toString(method.isStatic()));
            if (method.isFinal()) methodNode.addAttribute("isFinal", Boolean.toString(method.isFinal()));
            if (method.isAbstract()) methodNode.addAttribute("isAbstract", Boolean.toString(method.isAbstract()));
            if (method.isSynchronized()) methodNode.addAttribute("isSynchronized", Boolean.toString(method.isSynchronized()));
            
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
