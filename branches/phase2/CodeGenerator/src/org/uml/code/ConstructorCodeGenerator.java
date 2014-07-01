/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import java.util.HashMap;
import java.util.Map;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.Constructor;

/**
 * Constructor's code generating class. It is used to fully generate code for
 * Class constructors.
 *
 * @author Uros
 */
public class ConstructorCodeGenerator implements CodeGenerator {

    HashMap<String, Constructor> constructors;

    /**
     * Constructor that sets this object's constructors field to the given
     * value.
     *
     * @param constructors to be set
     */
    public ConstructorCodeGenerator(HashMap<String, Constructor> constructors) {
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
        String code = "";
        for (Map.Entry<String, Constructor> entry : constructors.entrySet()) {

            String string = entry.getKey();
            Constructor constructor = entry.getValue();
            code += "public " + constructor.getName() + "(";
            if (constructor.getArguments() != null) {
                code += constructor.getArguments();
            }
            code += ") {}";
        }
        return code;
    }

    //this method must be implemented because of the CodeGenerator interface, although it is never used
    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
    }
}
