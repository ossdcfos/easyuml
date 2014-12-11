package org.uml.model.components;

//import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import org.uml.model.members.Field;
import org.uml.model.members.Constructor;
import java.util.LinkedHashSet;
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

//    @XStreamAsAttribute
    private LinkedHashSet<Field> fields;
    private LinkedHashSet<Constructor> constructors;
    private LinkedHashSet<Method> methods;
    private boolean isAbstract;
    private boolean isStatic;
    private boolean isFinal;

    /**
     * Default constructor only specifying parent diagram. Sets name to default value.
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
        fields = new LinkedHashSet<>();
        constructors = new LinkedHashSet<>();
        methods = new LinkedHashSet<>();
        isAbstract = false;
    }

    /**
     * Returns the collection of fields that this class contains
     *
     * @return HashMap of fields contained
     */
    public LinkedHashSet<Field> getFields() {
        return fields;
    }

    /**
     * Returns the collection of methods that this class contains
     *
     * @return HashMap of methods contained
     */
    public LinkedHashSet<Method> getMethods() {
        return methods;
    }

    /**
     * Returns the collection of constructors for this class
     *
     * @return HashMap of this clas's constructors
     */
    public LinkedHashSet<Constructor> getConstructors() {
        return constructors;
    }
    
    /**
     * Adds the field to ClassComponent's collection of fields.
     *
     * @param field that will be added to collection.
     */
    public void addField(Field field) {
        addComponent(field);
//        addMember(field);
        field.setDeclaringComponent(this);
        fields.add(field);
    }

    /**
     * Adds the method to ClassComponent's collection of methods.
     *
     * @param method that will be added to collection.
     */
    public void addMethod(Method method) {
//        addMember(method);
        addComponent(method);
        method.setDeclaringComponent(this);
        methods.add(method);
    }

    /**
     * Adds the Constructor to ClassComponent's collection of constructors.
     *
     * @param constructor which will be added to collection.
     */
    public void addConstructor(Constructor constructor) {
//        addMember(constructor);
        addComponent(constructor);
        constructor.setDeclaringComponent(this);
        constructors.add(constructor);
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

    public boolean isStatic() {
        return isStatic;
    }

    public void setStatic(boolean isStatic) {
        boolean oldValue = this.isStatic;
        this.isStatic = isStatic;
        fire("isStatic", oldValue, this.isStatic);
    }

    public boolean isFinal() {
        return isFinal;
    }

    public void setFinal(boolean isFinal) {
        boolean oldValue = this.isFinal;
        this.isFinal = isFinal;
        fire("isFinal", oldValue, this.isFinal);
    }

    @Override
    public void removeMemberFromContainer(MemberBase member) {
        if(member instanceof Field) fields.remove((Field)member);
        else if (member instanceof Method) methods.remove((Method)member);
        else if (member instanceof Constructor) constructors.remove((Constructor)member);
        else throw new RuntimeException("Removing unsupported member: "+member.toString());
    }
    
}
