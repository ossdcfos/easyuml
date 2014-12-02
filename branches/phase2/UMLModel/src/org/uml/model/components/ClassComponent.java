package org.uml.model.components;

import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.uml.model.members.MethodBase;
import org.uml.model.members.Field;
import org.uml.model.members.Constructor;
import java.util.LinkedHashMap;
import org.uml.model.members.MemberBase;
import org.uml.model.members.Method;

/**
 * A class component in UML Class diagram. 
  *
 * @author Uros
 * @see ComponentBase
 * @see EnumComponent
 * @see InterfaceComponent
 * @see PackageComponent
 */
public class ClassComponent extends ComponentBase {

    @XStreamAsAttribute
    private LinkedHashMap<String, Field> fields;
    private LinkedHashMap<String, Constructor> constructors;
    private LinkedHashMap<String, Method> methods;
    private boolean isAbstract;

    /**
     * Default constructor only specifying parent diagram. Sets name to default value.
     * 
     * @param parentDiagram
     */
    public ClassComponent() {
        this("UntitledClass");
    }

    /**
     * Constructor with parameters which calls its super constructor.
     * Instantiates fields, methods and constructors collections, as well as
     * parentPackage field; Sets isAbstract to false.
     * 
     * <p> Only sets the name of Class. </p>
     *
     * @param parentDiagram
     * @param name to be set
     */
    public ClassComponent(String name) {
        super(name);
        //setParentPackage(null);
        fields = new LinkedHashMap<>();
        constructors = new LinkedHashMap<>();
        methods = new LinkedHashMap<>();
        isAbstract = false;
    }

    /**
     * Returns the collection of fields that this class contains
     *
     * @return HashMap of fields contained
     */
    public LinkedHashMap<String, Field> getFields() {
        return fields;
    }

    /**
     * Returns the collection of methods that this class contains
     *
     * @return HashMap of methods contained
     */
    public LinkedHashMap<String, Method> getMethods() {
        return methods;
    }

    /**
     * Returns the collection of constructors for this class
     *
     * @return HashMap of this clas's constructors
     */
    public LinkedHashMap<String, Constructor> getConstructors() {
        return constructors;
    }
    
    /**
     * Adds the field to ClassComponent's collection of fields.
     *
     * @param field that will be added to collection.
     */
    public void addField(Field field) {
        addMember(field);
        field.setDeclaringClass(this);
        fields.put(field.toString(), field);
    }

    /**
     * Adds the method to ClassComponent's collection of methods.
     *
     * @param method that will be added to collection.
     */
    public void addMethod(Method method) {
        addMember(method);
        method.setDeclaringClass(this);
        methods.put(method.toString(), method);
    }

    /**
     * Adds the Constructor to ClassComponent's collection of constructors.
     *
     * @param constructor which will be added to collection.
     */
    public void addConstructor(Constructor constructor) {
        addMember(constructor);
        constructor.setDeclaringClass(this);
        constructors.put(constructor.toString(), constructor);
    }

    /**
     * Returns true if this class is set to be abstract, false if not
     *
     * @return true if class is abstract
     */
    public boolean isAbstract() {
        return isAbstract;
    }

    /**
     * Sets abstract modifier to true or false
     *
     * @param isAbstract - true if the class is abstract, if not - false
     */
    public void setAbstract(boolean isAbstract) {
        boolean oldValue = this.isAbstract;
        this.isAbstract = isAbstract;
        fire("isAbstract", oldValue, this.isAbstract);
    }

    @Override
    public void removeMemberFromContainer(MemberBase member) {
        if(member instanceof Field) fields.values().remove((Field)member);
        else if (member instanceof MethodBase) methods.values().remove((MethodBase)member);
        else if (member instanceof Constructor) constructors.values().remove((Constructor)member);
        else throw new RuntimeException("Removing unsupported member: "+member.toString());
    }
    
}
