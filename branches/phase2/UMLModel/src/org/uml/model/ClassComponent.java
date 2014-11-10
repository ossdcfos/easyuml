package org.uml.model;

import org.uml.model.members.Method;
import org.uml.model.members.Field;
import org.uml.model.members.Constructor;
import java.util.HashMap;
import java.util.Random;

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

    private HashMap<String, Field> fields;
    private HashMap<String, Constructor> constructors;
    private HashMap<String, Method> methods;
    private boolean isAbstract;

    /**
     * Default constructor without parameters. Sets name to default value,
     * 
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
     * @param name to be set
     */
    public ClassComponent(String name) {
        super(name);
        //setParentPackage(null);
        fields = new HashMap<>();
        constructors = new HashMap<>();
        methods = new HashMap<>();
        isAbstract = false;
    }

    /**
     * Returns the collection of fields that this class contains
     *
     * @return HashMap of fields contained
     */
    public HashMap<String, Field> getFields() {
        return fields;
    }

    /**
     * Returns the collection of methods that this class contains
     *
     * @return HashMap of methods contained
     */
    public HashMap<String, Method> getMethods() {
        return methods;
    }

    /**
     * Returns the collection of constructors for this class
     *
     * @return HashMap of this clas's constructors
     */
    public HashMap<String, Constructor> getConstructors() {
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
        Random r = new Random();
        int Low = 0;
        int High = 100;
        int R = r.nextInt(High - Low) + Low;
        fields.put(Integer.toString(R), field);
    }

    /**
     * Removes the field given form this clas's collection of fields
     *
     * @param field to be removed
     */
    public void removeField(Field field) {
        fields.values().remove(field);
    }

    /**
     * Adds the method to ClassComponent's collection of methods.
     *
     * @param method that will be added to collection.
     */
    public void addMethod(Method method) {
        addMember(method);
        method.setDeclaringClass(this);
        Random r = new Random();
        int Low = 0;
        int High = 100;
        int R = r.nextInt(High - Low) + Low;
        methods.put(Integer.toString(R), method);
    }

    /**
     * Removes the method from ClassComponent's collection of methods.
     *
     * @param method of the method which will be removed.
     */
    public void removeMethod(Method method) {
        methods.values().remove(method);
    }

    /**
     * Adds the Constructor to ClassComponent's collection of constructors.
     *
     * @param constructor which will be added to collection.
     */
    public void addConstructor(Constructor constructor) {
        addMember(constructor);
        constructor.setDeclaringClass(this);
        constructors.put(this.getName(), constructor);
    }

    /**
     * Removes the Constructor from a ClassComponent's collection of
     * constructors.
     *
     * @param constructor of constructor which will be removed.
     */
    public void removeConstructor(Constructor constructor) {
        constructors.values().remove(constructor);
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
        this.isAbstract = isAbstract;
    }
    
}
