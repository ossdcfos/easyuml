/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import java.util.HashMap;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.members.Literal;

/**
 * Enumeration literal's code generating class.
 *
 * @author stefanpetrovic
 */
class LiteralCodeGenerator implements CodeGenerator {

    private HashMap<String, Literal> literals;

    /**
     * A constructor that sets this object's HashMap of literals to the given
     * value.
     *
     * @param literals which code needs to be generated
     */
    public LiteralCodeGenerator(HashMap<String, Literal> literals) {
        this.literals = literals;
    }

    /**
     * Genrates code for this object's HashMap of Enum's literals.
     *
     * @return code of the generated literals
     * @see Literal
     */
    @Override
    public String generateCode() {
        String code = "";
        for (Literal l : literals.values()) {
            code += l.getName() + ", ";
        }
        if (!code.equals("")) {
            code = code.substring(0, code.length() - 2);
        }
        return code;
    }

    //this method must be implemented because of the CodeGenerator interface, although it is never used
    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
