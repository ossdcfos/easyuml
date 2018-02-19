package org.uml.model.components;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import org.uml.model.members.Field;
import org.uml.model.members.Constructor;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.uml.model.ClassDiagram;
import org.uml.model.GetterSetterGeneration;
import org.uml.model.members.MemberBase;
import org.uml.model.members.Method;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.RelationBase;

/**
 * Class component in a class diagram.
 *
 * @author Uros
 * @see InterfaceComponent
 * @see EnumComponent
 */
public class ClassComponent extends ComponentBase {

    /**
     * Set of fields this class contains.
     */
    private LinkedHashSet<Field> fields;

    /**
     * Set of constructors this class contains.
     */
    private LinkedHashSet<Constructor> constructors;

    /**
     * Set of method this class contains.
     */
    private LinkedHashSet<Method> methods;
    
    /**
     * Getters automatic generation setting
     */
    private GetterSetterGeneration      getterGeneration;
    /**
     * Setters automatic generation setting
     */
    private GetterSetterGeneration      setterGeneration;
    
    /**
     * Default constructor. Sets name to default value.
     * <p>
     * Used when deserializing or adding a new component from palette or popup menu.
     */
    public ClassComponent() {
        this("UntitledClass");
    }

    /**
     * Constructor with parameters which calls its super constructor.
     * Sets the name of the class.
     * Instantiates fields, methods and constructors collections.
     *
     * @param name to be set
     */
    // Used when reverse engineering
    public ClassComponent(String name) {
        super(name);
        fields = new LinkedHashSet<>();
        constructors = new LinkedHashSet<>();
        methods = new LinkedHashSet<>();
        getterGeneration = GetterSetterGeneration.AUTO;
        setterGeneration = GetterSetterGeneration.AUTO;
    }

    /**
     * Returns the collection of fields that this class contains.
     *
     * @return HashSet of fields contained
     */
    public LinkedHashSet<Field> getFields() {
        return fields;
    }

    /**
     * Returns the collection of methods that this class contains.
     *
     * @return HashSet of methods contained
     */
    public LinkedHashSet<Method> getMethods() {
        return methods;
    }

    /**
     * Returns the collection of constructors that this class contains.
     *
     * @return HashSet of constructors contained
     */
    public LinkedHashSet<Constructor> getConstructors() {
        return constructors;
    }

    /**
     * Adds the given field to the class.
     * Sets the declaring component of the field to be this class.
     *
     * @param field to add
     */
    public void addField(Field field) {
        // Adds to ComponentBase container (components collection - set of all members).
        addComponentToContainter(field);
        field.setDeclaringComponent(this);
        // Adds to ClassComponent's fields (set of fields only).
        fields.add(field);
    }

    /**
     * Adds the given methode to the class.
     * Sets the declaring component of the method to be this class.
     *
     * @param method to add
     */
    public void addMethod(Method method) {
        addComponentToContainter(method);
        method.setDeclaringComponent(this);
        methods.add(method);
    }

    /**
     * Adds the given constructor to the class.
     * Sets the declaring component of the constructor to be this class.
     *
     * @param constructor to add
     */
    public void addConstructor(Constructor constructor) {
        addComponentToContainter(constructor);
        constructor.setDeclaringComponent(this);
        constructors.add(constructor);
    }

    /**
     * Returns true if abstract modifier bit is set, false if not.
     *
     * @return true if class is abstract
     */
    public boolean isAbstract() {
        return Modifier.isAbstract(modifiers);
    }

    /**
     * Sets the abstract modifier to true or false. Fires "isAbstract" property change event.
     *
     * @param isAbstract true if the class is abstract, false if not
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

    /**
     * Returns true if static modifier bit is set, false if not.
     *
     * @return true if class is static
     */
    public boolean isStatic() {
        return Modifier.isStatic(modifiers);
    }

    /**
     * Sets the static modifier to true or false. Fires "isStatic" property change event.
     *
     * @param isStatic true if the class is static, false if not
     */
    public void setStatic(boolean isStatic) {
        int oldModifiers = modifiers;
        if (isStatic) {
            addModifier(Modifier.STATIC);
        } else {
            removeModifier(Modifier.STATIC);
        }
        pcs.firePropertyChange("isStatic", Modifier.isStatic(oldModifiers), isStatic());
    }

    /**
     * Returns true if final modifier bit is set, false if not.
     *
     * @return true if class is final
     */
    public boolean isFinal() {
        return Modifier.isFinal(modifiers);
    }

    /**
     * Sets the final modifier to true or false. Fires "isFinal" property change event.
     *
     * @param isFinal true if the class is final, false if not
     */
    public void setFinal(boolean isFinal) {
        int oldModifiers = modifiers;
        if (isFinal) {
            addModifier(Modifier.FINAL);
        } else {
            removeModifier(Modifier.FINAL);
        }
        pcs.firePropertyChange("isFinal", Modifier.isFinal(oldModifiers), isFinal());
    }

    /**
     * Removes the member from the associated collection.
     *
     * @param member to be removed
     */
    @Override
    public void removeMember(MemberBase member) {
        removeComponentFromContainer(member);
        if (member instanceof Field) fields.remove((Field) member);
        else if (member instanceof Method) methods.remove((Method) member);
        else if (member instanceof Constructor) constructors.remove((Constructor) member);
//        else throw new RuntimeException("Removing unsupported member: " + member.toString());
    }

    public GetterSetterGeneration getGetterGeneration() {
        return getterGeneration;
    }

    public void setGetterGeneration(GetterSetterGeneration generateGetters) {
        this.getterGeneration = generateGetters;
    }

    public GetterSetterGeneration getSetterGeneration() {
        return setterGeneration;
    }

    public void setSetterGeneration(GetterSetterGeneration generateSetters) {
        this.setterGeneration = generateSetters;
    }
    
    /**
     * Returns setting for getter generation if not auto, other wise returns
     * parent setting
     * @return 
     */
    public GetterSetterGeneration getInheritedGetterGeneration() {
        if (getterGeneration == GetterSetterGeneration.AUTO) {
            return getParentDiagram().getGetterGeneration();
        }
        return getterGeneration;
    }

    /**
     * Returns setting for setter generation if not auto, other wise returns
     * parent setting
     * @return 
     */
    public GetterSetterGeneration getInheritedSetterGeneration() {
        if (setterGeneration == GetterSetterGeneration.AUTO) {
            return getParentDiagram().getSetterGeneration();
        }
        return setterGeneration;
    }
    
    /**
     * Get fields that need a getter method
     * 
     * @return list of getters
     */
    public List<Field> getRequestedGetters() {
        List<Field> list = new ArrayList();
        for (Field field: fields) {
            if (!field.getterGenerationRequested())
                continue;
            Method method = field.createGetter();
            if (signatureExists(method.getSignature()))
                continue;
            list.add(field);
        }
        return list;
    }
    
    /**
     * Get generated getter methods (not added to the class)
     * 
     * Only requested and undeclerared ones are created
     * 
     * @return list of getters
     */
    public List<Field> getRequestedSetters() {
        List<Field> list = new ArrayList();
        for (Field field: fields) {
            if (!field.getterGenerationRequested())
                continue;
            Method method = field.createSetter();
            if (signatureExists(method.getSignature()))
                continue;
            list.add(field);
        }
        return list;
    }    
    
    public boolean hasMethod(String methodSignature) {
        for (Method method : methods) {
            if (methodSignature.equals(method.getSignature())) {
                return true;
            }
        }
        return false;
    }
    
    public LinkedHashSet<Method> getUnimplementedMethods() {
        ClassDiagram diagram = getParentDiagram();
        HashSet<RelationBase> relations = diagram.getRelations();
        LinkedHashSet<Method> unimplementedMethods = new LinkedHashSet();
        for(RelationBase relation : relations) {
            if (relation.getSource() != this)
                continue;
            if (relation instanceof ImplementsRelation) {
                InterfaceComponent interfaceComponent = (InterfaceComponent)relation.getTarget();
                LinkedHashSet<Method> parentMethods = interfaceComponent.getMethods();
                for(Method parentMethod : parentMethods) {
                    String parentSignature = parentMethod.getSignature();
                    if (hasMethod(parentSignature))
                        continue;
                    boolean found = false;
                    for (Method method : unimplementedMethods) {
                        if (parentSignature.equals(method.getSignature())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        try {
                            Method method = (Method)parentMethod.clone();
                            unimplementedMethods.add(method);
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(ClassComponent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
            if (relation instanceof IsRelation) {
                ClassComponent classComponent = (ClassComponent)relation.getTarget();
                if (!classComponent.isAbstract())
                    continue;
                LinkedHashSet<Method> parentMethods = classComponent.getMethods();
                for(Method parentMethod : parentMethods) {
                    if (!parentMethod.isAbstract())
                        continue;
                    String parentSignature = parentMethod.getSignature();
                    if (hasMethod(parentSignature))
                        continue;
                    boolean found = false;
                    for (Method method : unimplementedMethods) {
                        if (parentSignature.equals(method.getSignature())) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        try {
                            Method method = (Method)parentMethod.clone();
                            unimplementedMethods.add(method);
                        } catch (CloneNotSupportedException ex) {
                            Logger.getLogger(ClassComponent.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        return unimplementedMethods;
    }

}
