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
     * Constructor with parameter for setting constructor's name. Only sets the
     * name parameter.
     * <p>
     * Calls super constructor.
     *
     * @param name
     * @see MemberBase
     */
    public Constructor(String name) {
        // name is the type for constructor
        super(name, null);
    }

    @Override
    public boolean allowedToAddModifier(int modifier) {
        return false;
    }
    
}
