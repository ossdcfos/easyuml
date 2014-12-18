package org.uml.model.members;

import java.util.HashMap;
import java.util.LinkedHashSet;
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
        super(name, null);
    }

    @Override
    public String deriveNewSignatureFromName(String newName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deriveNewSignatureFromType(String newType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
