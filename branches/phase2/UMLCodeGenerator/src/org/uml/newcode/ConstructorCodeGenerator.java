package org.uml.newcode;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclaratorId;
import java.util.LinkedList;
import java.util.List;
import org.uml.model.components.ClassComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.MethodArgument;

/**
 *
 * @author Boris
 */
public class ConstructorCodeGenerator {

    public static void createConstructors(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        for (Constructor constructor : component.getConstructors()) {
            ConstructorDeclaration declaration = new ConstructorDeclaration();
            declaration.setName(component.getName());
            switch (constructor.getVisibility()) {
                case PUBLIC:
                    declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PUBLIC));
                    break;
                case PROTECTED:
                    declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PROTECTED));
                    break;
                case PRIVATE:
                    declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.PRIVATE));
                    break;
            }
            if (!constructor.getArguments().isEmpty()) {
                List<Parameter> parameters = new LinkedList<>();
                for (MethodArgument argument : constructor.getArguments()) {
                    Parameter parameter = new Parameter();
                    String type = argument.getType();
                    parameter.setType(CodeGeneratorUtils.parseType(type));
                    parameter.setId(new VariableDeclaratorId(argument.getName()));
                    parameters.add(parameter);
                }
                declaration.setParameters(parameters);
            }
            members.add(declaration);
        }
    }

    static void updateConstructors(CompilationUnit cu) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
