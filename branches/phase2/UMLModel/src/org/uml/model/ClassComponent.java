package org.uml.model;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Random;

/**
 * Representation of Classes from UML Class diagrams. Is one of four possible
 * types of ClassDiagramComponents.
 *
 * @author Uros
 * @see ClassDiagramComponent
 * @see EnumComponent
 * @see InterfaceComponent
 * @see PackageComponent
 */
public class ClassComponent extends ClassDiagramComponent {

    private HashMap<String, Constructor> constructors;
    private HashMap<String, Field> fields;
    private HashMap<String, Method> methods;
    private boolean isAbstract;
    // private Type type; // we dont know why this is here...

    /**
     * Default constructor without parameters. Sets name to default value,
     * instantiates fields, methods and constructors collections, as well as
     * parentPackage field; sets visibility to public and isAbstract to false.
     */
    public ClassComponent() {
        this.setName("UntitledClass");
        fields = new HashMap<String, Field>();
        methods = new HashMap<String, Method>();
        constructors = new HashMap<String, Constructor>();
        setVisibility(Visibility.PUBLIC);
        isAbstract = false;
        parentPackage = null;
    }

    /**
     * Constructor with parameters which calls its super constructor.
     * <p>
     * Only sets the name of Class.
     *
     * @param name to be set
     */
    public ClassComponent(String name) {
        super(name);
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
     * Returns a field from this clas's collection of fields with a given name
     *
     * @param name of the field that needs to be returned
     * @return this clas's field with a given name
     */
    public Field getField(String name) {
        return fields.get(name);
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
     * Returns a method from this clas's collection of methods
     *
     * @param name of the method to be returned
     * @return method with a given name
     */
    public Method getMethod(String name) {
        return methods.get(name);
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
     * Returns a constructor with a given name that this class contains
     *
     * @param name of the wanted constructor
     * @return constructor of this class
     */
    public Constructor getConstuctor(String name) {
        return constructors.get(name);
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
        methods.values().remove(constructor);
    }

    /**
     * Returns visibility modifier of this class
     *
     * @return visibility of the class
     * @see Visibility
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility modifier of this class
     *
     * @param visibility to be set
     * @see Visibility
     */
    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    /**
     * Returns true if this class is set to be abstract, false if not
     *
     * @return true if class is abstract
     */
    public boolean isIsAbstract() {
        return isAbstract;
    }

    /**
     * Sets abstract modifier to true or false
     *
     * @param isAbstract - true if the class is abstract, if not - false
     */
    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }
//    public Type getType() {
//        return type;
//    }
//
//    public void setType(Type type) {
//        this.type = type;
//    }
}
