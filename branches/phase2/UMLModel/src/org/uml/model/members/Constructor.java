package org.uml.model.members;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Objects;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.EnumComponent;
import org.uml.model.Visibility;

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
        super(name, null, new HashMap<String, MethodArgument>());
    }

    /**
     * Constructor with three parameters used to set name, type of return
     * argument and input arguments(variables) of constructor. Calls the super
     * constructor.
     *
     * @param name of the Constructor
     * @param arguments - collection of arguments
     */
    public Constructor(String name, HashMap<String, MethodArgument> arguments) {
        super(name, null, arguments);
    }

    @Override
    public String deriveNewSignatureWithoutModifiersFromName(String newName) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String deriveNewSignatureWithoutModifiersFromType(String newType) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
