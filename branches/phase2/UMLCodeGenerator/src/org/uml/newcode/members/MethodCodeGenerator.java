package org.uml.newcode.members;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.MethodDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclaratorId;
import japa.parser.ast.expr.Expression;
import japa.parser.ast.expr.ObjectCreationExpr;
import japa.parser.ast.expr.StringLiteralExpr;
import japa.parser.ast.stmt.BlockStmt;
import japa.parser.ast.stmt.Statement;
import japa.parser.ast.stmt.ThrowStmt;
import japa.parser.ast.type.ClassOrInterfaceType;
import japa.parser.ast.type.VoidType;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;
import org.uml.newcode.CodeGeneratorUtils;
import org.uml.newcode.renaming.MemberRenameTable;

/**
 *
 * @author Boris
 */
public class MethodCodeGenerator {

    public static void createMethods(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        for (Method method : component.getMethods()) {
            // add rename listener if it doesn't exist
            if (!method.listenerTypeExists(MemberRenameTable.class)) {
                method.addPropertyChangeListener(new MemberRenameTable());
            }
            // create and add method declaration
            MethodDeclaration declaration = createMethodDeclaration(method, false);
            members.add(declaration);
        }

//        for (RelationBase relation : CodeGeneratorUtils.getRelevantRelations(component)) {
//            if (relation instanceof ImplementsRelation) {
//                InterfaceComponent target = (InterfaceComponent) relation.getTarget();
//                for (Method method : target.getMethods()) {
//                    MethodDeclaration declaration = createMethodDeclaration(method);
//                    // if it has been overridden
//                    if (!members.contains(declaration)) {
//                        members.add(declaration);
//                    }
//                }
//            }
//        }
    }

    public static void createMethods(InterfaceComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        for (Method method : component.getMethods()) {
            // add rename listener if it doesn't exist
            if (!method.listenerTypeExists(MemberRenameTable.class)) {
                method.addPropertyChangeListener(new MemberRenameTable());
            }
            // create and add method declaration
            MethodDeclaration declaration = createMethodDeclaration(method, true);
            members.add(declaration);
        }
    }

    public static void updateMethods(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();
        HashMap<String, String> oldToNew = MemberRenameTable.members.get(component.getSignature());
        // generate or update all direct methods
        for (Method method : component.getMethods()) {
            // add rename listener if it doesn't exist
            if (!method.listenerTypeExists(MemberRenameTable.class)) {
                method.addPropertyChangeListener(new MemberRenameTable());
            }
            // if the method has been renamed update or generate
            if (oldToNew.containsKey(method.getSignature())) {
                String oldSignature = oldToNew.get(method.getSignature());
                boolean found = false;
                for (BodyDeclaration member : members) {
                    if (member instanceof MethodDeclaration) {
                        MethodDeclaration declaration = (MethodDeclaration) member;
                        if (oldSignature.equals(getMethodDeclarationSignature(declaration))) {
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
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    MethodDeclaration declaration = createMethodDeclaration(method, false);
                    members.add(declaration);
                }
            } else {
                // if it has not been renamed, confirm it exists
                MethodDeclaration declaration = findExistingDeclaration(members, method);
                // if it doesn't exits, create and add it
                if (declaration == null) {
                    declaration = createMethodDeclaration(method, false);
                    members.add(declaration);
                }
            }
        }
    }

    public static void updateMethods(InterfaceComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();
        HashMap<String, String> oldToNew = MemberRenameTable.members.get(component.getSignature());
        // generate or update all direct methods
        for (Method method : component.getMethods()) {
            // add rename listener if it doesn't exist
            if (!method.listenerTypeExists(MemberRenameTable.class)) {
                method.addPropertyChangeListener(new MemberRenameTable());
            }
            // if the method has been renamed update or generate
            if (oldToNew.containsKey(method.getSignature())) {
                String oldSignature = oldToNew.get(method.getSignature());
                boolean found = false;
                for (BodyDeclaration member : members) {
                    if (member instanceof MethodDeclaration) {
                        MethodDeclaration declaration = (MethodDeclaration) member;
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
                if (!found) {
                    MethodDeclaration declaration = createMethodDeclaration(method, true);
                    members.add(declaration);
                }
            } else {
                // if it has not been renamed, confirm it exists
                MethodDeclaration declaration = findExistingDeclaration(members, method);
                // if it doesn't exits, create and add it
                if (declaration == null) {
                    declaration = createMethodDeclaration(method, true);
                    members.add(declaration);
                }
            }
        }
    }

    private static MethodDeclaration createMethodDeclaration(Method method, boolean forInterface) {
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
        if (!forInterface && !method.isAbstract()) {
            BlockStmt body = new BlockStmt();
            if (!(declaration.getType() instanceof VoidType)) {
                List<Statement> statements = new LinkedList<>();
//                if (!(declaration.getType() instanceof PrimitiveType)) {
//                    statements.add(new ReturnStmt(new NullLiteralExpr()));
//                } else {
//                    List<VariableDeclarator> declarations = new LinkedList<>();
//                    declarations.add(new VariableDeclarator(new VariableDeclaratorId("ret")));                            
//                    VariableDeclarationExpr returnVariable = new VariableDeclarationExpr(declaration.getType(), declarations);
//                    statements.add(new ExpressionStmt(returnVariable));
//                    statements.add(new ReturnStmt(new NameExpr("ret")));
//                }
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

    private static String getMethodDeclarationSignature(MethodDeclaration declaration) {
        StringBuilder result = new StringBuilder();
//        if (type != null)
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

    private static MethodDeclaration findExistingDeclaration(List<BodyDeclaration> declarations, Method method) {
        for (BodyDeclaration declaration : declarations) {
            if (declaration instanceof MethodDeclaration && method.getSignature().equals(getMethodDeclarationSignature((MethodDeclaration) declaration))) {
                return (MethodDeclaration) declaration;
            }
        }
        return null;
    }
}
