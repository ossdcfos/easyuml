/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;
import org.uml.model.PackageComponent;

/**
 *
 * @author zoran
 */
class EnumCodeGenerator implements CodeGenerator {

    EnumComponent enumComponent;

    public EnumCodeGenerator() {
    }

    @Override
    public String generateCode() {
        String code = "";
        PackageComponent pc= enumComponent.getParentPackage();
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

    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
        enumComponent = (EnumComponent) component;
    }
}
