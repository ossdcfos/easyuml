package org.uml.model.components;

import org.uml.model.members.MethodBase;
import java.util.LinkedHashMap;
import org.uml.model.ClassDiagram;
import org.uml.model.members.MemberBase;
import org.uml.model.members.Method;

/**
 * Interfaces from UML Class diagrams. Used to represent Interface classes in
 * UML diagrams.
 *
 * @author zoran
 * @see ComponentBase
 * @see ClassComponent
 * @see EnumComponent
 * @see PackageComponent
 */
public class InterfaceComponent extends ComponentBase {

    private LinkedHashMap<String, Method> methods;

    /**
     * Default constructor only specifying parent diagram. Sets name to default value.
     * @param parentDiagram
     */
    public InterfaceComponent() {
        this("UntitledInterface");
    }

    /**
     * One parameter constructor. Sets the name and instantiates methods.
     *
     * @param parentDiagram
     * @param name of InterfaceComponent
     * @see ComponentBase
     */
    public InterfaceComponent(String name) {
        super(name);
        methods = new LinkedHashMap<>();
    }

    /**
     * Returns all methods this interface has as a collection (HashMap)
     *
     * @return HashMap of methods
     */
    public LinkedHashMap<String, Method> getMethods() {
        return methods;
    }

    /**
     * Adds a Method instance to InterfaceComponent's members collection.
     *
     * @param method which will be added
     */
    public void addMethod(Method method) {
        method.setDeclaringClass(this);
        methods.put(method.toString(), method);
        addMember(method);
    }

    @Override
    public void removeMemberFromContainer(MemberBase member) {
        if (member instanceof MethodBase) methods.values().remove((MethodBase)member);
        else throw new RuntimeException("Removing unsupported member: "+member.toString());
    }
}
