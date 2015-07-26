package org.uml.newcode.members;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclaratorId;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.VoidType;
import java.util.LinkedList;
import java.util.List;
import org.uml.filetype.cdg.renaming.MyMembersRenameTable;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;
import org.uml.newcode.CodeGeneratorUtils;

/**
 *
 * @author Boris Perović
 */
public class MethodCodeGenerator {

    public static void createMethods(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        for (Method method : component.getMethods()) {
            // create and add method declaration
            MethodDeclaration declaration = createMethodDeclaration(method);
            members.add(declaration);
        }

        // Do not generate methods for implemented or extended interfaces,
        // let NetBeans show error and take care of that
    }

    public static void createMethods(InterfaceComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        for (Method method : component.getMethods()) {
            // create and add method declaration
            MethodDeclaration declaration = createInterfaceMethodDeclaration(method);
            members.add(declaration);
        }
    }

    public static void updateMethods(ClassComponent component, MyMembersRenameTable renames, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();
        // Generate or update all direct methods
        for (Method method : component.getMethods()) {
            MethodDeclaration existingDeclaration = findExistingDeclaration(members, method);
            if (existingDeclaration == null) { // If there is not existing declaration in the class body
                if (renames.contains(method)) { // If the method has been renamed
                    // Find the old method declaration
                    String oldSignature = renames.getOriginalSignature(method);
                    boolean found = false;
                    for (BodyDeclaration member : members) {
                        if (member instanceof MethodDeclaration) {
                            MethodDeclaration declaration = (MethodDeclaration) member;
                            if (oldSignature.equals(getMethodDeclarationSignature(declaration))) {
                                // Update the old method declaration
                                declaration.setName(method.getName());
                                declaration.setType(CodeGeneratorUtils.parseType(method.getType()));
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
                                // Finish updating the old method declaration
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) { // If the old method declaration has not been found and updated, create it and add it
                        MethodDeclaration declaration = createMethodDeclaration(method);
                        members.add(declaration);
                    }
                } else { // If the method has not been renamed, there is nothing to update, so create and add it
                    MethodDeclaration declaration = createMethodDeclaration(method);
                    members.add(declaration);
                }
            }
        }
    }

    public static void updateMethods(InterfaceComponent component, MyMembersRenameTable renames, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();
        // Generate or update all direct methods
        for (Method method : component.getMethods()) {
            // If the method has been renamed update or generate
            if (renames.contains(method)) {
                // Find the original parsed method
                String oldSignature = renames.getOriginalSignature(method);
                boolean found = false;
                for (BodyDeclaration member : members) {
                    if (member instanceof MethodDeclaration) {
                        MethodDeclaration declaration = (MethodDeclaration) member;
                        // Update the original parsed method
                        if (oldSignature.equals(getMethodDeclarationSignature(declaration))) {
                            declaration.setName(oldSignature);
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
                            found = true;
                            break;
                        }
                    }
                }
                // If the method has not been updated, generate it
                if (!found) {
                    MethodDeclaration declaration = createInterfaceMethodDeclaration(method);
                    members.add(declaration);
                }
            } else {
                // If it has not been renamed, confirm it exists
                MethodDeclaration declaration = findExistingDeclaration(members, method);
                // If it doesn't exits, create and add it
                if (declaration == null) {
                    declaration = createInterfaceMethodDeclaration(method);
                    members.add(declaration);
                }
            }
        }
    }

    private static MethodDeclaration createMethodDeclaration(Method method) {
        MethodDeclaration declaration = new MethodDeclaration();
        // Set name
        declaration.setName(method.getName());
        // Set return type
        String returnType = method.getType();
        declaration.setType(CodeGeneratorUtils.parseType(returnType));
        // Set visibility
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
        // Set modifiers
        if (method.isStatic()) {
            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
        }
        if (method.isFinal()) {
            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.FINAL));
        }
        if (method.isAbstract()) {
            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.ABSTRACT));
        }
        if (method.isSynchronized()) {
            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.SYNCHRONIZED));
        }

        // Set arguments
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
        // Fill method body
        if (!method.isAbstract()) {
            BlockStmt body = new BlockStmt();
            // If the return type is not void, set the body to UnsupportedOperationException
            // Otherwise, leave body empty
            if (!(declaration.getType() instanceof VoidType)) {
                List<Statement> statements = new LinkedList<>();
                ObjectCreationExpr exception = new ObjectCreationExpr();
                exception.setType(new ClassOrInterfaceType("UnsupportedOperationException"));
                List<Expression> arguments = new LinkedList<>();
                arguments.add(new StringLiteralExpr("Not supported yet."));
                exception.setArgs(arguments);
                statements.add(new ThrowStmt(exception));
                body.setStmts(statements);
            }
            declaration.setBody(body);
        }
        return declaration;
    }

    private static MethodDeclaration findExistingDeclaration(List<BodyDeclaration> declarations, Method method) {
        for (BodyDeclaration declaration : declarations) {
            if (declaration instanceof MethodDeclaration && method.getSignature().equals(getMethodDeclarationSignature((MethodDeclaration) declaration))) {
                return (MethodDeclaration) declaration;
            }
        }
        return null;
    }

    private static MethodDeclaration createInterfaceMethodDeclaration(Method method) {
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
        }
        if (method.isStatic()) {
            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
        }

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
        return declaration;
    }

    private static String getMethodDeclarationSignature(MethodDeclaration declaration) {
        StringBuilder result = new StringBuilder();
        result.append(declaration.getType().toString()).append(" ");
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
