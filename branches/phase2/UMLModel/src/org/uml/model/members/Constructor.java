package org.uml.model.members;

import org.uml.model.components.ClassComponent;
import org.uml.model.components.EnumComponent;

/**
 * Constructors from UML Class diagrams. Used to represent class constructors
 * (both with and without parameters).
 *
 * @author zoran
 * @see MemberBase
 * @see Literal
 * @see Field
 * @see MethodBase
 * @see ClassComponent
 * @see EnumComponent
 */
public class Constructor extends MethodBase {

    /**
     * Constructor with parameter for setting constructor's name.
     * Name is the type for the constructor, so the type is set to null.
     * <p>
     * Calls super constructor.
     *
     * @param name of the constructor
     */
    public Constructor(String name) {
        super(name, null);
    }

    /**
     * Always returns false. Cannot add any non-access modifier to constructor.
     * @param modifier
     * @return 
     */
    @Override
    public boolean allowedToAddModifier(int modifier) {
        return false;
    }

    @Override
    public String getLabelText(boolean isSimpleTypeNames) {
        StringBuilder result = new StringBuilder();
        if (isSimpleTypeNames) result.append(getSimpleTypeSignature());
        else result.append(getSignature());
        return result.toString();
    }
}
