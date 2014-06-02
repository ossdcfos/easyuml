package org.uml.model;

import java.util.HashMap;
import java.util.Random;

/**
 * Representation of Enumerations (Enums) from UML Class diagrams. Is one of
 * four possible types of ClassDiagramComponents. Used to represent enumeration
 * classes in UML diagrams.
 *
 * @author zoran
 * @see ClassDiagramComponents
 * @see ClassComponent
 * @see InterfaceComponent
 * @see PackageComponent
 */
public class EnumComponent extends ClassDiagramComponent {

    private HashMap<String, Field> fields;
    private HashMap<String, Method> methods;
    private HashMap<String, Literal> literals;
    private HashMap<String, Constructor> constructors;

    /**
     * Default constructor without parameters. Sets name to default value and
     * instantiates fields, methods, constructors and literals collections.
     */
    public EnumComponent() {
        this.setName("UntitledEnum");
        fields = new HashMap<String, Field>();
        methods = new HashMap<String, Method>();
        constructors = new HashMap<String, Constructor>();
        literals = new HashMap<String, Literal>();
    }

    /**
     * Parameterized Constructor, calls the default constructor (only sets name
     * to default value).
     *
     * @param name of EnumComponent
     * @see ClassDiagramComponent
     */
    public EnumComponent(String name) {
        super(name);
    }
/**
 * Returns the collection of fields that this enum contains
 * @return HashMap of enum's fields
 */
    public HashMap<String, Field> getFields() {
        return fields;
    }
/**
 * Returns the collection of this enum's methods
 * @return HashMap of enum's methods
 */
    public HashMap<String, Method> getMethods() {
        return methods;
    }
/**
 *  Returns the collection of this enum's constructors
 * @return HashMap of enum's methods
 */
    public HashMap<String, Constructor> getConstructors() {
        return constructors;
    }
/**
 * Returns the collection of literals that this enum contains
 * @return HashMap of enum's literals
 */
    public HashMap<String, Literal> getLiterals() {
        return literals;
    }
/**
 * Returns enum's field with the given name from the collection of fields
 * @param name of the field that is to be returned
 * @return enum's field
 */
    public Field getField(String name) {
        return fields.get(name);
    }

    /**
     * Adds a field to EnumComponent's collection of fields.
     * <p>
     * Automatically generates the first parameter of fields collection (the
     * second is field supplied as a parameter).
     *
     * @param field that will be added to collection
     * @see Field
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
     * Removes a field from EnumComponent's collection of fields.
     *
     * @param name of field which will be removed
     * @see Field
     */
    public void removeField(String name) {
        fields.remove(name);
    }
/**
 * Returns the method with the specific name given from enum's collection of methods
 * @param name of the method to be returned
 * @return enum's method with a given name
 */
    public Method getMethod(String name) {
        return methods.get(name);
    }

    /**
     * Adds a method to EnumComponent's collection of methods.
     * <p>
     * Automatically generates the first parameter of methods collection (the
     * second is method supplied as a parameter).
     *
     * @param method that will be added to collection
     * @see Method
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
     * Removes a method from EnumComponent's collection of methods.
     *
     * @param name of method which will be removed
     * @see Method
     */
    public void removeMethod(String name) {
        methods.remove(name);
    }
/**
 * Returns a constructor with a given name from the collection of constructors
 * @param name of the constructor to be returned
 * @return this enum's constructor
 */
    public Constructor getConstuctor(String name) {
        return constructors.get(name);
    }

    /**
     * Adds a constructor to EnumComponent's collection of constructors.
     * <p>
     * Automatically generates the first parameter of constructors collection
     * (the second is constructor supplied as a parameter).
     *
     * @param constructor that will be added to collection
     */
    public void addConstructor(Constructor constructor) {
        addMember(constructor);
        constructor.setDeclaringClass(this);
        Random r = new Random();
        int Low = 0;
        int High = 100;
        int R = r.nextInt(High - Low) + Low;
        constructors.put(Integer.toString(R), constructor);
    }

    /**
     * Removes a constructor from EnumComponent's collection of constructors.
     *
     * @param name of constructor which will be removed
     */
    public void removeConstructor(String name) {
        constructors.remove(name);
    }
/**
 * Returns the enum's literal with the name provided
 * @param name of the needed literal
 * @return enum's literal with the name given
 */
    public Literal getLiteral(String name) {
        return literals.get(name);
    }

    /**
     * Adds a literal to EnumComponent's collection of literals
     * <p>
     * Automatically generates first parameter of literals collection (the
     * second is literal supplied as a parameter).
     *
     * @param literal that will be added to collection
     */
    public void addLiteral(Literal literal) {
        addMember(literal);
        literal.setDeclaringClass(this);
        Random r = new Random();
        int Low = 0;
        int High = 100;
        int R = r.nextInt(High - Low) + Low;
        literals.put(Integer.toString(R), literal);
    }

    /**
     * Removes literal from EnumComponent's collection of literals.
     *
     * @param name of literal which will be removed
     */
    public void removeLiteral(String name) {
        literals.remove(name);
    }
}
