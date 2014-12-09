package org.uml.model.components;

import java.util.LinkedHashMap;
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
     */
    public InterfaceComponent() {
        this("UntitledInterface");
    }

    /**
     * One parameter constructor. Sets the name and instantiates methods.
     *
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
        method.setDeclaringComponent(this);
        methods.put(method.toString(), method);
        addComponent(method);
    }

    @Override
    public void removeMemberFromContainer(MemberBase member) {
        if (member instanceof Method) methods.values().remove((Method)member);
        else throw new RuntimeException("Removing unsupported member: "+member.toString());
    }
}
