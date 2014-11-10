package org.uml.model;

import org.uml.model.members.Method;
import java.util.HashMap;

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

    private HashMap<String, Method> methods;

    /**
     * Constructor without parameters. Sets the name to default value.
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
        methods = new HashMap<>();
    }

    /**
     * Returns all methods this interface has as a collection (HashMap)
     *
     * @return HashMap of methods
     */
    public HashMap<String, Method> getMethods() {
        return methods;
    }

    /**
     * Adds a Method instance to InterfaceComponent's members collection.
     *
     * @param method which will be added
     */
    public void addMethod(Method method) {
        method.setDeclaringClass(this);
        methods.put(method.getName(), method);
        addMember(method);
    }

    /**
     * Removes Method from InterfaceComponent's members collection.
     *
     * @param name of the method which will be removed
     */
    public void removeMethod(String name) {
        methods.remove(name);
    }
}
