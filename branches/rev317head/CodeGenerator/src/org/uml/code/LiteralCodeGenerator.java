/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import java.util.HashMap;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.Literal;

/**
 *
 * @author stefanpetrovic
 */
class LiteralCodeGenerator implements CodeGenerator{

    private HashMap<String, Literal> literals;

    public LiteralCodeGenerator(HashMap<String, Literal> literals) {
        this.literals = literals;
    }
    
    @Override
    public String generateCode() {
        String code = "";
        for (Literal l : literals.values()) {
            code += l.getName() + ", ";
        }
        if (!code.equals("")) {
            code = code.substring(0, code.length()-2);
        }
        return code;
    }

    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
