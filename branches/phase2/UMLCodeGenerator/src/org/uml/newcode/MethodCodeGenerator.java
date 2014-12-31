package org.uml.newcode;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.stmt.BlockStmt;
import java.util.LinkedList;
import java.util.List;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.RelationBase;

/**
 *
 * @author Boris
 */
public class MethodCodeGenerator {

    public static void createMethods(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        for (Method method : component.getMethods()) {
            MethodDeclaration declaration = new MethodDeclaration();
            declaration.setName(method.getName());
            String returnType = method.getType();
            declaration.setType(CodeGeneratorUtils.parseType(returnType));
            switch (method.getVisibility()) {
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
            if (method.isStatic()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
            if (method.isFinal()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.FINAL));
            if (method.isAbstract()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.ABSTRACT));
            if (method.isSynchronized()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.SYNCHRONIZED));

            if (!method.getArguments().isEmpty()) {
                List<Parameter> parameters = new LinkedList<>();
                for (MethodArgument argument : method.getArguments()) {
                    Parameter parameter = new Parameter();
                    String type = argument.getType();
                    parameter.setType(CodeGeneratorUtils.parseType(type));
                    parameter.setId(new VariableDeclaratorId(argument.getName()));
                    parameters.add(parameter);
                }
                declaration.setParameters(parameters);
            }
            declaration.setBody(new BlockStmt());
            members.add(declaration);
        }

        for (RelationBase relation : CodeGeneratorUtils.getRelevantRelations(component)) {
            if (relation instanceof ImplementsRelation) {
                InterfaceComponent target = (InterfaceComponent) relation.getTarget();
                for (Method method : target.getMethods()) {
                    MethodDeclaration declaration = new MethodDeclaration();
                    declaration.setName(method.getName());
                    String returnType = method.getType();
                    declaration.setType(CodeGeneratorUtils.parseType(returnType));
                    switch (method.getVisibility()) {
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
                    if (method.isStatic()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
                    if (method.isFinal()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.FINAL));
                    if (method.isAbstract()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.ABSTRACT));
                    if (method.isSynchronized()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.SYNCHRONIZED));

                    if (!method.getArguments().isEmpty()) {
                        List<Parameter> parameters = new LinkedList<>();
                        for (MethodArgument argument : method.getArguments()) {
                            Parameter parameter = new Parameter();
                            String type = argument.getType();
                            parameter.setType(CodeGeneratorUtils.parseType(type));
                            parameter.setId(new VariableDeclaratorId(argument.getName()));
                            parameters.add(parameter);
                        }
                        declaration.setParameters(parameters);
                    }
                    declaration.setBody(new BlockStmt());
                    if (!members.contains(declaration)) {
                        members.add(declaration);
                    }
                }
            }
        }
    }

    static void updateMethods(CompilationUnit cu) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
