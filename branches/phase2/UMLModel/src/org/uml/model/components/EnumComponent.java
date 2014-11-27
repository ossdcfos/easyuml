package org.uml.model.components;

import java.util.LinkedHashMap;
import org.uml.model.ClassDiagram;
import org.uml.model.members.Literal;
import org.uml.model.members.MemberBase;

/**
 * Enumerations (Enums) from UML Class diagrams. Used to represent enumeration
 * classes in UML diagrams.
 *
 * @author zoran
 * @see ClassDiagramComponents
 * @see ClassComponent
 * @see InterfaceComponent
 * @see PackageComponent
 */
public class EnumComponent extends ComponentBase {

//    private LinkedHashMap<String, Field> fields;
//    private LinkedHashMap<String, Method> methods;
//    private LinkedHashMap<String, Constructor> constructors;
    private LinkedHashMap<String, Literal> literals;

    /**
     * Default constructor only specifying parent diagram. Sets name to default value.
     * @param parentDiagram
     */
    public EnumComponent(ClassDiagram parentDiagram) {
        this(parentDiagram, "UntitledEnum");
    }

    /**
     * Parameterized Constructor, calls the default constructor (sets the name
     * to default value). Instantiates fields, methods, constructors and
     * literals collections.
     *
     * @param parentDiagram
     * @param name of EnumComponent
     * @see ComponentBase
     */
    public EnumComponent(ClassDiagram parentDiagram, String name) {
        super(parentDiagram, name);
//        fields = new LinkedHashMap<>();
//        methods = new LinkedHashMap<>();
//        constructors = new LinkedHashMap<>();
        literals = new LinkedHashMap<>();
    }

//    /**
//     * Returns the collection of fields that this enum contains
//     *
//     * @return HashMap of enum's fields
//     */
//    public LinkedHashMap<String, Field> getFields() {
//        return fields;
//    }
//
//    /**
//     * Returns the collection of this enum's methods
//     *
//     * @return HashMap of enum's methods
//     */
//    public LinkedHashMap<String, Method> getMethods() {
//        return methods;
//    }
//
//    /**
//     * Returns the collection of this enum's constructors
//     *
//     * @return HashMap of enum's methods
//     */
//    public HashMap<String, Constructor> getConstructors() {
//        return constructors;
//    }

    /**
     * Returns the collection of literals that this enum contains
     *
     * @return HashMap of enum's literals
     */
    public LinkedHashMap<String, Literal> getLiterals() {
        return literals;
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
//    public void addField(Field field) {
//        addMember(field);
//        field.setDeclaringClass(this);
//        fields.put(field.toString(), field);
//    }

    /**
     * Adds a method to EnumComponent's collection of methods.
     * <p>
     * Automatically generates the first parameter of methods collection (the
     * second is method supplied as a parameter).
     *
     * @param method that will be added to collection
     * @see MethodBase
     */
//    public void addMethod(Method method) {
//        addMember(method);
//        method.setDeclaringClass(this);
//        methods.put(method.toString(), method);
//    }

    /**
     * Adds a constructor to EnumComponent's collection of constructors.
     * <p>
     * Automatically generates the first parameter of constructors collection
     * (the second is constructor supplied as a parameter).
     *
     * @param constructor that will be added to collection
     */
//    public void addConstructor(Constructor constructor) {
//        addMember(constructor);
//        constructor.setDeclaringClass(this);
//        constructors.put(constructor.toString(), constructor);
//    }

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
        literals.put(literal.toString(), literal);
    }

    @Override
    public void removeMemberFromContainer(MemberBase member) {
//        if(member instanceof Field) fields.remove((Field)member);
//        else if (member instanceof MethodBase) methods.values().remove((MethodBase)member);
//        else if (member instanceof Constructor) constructors.values().remove((Constructor)member);
//        else 
        if (member instanceof Literal) literals.values().remove((Literal)member);
        else throw new RuntimeException("Removing unsupported member: "+member.toString());
    }
}
