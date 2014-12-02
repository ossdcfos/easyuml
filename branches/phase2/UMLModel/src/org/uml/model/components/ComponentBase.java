package org.uml.model.components;

import org.uml.model.members.*;
import java.awt.*;
import java.beans.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;
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
    
    private Point position; // this should be removed in future
    private Rectangle bounds;

    private Visibility visibility;
    private PackageComponent parentPackage;
    private transient List<PropertyChangeListener> listeners = Collections.synchronizedList(new LinkedList());

    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        listeners.add(pcl);
    }

    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        listeners.remove(pcl);
    }

    protected void fire(String propertyName, Object old, Object nue) {
        for (PropertyChangeListener pcl : listeners) {
            pcl.propertyChange(new PropertyChangeEvent(this, propertyName, old, nue));
        }
    }

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
        visibility = Visibility.PUBLIC;
    }

    /**
     * Adds a member of ClassDiagramComponent to the members collection. Similar
     * implementation as in ClassDiagram for addComponent and addRelation.
     *
     * @param member to be added to members collection
     * @see ClassDiagram#addComponent(org.uml.model.ClassDiagramComponent)
     * @see ClassDiagram#addRelation(org.uml.model.ClassDiagramComponent)
     * @see MemberBase
     */
    public void addMember(MemberBase member) {
        // TODO throw or inform?
        if (nameExists(member.toString())) {
            //member.setName(member.getName() + memberCounter);
            //JOptionPane.showMessageDialog(null, "You have entered name that already exists, please enter new one.");
            throw new RuntimeException("Error while entering member name: name already exists. Member name: " + member.getName());
        }
//        members.put(member.toString(), member);
        components.put(member.getName(), member);
    }

    public abstract void removeMemberFromContainer(MemberBase member);

    /**
     * Removes a member from ClassDiagramComponent's collection of members and
     * replaces it with the same member (but with new name).
     *
     * @param member that will be renamed
     * @param oldName old name of that component
     * @see MemberBase
     */
    public void notifyMemberNameChanged(MemberBase member, String oldName) {
        removeComponent(oldName);
        addMember(member);
    }

    /**
     * Returns members that this ClassDiagramComponent has
     *
     * @return all members of this ClassDiagramComponent
     */
    public HashMap<String, MemberBase> getMembers() {
        return new HashMap(components);
    }

    // used from Property sheet
    public void changeName(String newName) {
//        if (parentDiagram.nameExists(newName)) {
//            throw new RuntimeException("Name of component " + newName + " already exists!");
//        } else {
            String oldName = name;
            name = newName;
            parentDiagram.notifyComponentNameChanged(this, oldName);
            fire("name", oldName, newName);
//        }
        //WidgetAction editor = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
        //ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(getNameLabel());
//          JOptionPane.showMessageDialog(null, "Greska, ime vec postoji.");
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

    /**
     * Returns package that contains this ClassDiagramComponent
     *
     * @return PackageComponent of this ClassDiagramComponent
     * @see PackageComponent
     */
    public PackageComponent getParentPackage() {
        return parentPackage;
    }

    /**
     * Sets the package that contains this ClassDiagramComponent
     *
     * @param parentPackage
     */
    public void setParentPackage(PackageComponent parentPackage) {
        this.parentPackage = parentPackage;
        //   parentPackage.addMember(this);
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
        this.visibility = visibility;
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setBounds(Rectangle bounds) {
        this.bounds = bounds;
    }

}
