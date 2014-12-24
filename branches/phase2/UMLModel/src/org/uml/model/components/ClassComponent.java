package org.uml.model.components;

//import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import java.lang.reflect.Modifier;
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

    /**
     * Default constructor only specifying parent diagram. Sets name to default value.
     * 
     */
    // Used when deserializing and adding a new component from palette or popup menu
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
    // Used when reverse engineering
    public ClassComponent(String name) {
        super(name);
        fields = new LinkedHashSet<>();
        constructors = new LinkedHashSet<>();
        methods = new LinkedHashSet<>();
    }

    /**
     * Returns the collection of fields that this class contains
     *
     * @return HashSet of fields contained
     */
    public LinkedHashSet<Field> getFields() {
        return fields;
    }

    /**
     * Returns the collection of methods that this class contains
     *
     * @return HashSet of methods contained
     */
    public LinkedHashSet<Method> getMethods() {
        return methods;
    }

    /**
     * Returns the collection of constructors for this class
     *
     * @return HashSet of this clas's constructors
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
        addPartToContainter(field);
        field.setDeclaringComponent(this);
        fields.add(field);
    }

    /**
     * Adds the method to ClassComponent's collection of methods.
     *
     * @param method that will be added to collection.
     */
    public void addMethod(Method method) {
        addPartToContainter(method);
        method.setDeclaringComponent(this);
        methods.add(method);
    }

    /**
     * Adds the Constructor to ClassComponent's collection of constructors.
     *
     * @param constructor which will be added to collection.
     */
    public void addConstructor(Constructor constructor) {
        addPartToContainter(constructor);
        constructor.setDeclaringComponent(this);
        constructors.add(constructor);
    }

    /**
     * Returns true if this class is set to be abstract, false if not
     *
     * @return true if class is abstract
     */
    public boolean isAbstract() {
        return Modifier.isAbstract(modifiers);
    }

    
    /**
     * Sets abstract modifier to true or false
     *
     * @param isAbstract - true if the class is abstract, if not - false
     */
    public void setAbstract(boolean isAbstract) {
        int oldModifiers = modifiers;
        if (isAbstract) {
            addModifier(Modifier.ABSTRACT);
        } else {
            removeModifier(Modifier.ABSTRACT);
        }
        pcs.firePropertyChange("isAbstract", Modifier.isAbstract(oldModifiers), isAbstract());
    }

    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    public void setStatic(boolean isStatic) {
        int oldModifiers = modifiers;
        if (isStatic) {
            addModifier(Modifier.STATIC);
        } else {
            removeModifier(Modifier.STATIC);
        }
        pcs.firePropertyChange("isStatic", Modifier.isStatic(oldModifiers), isStatic());
    }

    public boolean isFinal() {
        return Modifier.isFinal(modifiers);
    }

    public void setFinal(boolean isFinal) {
        int oldModifiers = modifiers;
        if (isFinal) {
            addModifier(Modifier.FINAL);
        } else {
            removeModifier(Modifier.FINAL);
        }
        pcs.firePropertyChange("isFinal", Modifier.isFinal(oldModifiers), isFinal());
    }
    

    @Override
    public void removeMember(MemberBase member) {
        if(member instanceof Field) fields.remove((Field)member);
        else if (member instanceof Method) methods.remove((Method)member);
        else if (member instanceof Constructor) constructors.remove((Constructor)member);
        else throw new RuntimeException("Removing unsupported member: "+member.toString());
    }
    
}
