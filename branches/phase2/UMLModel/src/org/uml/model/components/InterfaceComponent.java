package org.uml.model.components;

import java.util.LinkedHashSet;
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

    private LinkedHashSet<Method> methods;
    private boolean isStatic;
    
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
        methods = new LinkedHashSet<>();
    }

    /**
     * Returns all methods this interface has as a collection (HashMap)
     *
     * @return HashMap of methods
     */
    public LinkedHashSet<Method> getMethods() {
        return methods;
    }

    /**
     * Adds a Method instance to InterfaceComponent's members collection.
     *
     * @param method which will be added
     */
    public void addMethod(Method method) {
        method.setDeclaringComponent(this);
        methods.add(method);
        addComponent(method);
    }

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        boolean oldValue = this.isStatic;
        this.isStatic = isStatic;
        pcs.firePropertyChange("isStatic", oldValue, this.isStatic);
    }

    @Override
    public void removeMemberFromContainer(MemberBase member) {
        if (member instanceof Method) methods.remove((Method)member);
        else throw new RuntimeException("Removing unsupported member: "+member.toString());
    }
    
}
