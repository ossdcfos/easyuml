package org.uml.model.members;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Modifier;
import javax.swing.JOptionPane;
import org.uml.model.IHasSignature;
import org.uml.model.INameable;
import org.uml.model.Visibility;
import org.uml.model.components.ComponentBase;

/**
 * Represents a member of a component. Can be Field, Constructor, Method or Literal.
 *
 * @author zoran
 * @see Field
 * @see Constructor
 * @see Method
 * @see Literal
 * @see ComponentBase
 */
public abstract class MemberBase implements INameable, IHasSignature {

    /**
     * Declaring component which contains this member. It is set when the member is added to the component,
     * but could be moved as a constructor parameter to be obligatory. TODO
     */
    private transient ComponentBase declaringComponent;

    /**
     * Name of the member.
     */
    protected String name;

    /**
     * Type of the container.
     */
    protected String type;

    /**
     * Visibility of the member. Can be public, private, package, protected.
     */
    protected Visibility visibility;

    /**
     * Modifiers integer which stores non-access modifiers as bits,
     * as defined in java.lang.reflect.Modifier.
     *
     * @see java.lang.reflect.Modifier
     */
    protected int modifiers;

    /**
     * Property change support and related methods.
     */
    protected transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    public void addPropertyChangeListener(PropertyChangeListener pcl) {
        pcs.addPropertyChangeListener(pcl);
    }
    public void removePropertyChangeListener(PropertyChangeListener pcl) {
        pcs.removePropertyChangeListener(pcl);
    }
    
    /**
     * Default constructor. Sets the name of a Member.
     *
     * @param name of a member
     */
    protected MemberBase(String name) {
        this.name = name;
    }

    /**
     * Return the name of the member.
     *
     * @return name of the member
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Set the member name to newName. Fires "name" property change event.
     *
     * @param newName of the member
     */
    @Override
    public void setName(String newName) {
        String oldName = getName();
        name = newName;
        pcs.firePropertyChange("name", oldName, newName);
    }

    /**
     * Return the type of the member.
     *
     * @return type of the member
     */
    public String getType() {
        return type;
    }

    /**
     * Set the member type to newType. Fires "type" property change event.
     *
     * @param newType of the member
     */
    public void setType(String newType) {
        String oldType = this.type;
        this.type = newType;
        pcs.firePropertyChange("type", oldType, newType);
    }

    /**
     * Adds a modifier bit of java Modifier enum into the modifiers array.
     *
     * @param modifier to be added to the modifiers array
     * @see java.lang.reflect.Modifier
     */
    public void addModifier(int modifier) //throws Exception 
    {
        if (allowedToAddModifier(modifier)) {
            modifiers |= modifier;
        } else {
            // TODO should not be here, model without GUI
            JOptionPane.showMessageDialog(null, "Illegal modifier combination \"" + Modifier.toString(modifiers | modifier) + "\"!", "Warning", JOptionPane.WARNING_MESSAGE);
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
     * Returns true if the modifier can be added to the member (does not conflict with other modifiers).
     * @param modifier
     * @return 
     */
    public abstract boolean allowedToAddModifier(int modifier);

    /**
     * Returns the visibility of this member.
     *
     * @return visibility of this member
     * @see Visibility
     */
    public Visibility getVisibility() {
        return visibility;
    }

    /**
     * Sets the visibility of this member. Fires "visibility" property change event.
     *
     * @param visibility to be set
     * @see Visibility
     */
    public void setVisibility(Visibility visibility) {
        Visibility oldValue = this.visibility;
        this.visibility = visibility;
        pcs.firePropertyChange("visibility", oldValue, this.visibility);
    }

    /**
     * Returns the declaringComponent of this member.
     * Declaring component is a ComponentBase object that contains this member.
     *
     * @return ComponentBase containing this member
     * @see ComponentBase
     */
    public ComponentBase getDeclaringComponent() {
        return declaringComponent;
    }

    /**
     * Sets the declaring component of this member.
     * Declaring component is a ComponentBase object that contains this member.
     *
     * @param declaringComponent
     * @see ComponentBase
     */
    public void setDeclaringComponent(ComponentBase declaringComponent) {
        this.declaringComponent = declaringComponent;
    }

    /**
     * Returns a signature with simple type names and without modifiers.
     * @return simple signature of this member
     */
    protected abstract String getSimpleTypeSignature();

    /**
     * Returns the text which is shown to the user in the GUI.
     * @param isSimpleTypeNames - if simple type names are used or not
     * @return 
     */
    public abstract String getLabelText(boolean isSimpleTypeNames);

    /**
     * Abstract method which should make a signature with a potential new name, 
     * so that we can check if the member with the same signature already exists.
     *
     * @param newName to make a signature from
     * @return
     */
    public abstract String deriveSignatureFromName(String newName);

    /**
  
     * Abstract method which should make a signature with a potential new type, 
     * so that we can check if the member with the same signature already exists.
     * 
     * @param newType to make a signature from
     * @return
     */
    public abstract String deriveSignatureFromType(String newType);
    
    protected String getSimpleType(String curType){
        return curType.contains(".") ? curType.substring(curType.lastIndexOf(".") + 1) : curType;
    }
}
