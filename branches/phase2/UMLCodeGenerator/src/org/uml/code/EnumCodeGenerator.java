package org.uml.code;

import org.uml.model.components.ComponentBase;
import org.uml.model.components.EnumComponent;

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
        String parentPackage = enumComponent.getParentPackage();
        if (!parentPackage.equals("")) {
            code += "package " + parentPackage + ";\n\n";
        }
        String header = "public enum {\n";
        LiteralCodeGenerator lcg = new LiteralCodeGenerator(enumComponent.getLiterals());
        String literals = lcg.generateCode();
        String end = "\n }";

        code += header + "\n";
        if (!literals.equals("")) code += literals + ";\n";
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
