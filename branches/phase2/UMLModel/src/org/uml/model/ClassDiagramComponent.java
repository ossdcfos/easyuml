package org.uml.model;

import java.awt.Point;
import java.io.Serializable;
import java.util.HashMap;
import javax.swing.JOptionPane;

/**
 * Base class for all UML class diagram components (classes, interfaces or enums)
 * that can be added to class diagram. Note that relations are not represented
 * with this class as well; they are represented with RelationComponent.
 *
 * @author zoran
 * @see ClassComponent
 * @see RelationComponent
 */
public class ClassDiagramComponent implements Serializable {

    private String name;     // class, interface or enum name              
    protected HashMap<String, Member> members; // index of all fields, methods and constructors
    protected ClassDiagram parentDiagram;
    private int memberCounter = 0;
    protected Visibility visibility;
    private Point position; // this should be removed in future
    protected PackageComponent parentPackage;

    /**
     * Default constructor. Initializes only members of the
     * ClassDiagramComponent. Members can be fields, methods, constructors and
     * literals.
     *
     * @see Field
     * @see Method
     * @see Constructor
     * @see Literal
     */
    public ClassDiagramComponent() {
        members = new HashMap<String, Member>();
    }

    /**
     * Constructor that initializes only name of ClassDiagramComponent.
     *
     * @param name of component
     */
    public ClassDiagramComponent(String name) {
        this.name = name;
    }

    /**
     * Gets the name of ClassDiagramComponent.
     *
     * @return name of this ClassDiagramComponent
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of ClassDiagramComponent.
     *
     * @param name to be set to ClassDiagramComponent
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Adds a member of ClassDiagramComponent to the members collection. Similar
     * implementation as in ClassDiagram for addComponent and addRelation.
     *
     * @param member to be added to members collection
     * @see ClassDiagram#addComponent(org.uml.model.ClassDiagramComponent)
     * @see ClassDiagram#addRelation(org.uml.model.ClassDiagramComponent)
     * @see Member
     */
    public void addMember(Member member){
        if (nameExists(member.getName())) {
//            member.setName(member.getName() + memberCounter);
            //JOptionPane.showMessageDialog(null, "You have entered name that already exists, please enter new one.");
            throw new RuntimeException("Error while entering member name: name already exists.");
        }
        memberCounter++;
        members.put(member.getName(), member);
    }

    /**
     * Removes a member of ClassDiagramComponent from the collection of
     * components.
     *
     * @param name of component that will be removed
     */
    public void removeMember(String name) {
        members.remove(name);
    }

    /**
     * Gets parentDiagram. Parent diagram is a ClassDiagram object that contains
     * this ClassDiagramComponent.
     *
     * @return ClassDiagram containing this ClassDiagramComponent
     * @see ClassDiagram
     */
    public ClassDiagram getParentDiagram() {
        return parentDiagram;
    }

    /**
     * Sets parentDiagram. Parent diagram is a ClassDiagram object that contains
     * this ClassDiagramComponent.
     *
     * @see ClassDiagram
     */
    public void setParentDiagram(ClassDiagram parentDiagram) {
        this.parentDiagram = parentDiagram;
    }

    /**
     * Checks if a member of ClassDiagramComponent already exists in the
     * collection of components.
     *
     * @param name of ClassDiagramComponent that is to be checked
     * @return true if already exists, false if it doesn't
     */
    public boolean nameExists(String name) {
        return members.containsKey(name);
    }

    /**
     * Removes a member from ClassDiagramComponent's collection of members and
     * replaces it with the same member (but with new name).
     *
     * @param member that will be renamed
     * @param oldName old name of that component
     * @see Member
     */
    public void notifyMemberNameChanged(Member member, String oldName){
        addMember(member);
        members.remove(oldName);
    }

    public HashMap<String, Member> getMembers() {
        return members;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public PackageComponent getParentPackage() {
        return parentPackage;
    }

    public void setParentPackage(PackageComponent parentPackage) {
        this.parentPackage = parentPackage;
        //   parentPackage.addMember(this);
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }
}
