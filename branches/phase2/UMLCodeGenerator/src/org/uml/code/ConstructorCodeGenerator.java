package org.uml.code;

import java.util.LinkedHashSet;
import org.uml.model.components.ComponentBase;
import org.uml.model.members.Constructor;

/**
 * Constructor's code generating class. It is used to fully generate code for
 * Class constructors.
 *
 * @author Uros
 */
public class ConstructorCodeGenerator implements CodeGenerator {

    LinkedHashSet<Constructor> constructors;

    /**
     * Constructor that sets this object's constructors field to the given
     * value.
     *
     * @param constructors to be set
     */
    public ConstructorCodeGenerator(LinkedHashSet<Constructor> constructors) {
        this.constructors = constructors;
    }

    /**
     * Generates code for previously set Hash map of Constructors. Iterates
     * through it and writes empty constructors.
     *
     * @return code of the generated constructor
     * @see Constructor
     */
    @Override
    public String generateCode() {
        StringBuilder constructorString = new StringBuilder();
        for (Constructor constructor : constructors) {
            constructorString.append(constructor.getFullSignature());
        }
        return constructorString.toString();
    }

    //this method must be implemented because of the CodeGenerator interface, although it is never used
    @Override
    public void setComponent(ComponentBase component) {
    }
}
