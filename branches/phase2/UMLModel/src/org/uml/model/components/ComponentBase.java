package org.uml.model.components;

import org.uml.model.members.*;
import java.awt.*;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.*;
import org.uml.*;
import org.uml.model.ClassDiagram;
import org.uml.model.ContainerBase;
import org.uml.model.Visibility;
import org.uml.model.relations.RelationBase;

/**
 * Base class for all UML class diagram components (classes, interfaces or
 * enums) that can be added to class diagram. Note that relations are not
 * represented with this class as well; they are represented with
 * RelationComponent.
 *
 * @author zoran
 * @see ClassComponent
 * @see RelationBase
 */
public abstract class ComponentBase extends ContainerBase<MemberBase> implements Serializable {

    private transient ClassDiagram parentDiagram;

    private String parentPackage;
    private Visibility visibility;
    protected int modifiers;

    // these should be removed in future
    private Point position;
    private Rectangle bounds;

    /**
     * Default constructor. Initializes members of the ComponentBase. Members
     * can be fields, methods, constructors and literals.
     *
     * @param name of component
     * @see Field
     * @see Method
     * @see Constructor
     * @see Literal
     */
    public ComponentBase(String name) {
        super(name);
        parentPackage = "";
        visibility = Visibility.PUBLIC;
    }

    public abstract void removeMemberFromContainer(MemberBase member);

    /**
     * Returns members that this ClassDiagramComponent has
     *
     * @return all members of this ClassDiagramComponent
     */
    @SuppressWarnings("unchecked")
    public LinkedHashSet<MemberBase> getMembers() {
        return new LinkedHashSet<>(containerComponents);
    }

    /**
     * Returns parentDiagram of this ClassDiagramComponent. Parent diagram is a
     * ClassDiagram object that contains this ClassDiagramComponent.
     *
     * @return ClassDiagram containing this ClassDiagramComponent
     * @see ClassDiagram
     */
    public ClassDiagram getParentDiagram() {
        return parentDiagram;
    }

    /**
     * Sets parentDiagram of this ClassDiagramComponent. Parent diagram is a
     * ClassDiagram object that contains this ClassDiagramComponent.
     *
     * @param parentDiagram
     * @see ClassDiagram
     */
    public void setParentDiagram(ClassDiagram parentDiagram) {
        this.parentDiagram = parentDiagram;
    }

    /**
     * Returns position that this ClassDiagramComponent has on the scene
     *
     * @return point that represents X and Y coordinates
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Sets this ClassDiagramComponent's position on scene
     *
     * @param position that the component has
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    public String getParentPackage() {
        return parentPackage;
    }

    public void setParentPackage(String parentPackage) {
        String oldParentPackage = this.parentPackage;
        this.parentPackage = parentPackage;
        pcs.firePropertyChange("parentPackage", oldParentPackage, parentPackage);
    }

    @Override
    public String getSignature() {
//        if(!getParentPackage().equals("")){
            return getParentPackage() + " " + getName();
//        } else return getName();
    }

    public String deriveNewSignatureFromName(String newName) {
//        if(!getParentPackage().equals("")){
            return getParentPackage() + " " + newName;
//        } else return newName;
    }

    public String deriveNewSignatureFromPackage(String newParentPackage) {
        return newParentPackage + " " + getName();
    }
    
    /**
     * Adds numerical representation of java Modifier enum's constants into
     * modifiers array.
     * <p>
     * Modifiers array can hold up to four modifier constants. Modifiers are
     * thoroughly explained in Member class.
     *
     * @param modifier to be added to modifiers array
     * @see java.lang.reflect.Modifier
     * @see MemberBase
     */
    public void addModifier(int modifier) //throws Exception 
    {
        if ((this instanceof ClassComponent && (Modifier.classModifiers()& modifier) != 0)
                || (this instanceof InterfaceComponent && (Modifier.interfaceModifiers()& modifier) != 0)) {
            modifiers |= modifier;
        } else {
            //throw new Exception("Modifier " + Modifier.toString(modifier) + " not allowed for " + this.getClass().getSimpleName() + ".");
        }
    }

    /**
     * Removes given modifier integer from modifiers array.
     * <p>
     * Modifiers are thoroughly explained in Member class.
     *
     * @param modifier to be removed
     * @see Modifier
     */
    public void removeModifier(int modifier) {
        modifiers &= ~modifier;
    }

    /**
     * Returns the visibility modifier of this ClassDiagramComponent
     *
     * @return visibility of this ClassDiagramComponent
     * @see Visibility
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility modifier of this ClassDiagramComponent
     *
     * @param visibility to be set
     * @see Visibility
     */
    public void setVisibility(Visibility visibility) {
        Visibility oldVisibility = this.visibility;
        this.visibility = visibility;
        pcs.firePropertyChange("visibility", oldVisibility, visibility);
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

    @Override
    public String toString() {
        return getSignature();
    }

}
