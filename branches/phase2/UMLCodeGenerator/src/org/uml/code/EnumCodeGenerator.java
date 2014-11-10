/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import org.uml.model.ComponentBase;
import org.uml.model.EnumComponent;
import org.uml.model.PackageComponent;

/**
 * Enumeration's code generating class. Implements all necessary methods
 * that are used in order to generate code from EnumComponents. 
 * 
 * @author zoran
 */
class EnumCodeGenerator implements CodeGenerator {

    EnumComponent enumComponent;

    /**
     * Parameterless constructor that does not instantiate enumComponent
     * field.
     */
    public EnumCodeGenerator() {
    }

    /**
     * Generates code for previously set enumComponent field. Along with
     * standard Enumeration definitions and descriptions, fields, methods,
     * literals and constructors are also appended.
     *
     * @return code of the generated enum
     * @see EnumComponent
     */
    @Override
    public String generateCode() {
        String code = "";
        PackageComponent pc = enumComponent.getParentPackage();
        if (pc != null && !pc.getName().equals("")) {
            String pack = pc.getName();
            code += "package " + pack + "; \n";
        }
        String abstractModifierStr = "";
        String header = "public enum {\n";
        LiteralCodeGenerator lcg = new LiteralCodeGenerator(enumComponent.getLiterals());
        String literals = lcg.generateCode();
        ConstructorCodeGenerator ccg = new ConstructorCodeGenerator(enumComponent.getConstructors());
        String constructors = ccg.generateCode();
        FieldCodeGenerator fcg = new FieldCodeGenerator(enumComponent.getFields());
        String fields = fcg.generateCode();
        MethodCodeGenerator mcg = new MethodCodeGenerator(enumComponent.getMethods());
        String methods = mcg.generateCode();
        String end = "\n }";
        //classComponent.get

        code += header + "\n";
        code += literals + ";\n";
        code += constructors + "\n";
        code += fields + "\n";
        code += methods + "\n";
        code += end;
        return code;
    }

    /**
     * EnumComponent field is casted out of given component and this object's
     * enumComponent field is set to the value.
     *
     * @param component used for code generating
     */
    @Override
    public void setComponent(ComponentBase component) {
        enumComponent = (EnumComponent) component;
    }
}
