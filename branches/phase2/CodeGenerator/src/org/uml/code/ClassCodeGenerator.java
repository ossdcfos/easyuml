package org.uml.code;

import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.PackageComponent;

/**
 *
 * @author zoran
 */
public class ClassCodeGenerator implements CodeGenerator {

    ClassComponent classComponent;

    public ClassCodeGenerator() {
    }

    public void setClassComponent(ClassComponent classComponent) {
        this.classComponent = classComponent;
    }

    @Override
    public String generateCode() {
        String code = "";
        PackageComponent pc= classComponent.getParentPackage();
        if (pc != null && !pc.getName().equals("")) {
            String pack = pc.getName();
            code += "package " + pack + "; \n";
        }
        String abstractModifierStr = "";
        if (classComponent.isIsAbstract()) {
            abstractModifierStr = "abstract";
        }
        String header = "public " + abstractModifierStr + " class " + classComponent.getName() + " { ";
        ConstructorCodeGenerator ccg = new ConstructorCodeGenerator(classComponent.getConstructors());
        String constructors = ccg.generateCode();
        FieldCodeGenerator fcg = new FieldCodeGenerator(classComponent.getFields());
        String fields = fcg.generateCode();
        MethodCodeGenerator mcg = new MethodCodeGenerator(classComponent.getMethods());
        String methods = mcg.generateCode();
        String end = "\n }";
        //classComponent.get

        code += header + "\n";
        code += constructors + "\n";
        code += fields + "\n";
        code += methods + "\n";
        code += end;
        return code;
    }

    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
        classComponent = (ClassComponent) component;
    }
}
