package org.uml.model.components;

import org.uml.model.members.*;
import java.awt.*;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.List;
import org.uml.model.ClassDiagram;
import org.uml.model.ContainerBase;
import org.uml.model.IHasSignature;
import org.uml.model.INameable;
import org.uml.model.Visibility;
import org.uml.model.relations.RelationBase;

/**
 * Base class for all UML class diagram components (classes, interfaces or enums)
 * that can be added to class diagram. Note that relations are not
 * represented with this class as well; they are represented with RelationBase class.
 * <p>Implements INameable and IHasSignature so it can be a member of ContainerBase (i.e. ClassDiagram).
 *
 * @author zoran
 * @see ClassComponent
 * @see InterfaceComponent
 * @see EnumComponent
 * @see RelationBase
 */
public abstract class ComponentBase extends ContainerBase<MemberBase> implements INameable, IHasSignature {

    /**
     * Parent diagram which contains this component. It is set when the component is added to the diagram,
     * but could be moved as a constructor parameter to be obligatory. TODO
     */
    private transient ClassDiagram parentDiagram;

    /**
     * Parent package string.
     */
    private String parentPackage;

    /**
     * Visibility of the component. Can be public, private, package, protected in general, but
     * depends on the actual component.
     */
    private Visibility visibility;

    /**
     * Modifiers integer which stores modifiers as bits, as defined in java.lang.reflect.Modifier.
     *
     * @see java.lang.reflect.Modifier
     */
    protected int modifiers;

    // TODO these should be removed in future (use only bounds)
    /**
     * Location of the component on the diagram.
     */
    private Point location;
    /**
     * Bounds of the component on the diagram.
     */
    private Rectangle bounds;

    /**
     * Default constructor. Initializes members of ComponentBase. Members can be
     * fields, methods, constructors and literals.
     *
     * @param name of component
     * @see Field
     * @see Method
     * @see Constructor
     * @see Literal
     */
    protected ComponentBase(String name) {
        super(name);
        parentPackage = "";
        visibility = Visibility.PUBLIC;
    }

    /**
     * Removes the given member. It is an abstract method, as in different components
     * we need to remove members from different sets (like fields, methods etc.).
     * TODO maybe make separate methods.
     *
     * @param member
     */
    public abstract void removeMember(MemberBase member);

    /**
     * Returns members that this component has.
     *
     * @return members of this component
     */
    public LinkedHashSet<MemberBase> getMembers() {
        return new LinkedHashSet<>(components);
    }

    /**
     * Returns the parentDiagram of this component. Parent diagram is a
     * ClassDiagram object that contains this component.
     *
     * @return ClassDiagram containing this component
     * @see ClassDiagram
     */
    public ClassDiagram getParentDiagram() {
        return parentDiagram;
    }

    /**
     * Sets the parentDiagram of this component. Parent diagram is a
     * ClassDiagram object that contains this component.
     *
     * @param parentDiagram
     * @see ClassDiagram
     */
    public void setParentDiagram(ClassDiagram parentDiagram) {
        this.parentDiagram = parentDiagram;
    }

    /**
     * Returns the location of the component on the scene.
     *
     * @return point representing X and Y coordinates
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Sets the component's location on the scene.
     *
     * @param location point representing X and Y coordinates of the new location
     */
    public void setLocation(Point location) {
        this.location = location;
    }

    // TODO bounds
    public Rectangle getBounds() {
        return bounds;
    }

    // TODO bounds
    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    /**
     * Returns the parent package of the component.
     *
     * @return parent package
     */
    public String getParentPackage() {
        return parentPackage;
    }

    /**
     * Sets parent package of the component. Fires "parentPackage" property change event.
     *
     * @param parentPackage
     */
    public void setParentPackage(String parentPackage) {
        String oldParentPackage = this.parentPackage;
        this.parentPackage = parentPackage;
        pcs.firePropertyChange("parentPackage", oldParentPackage, parentPackage);
    }

    /**
     * Returns the visibility of this component.
     *
     * @return visibility of this component
     * @see Visibility
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility of this component. Fires "visibility" property change event.
     *
     * @param visibility to be set
     * @see Visibility
     */
    public void setVisibility(Visibility visibility) {
        Visibility oldVisibility = this.visibility;
        this.visibility = visibility;
        pcs.firePropertyChange("visibility", oldVisibility, visibility);
    }

    /**
     * Adds a modifier bit of java Modifier enum into the modifiers array.
     *
     * @param modifier to be added to the modifiers array
     * @see java.lang.reflect.Modifier
     */
    public void addModifier(int modifier) //throws Exception 
    {
        if ((this instanceof ClassComponent && (Modifier.classModifiers() & modifier) != 0)
                || (this instanceof InterfaceComponent && (Modifier.interfaceModifiers() & modifier) != 0)) {
            modifiers |= modifier;
        } else {
            //throw new Exception("Modifier " + Modifier.toString(modifier) + " not allowed for " + this.getClass().getSimpleName() + ".");
        }
    }

    /**
     * Removes the given modifier bit from the modifiers array. Use modifiers
     * defined in java.lang.reflect.Modifier class.
     *
     * @param modifier to be removed
     * @see java.lang.reflect.Modifier
     */
    public void removeModifier(int modifier) {
        modifiers &= ~modifier;
    }

    /**
     * Returns the unique signature of this component as a fully qualified name
     * (parentPackage.name).
     *
     * @return unique signature as a fully qualified name
     */
    @Override
    public String getSignature() {
        if (getParentPackage().equals("")) {
            return getName();
        } else {
            return getParentPackage() + "." + getName();
        }
    }
    
    @Override
    public String getUMLSignature() {
        return getSignature();
    }    

    /**
     * Makes a signature with a potential new name so that we can check if the component
     * with the same signature already exists.
     *
     * @param newName to make a signature from
     * @return
     */
    public String deriveSignatureFromNewName(String newName) {
        if (getParentPackage().equals("")) {
            return newName;
        } else {
            return getParentPackage() + "." + newName;
        }
    }

    /**
     * Makes a signature with a potential new package so that we can check if the component
     * with the same signature already exists.
     *
     * @param newParentPackage to make a signature from
     * @return
     */
    public String deriveSignatureFromNewPackage(String newParentPackage) {
        if (newParentPackage.equals("")) {
            return getName();
        } else {
            return newParentPackage + "." + getName();
        }
    }

    /**
     * Set a new name for this component. Updates all of the types in the diagram
     * that referenced this component.
     *
     * @param newName of this component
     */
    @Override
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        pcs.firePropertyChange("name", oldName, newName);

        // Update this component
        updateTypes(this, oldName, newName);
        // Update other components if this component is in a diagram
        if (parentDiagram != null) {
            for (ComponentBase component : parentDiagram.getComponents()) {
                if (component != this) {
                    updateTypes(component, oldName, newName);
                }
            }
        }
    }

    /**
     * Updates types of the component members based on new component name.
     * @param component to update
     * @param oldComponentName type to rename
     * @param newComponentName new type
     */
    private void updateTypes(ComponentBase component, String oldComponentName, String newComponentName) {
        for (MemberBase member : component.getMembers()) {
            if (member instanceof Constructor && component == this) {
                member.setName(newComponentName);
            } else if (member instanceof Method) {
                Method method = (Method) member;
                String newMethodType = method.getType().replaceAll("(?<!\\w)" + oldComponentName + "(?!\\w)", newComponentName);
                method.setType(newMethodType);
                for (MethodArgument argument : method.getArguments()) {
                    String newArgumentType = argument.getType().replaceAll("(?<!\\w)" + oldComponentName + "(?!\\w)", newComponentName);
                    method.setType(newArgumentType);
                }
            } else if (member instanceof Field) {
                Field field = (Field) member;
                String newFieldType = field.getType().replaceAll("(?<!\\w)" + oldComponentName + "(?!\\w)", newComponentName);
                field.setType(newFieldType);
            }
        }
    }
    
    public List<RelationBase> getRelevantRelations(){
        List<RelationBase> relevantRelations = new LinkedList<>();
        for (RelationBase rc : getParentDiagram().getRelations()) {
            if (rc.getSource().equals(this)) {
                relevantRelations.add(rc);
            }
        }
        return relevantRelations;
    }
}
