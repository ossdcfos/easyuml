package org.uml.model;

import org.uml.model.members.Method;
import java.util.HashMap;

/**
 * Interfaces from UML Class diagrams. Used to represent Interface
 * classes in UML diagrams.
 *
 * @author zoran
 * @see ClassDiagramComponent
 * @see ClassComponent
 * @see EnumComponent
 * @see PackageComponent
 */
public class InterfaceComponent extends ClassDiagramComponent {

    private HashMap<String, Method> methods;

    /**
     * Constructor without parameters. Sets the name to default value and
     * instantiates the methods collection.
     */
    public InterfaceComponent() {
        this.setName("UntitledInterface");
        methods = new HashMap<>();
    }

    /**
     * One parameter constructor. Only sets the name.
     *
     * @param name of InterfaceComponent
     * @see ClassDiagramComponent
     */
    public InterfaceComponent(String name) {
        super(name);
    }
    /**
     * Returns all methods this interface has as a collection (HashMap)
     * @return HashMap of methods
     */
    public HashMap<String, Method> getMethods() {
        return methods;
    }
    /**
     * Returns this interfaces' specific method with the name provided as input
     * @param name of the method wanted
     * @return Method object
     * @see Method
     */
    public Method getMethod(String name) {
        return methods.get(name);
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
