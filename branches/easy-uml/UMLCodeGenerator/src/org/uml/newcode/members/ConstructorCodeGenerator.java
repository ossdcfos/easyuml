package org.uml.newcode.members;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.stmt.BlockStmt;
import java.util.LinkedList;
import java.util.List;
import org.uml.filetype.cdg.renaming.MyMembersRenameTable;
import org.uml.model.components.ClassComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.MethodArgument;
import org.uml.newcode.CodeGeneratorUtils;

/**
 *
 * @author Boris Perović
 */
public class ConstructorCodeGenerator {

    public static void createConstructors(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        for (Constructor constructor : component.getConstructors()) {
            // create and add constructor declaration
            ConstructorDeclaration declaration = createConstructorDeclaration(constructor);
            members.add(declaration);
        }
    }

    public static void updateConstructors(ClassComponent component, MyMembersRenameTable renames, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();
        // Generate or update all direct constructors
        for (Constructor constructor : component.getConstructors()) {
            ConstructorDeclaration existingDeclaration = findExistingDeclaration(members, constructor);
            if (existingDeclaration == null) { // If there is not existing declaration in the class body
                if (renames.contains(constructor)) { // If the constructor has been renamed
                    // Find the old constructor declaration
                    String oldSignature = renames.getOriginalSignature(constructor);
                    boolean found = false;
                    for (BodyDeclaration member : members) {
                        if (member instanceof ConstructorDeclaration) {
                            ConstructorDeclaration declaration = (ConstructorDeclaration) member;
                            if (oldSignature.equals(getConstructorDeclarationSignature(declaration))) {
                                // Update the old constructor declaration
                                declaration.setName(constructor.getName());
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
                                // Finish updating the old constructor declaration
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) { // If the old constructor declaration has not been found and updated, create it and add it
                        ConstructorDeclaration declaration = createConstructorDeclaration(constructor);
                        members.add(declaration);
                    }
                } else { // If the constructor has not been renamed, there is nothing to update, so create and add it
                    ConstructorDeclaration declaration = createConstructorDeclaration(constructor);
                    members.add(declaration);
                }
            }
        }
    }

    private static ConstructorDeclaration findExistingDeclaration(List<BodyDeclaration> declarations, Constructor constructor) {
        for (BodyDeclaration declaration : declarations) {
            if (declaration instanceof ConstructorDeclaration && constructor.getSignature().equals(getConstructorDeclarationSignature((ConstructorDeclaration) declaration))) {
                return (ConstructorDeclaration) declaration;
            }
        }
        return null;
    }

    private static ConstructorDeclaration createConstructorDeclaration(Constructor constructor) {
        ConstructorDeclaration declaration = new ConstructorDeclaration();
        declaration.setName(constructor.getDeclaringComponent().getName());
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
        declaration.setBlock(new BlockStmt());
        return declaration;
    }

    private static String getConstructorDeclarationSignature(ConstructorDeclaration declaration) {
        StringBuilder result = new StringBuilder();
        result.append(declaration.getName()).append("(");
        String args = "";
        if (declaration.getParameters() != null) {
            for (Parameter parameter : declaration.getParameters()) {
                args += parameter.getType() + " " + parameter.getId().getName() + ", ";
            }
            if (!args.equals("")) {
                args = args.substring(0, args.length() - 2);
            }
        }
        result.append(args).append(")");
        return result.toString();
    }
}
