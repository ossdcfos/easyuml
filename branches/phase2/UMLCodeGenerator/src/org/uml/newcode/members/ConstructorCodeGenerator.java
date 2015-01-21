package org.uml.newcode.members;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.stmt.BlockStmt;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.uml.model.components.ClassComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.MethodArgument;
import org.uml.newcode.CodeGeneratorUtils;
import org.uml.newcode.renaming.MemberRenameTable;

/**
 *
 * @author Boris
 */
public class ConstructorCodeGenerator {

    public static void createConstructors(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        for (Constructor constructor : component.getConstructors()) {
            // add rename listener if it doesn't exist
            if (!constructor.listenerTypeExists(MemberRenameTable.class)) {
                constructor.addPropertyChangeListener(new MemberRenameTable());
            }
            // create and add constructor declaration
            ConstructorDeclaration declaration = createConstructorDeclaration(constructor);
            members.add(declaration);
        }
    }

    public static void updateConstructors(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();
        HashMap<String, String> oldToNew = MemberRenameTable.members.get(component.getSignature());
        // generate or update all direct constructors
        for (Constructor constructor : component.getConstructors()) {
            // add rename listener if it doesn't exist
            if (!constructor.listenerTypeExists(MemberRenameTable.class)) {
                constructor.addPropertyChangeListener(new MemberRenameTable());
            }
            // if the constructor has been renamed update or generate
            if (oldToNew.containsKey(constructor.getSignature())) {
                String oldSignature = oldToNew.get(constructor.getSignature());
                boolean found = false;
                for (BodyDeclaration member : members) {
                    if (member instanceof ConstructorDeclaration) {
                        ConstructorDeclaration declaration = (ConstructorDeclaration) member;
                        if (oldSignature.equals(getConstructorDeclarationSignature(declaration))) {
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
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    ConstructorDeclaration declaration = createConstructorDeclaration(constructor);
                    members.add(declaration);
                }
            } else {
                // if it has not been renamed, confirm it exists
                ConstructorDeclaration declaration = findExistingDeclaration(members, constructor);
                // if it doesn't exits, create and add it
                if (declaration == null) {
                    declaration = createConstructorDeclaration(constructor);
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
}
