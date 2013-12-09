package org.uml.model;

import java.util.HashMap;

/**
 * Implementation of EnumComponent, one of four posible types of ClassDiagramComponents.
 * @author zoran
 * @see ClassDiagramComponents
 * @see ClassComponent
 * @see RelationComponent
 * @see PackageComponent
 */
public class EnumComponent extends ClassDiagramComponent {
    private HashMap<String, Field> fields;
    private HashMap<String, Method> methods;
    private HashMap<String, Literal> literals;
    private HashMap<String, Constructor> constructors;
    
    /**
     * Default constructor without parameters
     */
    public EnumComponent() {
        this.setName("UntitledEnum");
        fields= new HashMap<String, Field>();
        methods= new HashMap<String, Method>();
        constructors = new HashMap<String, Constructor>();
        literals = new HashMap<String, Literal>();
    }

    /**
     * Parameterized Constructor, calls default constructor
     * @param name of EnumComponent
     */
    public EnumComponent(String name) {
        super(name);
    }

    public HashMap<String, Field> getFields() {
        return fields;
    }

    public HashMap<String, Method> getMethods() {
        return methods;
    }

    public HashMap<String, Constructor> getConstructors() {
        return constructors;
    }

    public Field getField (String name) {
        return fields.get(name);
    }
    
    /**
     * Adds a field to EnumComponent's collection of fields
     * @param field that will be added to collection
     */
    public void addField (Field field) {
        field.setDeclaringClass(this);
        fields.put(field.getName(), field);
        addMember(field);
    } 
    
    /**
     * Removes field from EnumComponent's collection of fields
     * @param name of field which will be removed
     */
    public void removeField (String name) {
        fields.remove(name);
    }
    
     public Method getMethod(String name) {
        return methods.get(name);
    }    
     
    /**
     * Adds a method to EnumComponent's collection of methods
     * @param method that will be added to collection
     */    
    public void addMethod(Method method) {
        method.setDeclaringClass(this);
        methods.put(method.getName(), method);
        addMember(method);
    }

     /**
     * Removes method from EnumComponent's collection of methods
     * @param name of method which will be removed
     */
    public void removeMethod(String name) {
        methods.remove(name);
    }
    
    public Constructor getConstuctor(String name) {
        return constructors.get(name);
    }    
    
     /**
     * Adds a constructor to EnumComponent's collection of constructors
     * @param constructor that will be added to collection
     */
    public void addConstructor(Constructor constructor) {
        constructor.setDeclaringClass(this);
        constructors.put(this.getName(), constructor);
        addMember(constructor);
    }
    
     /**
     * Removes constructor from EnumComponent's collection of constructors
     * @param name of constructor which will be removed
     */
    public void removeConstructor(String name) {
        constructors.remove(name);
    }
    
    public Literal getLiteral(String name) {
        return literals.get(name);
    }    
    
     /**
     * Adds a literal to EnumComponent's collection of literals
     * @param literal that will be added to collection
     */
    public void addLiteral(Literal literal) {
        literal.setDeclaringClass(this);
        literals.put(this.getName(), literal);
        addMember(literal);
    }
    
     /**
     * Removes literal from EnumComponent's collection of literals
     * @param name of literal which will be removed
     */
    public void removeLiteral(String name) {
        literals.remove(name);
    }
}
