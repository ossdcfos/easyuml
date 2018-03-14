package org.uml.newcode.members;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.expr.StringLiteralExpr;
import com.github.javaparser.ast.expr.ThisExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.stmt.ThrowStmt;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.VoidType;
import com.github.javaparser.printer.lexicalpreservation.LexicalPreservingPrinter;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import org.uml.filetype.cdg.renaming.MyMembersRenameTable;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.members.Field;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;
import org.uml.newcode.CodeGeneratorUtils;

/**
 *
 * @author Boris Perović
 */
public class MethodCodeGenerator {

    public static void createMethods(ClassComponent component, CompilationUnit cu) {
        NodeList<BodyDeclaration<?>> members = cu.getTypes().get(0).getMembers();

        for (Method method : component.getMethods()) {
            // create and add method declaration
            MethodDeclaration declaration = createMethodDeclaration(method,null);
            members.add(declaration);
        }
        
        // Generate getters 
        for (Field field : component.getRequestedGetters()) {
            MethodDeclaration declaration = createMethodDeclaration(field.createGetter(),createGetterBody(field));
            members.add(declaration);
        }

        // Generate setters 
        for (Field field : component.getRequestedSetters()) {
            MethodDeclaration declaration = createMethodDeclaration(field.createSetter(),createSetterBody(field));
            members.add(declaration);
        }
        
        // Do not generate methods for implemented or extended interfaces,
        // let NetBeans show error and take care of that
    }

    public static void createMethods(InterfaceComponent component, CompilationUnit cu) {
        NodeList<BodyDeclaration<?>> members = cu.getTypes().get(0).getMembers();

        for (Method method : component.getMethods()) {
            // create and add method declaration
            MethodDeclaration declaration = createInterfaceMethodDeclaration(method);
            members.add(declaration);
        }
    }

    public static void updateMethods(ClassComponent component, MyMembersRenameTable renames, CompilationUnit cu) {
        NodeList<BodyDeclaration<?>> members = cu.getTypes().get(0).getMembers();
        // Generate or update all direct methods
        for (Method method : component.getMethods()) {
            MethodDeclaration existingDeclaration = findExistingDeclaration(members, method);
            if (existingDeclaration == null) { // If there is not existing declaration in the class body
                if (renames.contains(method)) { // If the method has been renamed
                    // Find the old method declaration
                    String oldSignature = renames.getOriginalSignature(method);
                    System.out.println(component.getSignature()+"."+method.getSignature()+": renamed from "+oldSignature);
                    boolean found = false;
                    for (BodyDeclaration member : members) {
                        if (member instanceof MethodDeclaration) {
                            MethodDeclaration declaration = (MethodDeclaration) member;
                            if (oldSignature.equals(getMethodDeclarationSignature(declaration))) {
                                // Update the old method declaration
                                declaration.setName(method.getName());
                                declaration.setType(CodeGeneratorUtils.parseType(method.getType()));
                                if (!method.getArguments().isEmpty()) {
                                    NodeList<Parameter> parameters = new NodeList<>();
                                    for (MethodArgument argument : method.getArguments()) {
                                        Parameter parameter = new Parameter();
                                        String type = argument.getType();
                                        parameter.setType(CodeGeneratorUtils.parseType(type));
                                        parameter.setName(argument.getName());
                                        parameters.add(parameter);
                                    }
                                    declaration.setParameters(parameters);
                                }
                                System.out.println("  Update method "+method.getSignature());
                                // Finish updating the old method declaration
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) { // If the old method declaration has not been found and updated, create it and add it
                        MethodDeclaration declaration = createMethodDeclaration(method,null);
                        members.add(declaration);
                        System.out.println("  Add renamed method "+method.getSignature());
                    }
                } else { // If the method has not been renamed, there is nothing to update, so create and add it
                    MethodDeclaration declaration = createMethodDeclaration(method,null);
                    members.add(declaration);
                    System.out.println("  Add method "+method.getSignature());
                }
            }
        }
        // Generate getters 
        for (Field field : component.getRequestedGetters()) {
            Method method = field.createGetter();
            MethodDeclaration existingDeclaration = findExistingDeclaration(members, method);
            if (existingDeclaration != null)
                continue;
            MethodDeclaration declaration = createMethodDeclaration(method,createGetterBody(field));
            members.add(declaration);
            System.out.println("  Add getter "+method.getSignature());
        }

        // Generate setters 
        for (Field field : component.getRequestedSetters()) {
            Method method = field.createSetter();
            MethodDeclaration existingDeclaration = findExistingDeclaration(members, method);
            if (existingDeclaration != null)
                continue;
            MethodDeclaration declaration = createMethodDeclaration(method,createSetterBody(field));
            members.add(declaration);
            System.out.println("  Add setter "+method.getSignature());
        }
        
    }

    public static void updateMethods(InterfaceComponent component, MyMembersRenameTable renames, CompilationUnit cu) {
        NodeList<BodyDeclaration<?>> members = cu.getTypes().get(0).getMembers();
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
                                NodeList<Parameter> parameters = new NodeList<>();
                                for (MethodArgument argument : method.getArguments()) {
                                    Parameter parameter = new Parameter();
                                    String type = argument.getType();
                                    parameter.setType(CodeGeneratorUtils.parseType(type));
                                    parameter.setName(argument.getName());
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

    private static MethodDeclaration createMethodDeclaration(Method method,BlockStmt body) {
        MethodDeclaration declaration = new MethodDeclaration();
        // Set name
        declaration.setName(method.getName());
        // Set return type
        String returnType = method.getType();
        declaration.setType(CodeGeneratorUtils.parseType(returnType));
        // Set visibility
        switch (method.getVisibility()) {
            case PUBLIC:
                declaration.addModifier(Modifier.PUBLIC);
                break;
            case PROTECTED:
                declaration.addModifier(Modifier.PROTECTED);
                break;
            case PRIVATE:
                declaration.addModifier(Modifier.PRIVATE);
                break;
        }
        if (method.isStatic()) {
            declaration.addModifier(Modifier.STATIC);
        }
        if (method.isFinal()) {
            declaration.addModifier(Modifier.FINAL);
        }
        if (method.isAbstract()) {
            declaration.addModifier(Modifier.ABSTRACT);
        }
        if (method.isSynchronized()) {
            declaration.addModifier(Modifier.SYNCHRONIZED);
        }

        // Set arguments
        if (!method.getArguments().isEmpty()) {
            NodeList<Parameter> parameters = new NodeList<>();
            for (MethodArgument argument : method.getArguments()) {
                Parameter parameter = new Parameter();
                String type = argument.getType();
                parameter.setType(CodeGeneratorUtils.parseType(type));
                parameter.setName(argument.getName());
                parameters.add(parameter);
            }
            declaration.setParameters(parameters);
        }
        // Fill method body
        if (!method.isAbstract()) {
            if (body == null) {
                body = new BlockStmt();
                // If the return type is not void, set the body to UnsupportedOperationException
                // Otherwise, leave body empty
                if (!(declaration.getType() instanceof VoidType)) {
                    NodeList<Statement> statements = new NodeList<>();
                    ObjectCreationExpr exception = new ObjectCreationExpr();
                    exception.setType(JavaParser.parseClassOrInterfaceType("UnsupportedOperationException"));
                    NodeList<Expression> arguments = new NodeList<>();
                    arguments.add(new StringLiteralExpr("Not supported yet."));
                    exception.setArguments(arguments);
                    statements.add(new ThrowStmt(exception));
                    body.setStatements(statements);
                }
            }
            declaration.setBody(body);
        }
        else {
            declaration.setBody(null);
        }
        return declaration;
    }

    private static MethodDeclaration findExistingDeclaration(NodeList<BodyDeclaration<?>> declarations, Method method) {
        for (BodyDeclaration declaration : declarations) {
            if (!(declaration instanceof MethodDeclaration))
                continue;
            String methodSignature = method.getSignature();
            String declarationSignature = getMethodDeclarationSignature((MethodDeclaration) declaration);
            if (methodSignature.equals(declarationSignature)) {
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
                declaration.addModifier(Modifier.PUBLIC);
                break;
            case PROTECTED:
                declaration.addModifier(Modifier.PROTECTED);
                break;
        }
        if (method.isStatic()) 
            declaration.addModifier(Modifier.STATIC);

        if (!method.getArguments().isEmpty()) {
            NodeList<Parameter> parameters = new NodeList<>();
            for (MethodArgument argument : method.getArguments()) {
                Parameter parameter = new Parameter();
                String type = argument.getType();
                parameter.setType(CodeGeneratorUtils.parseType(type));
                parameter.setName(argument.getName());
                parameters.add(parameter);
            }
            declaration.setParameters(parameters);
        }
        
        declaration.setBody(null);
        
        return declaration;
    }
    
    private static String getMethodDeclarationSignature(MethodDeclaration declaration) {
        Method method = new Method(declaration.getName().asString(), declaration.getType().asString());
        
        LinkedHashSet<MethodArgument> arguments = method.getArguments();
        for (Parameter parameter : declaration.getParameters()) {
            MethodArgument arg = new MethodArgument(parameter.getType().asString(), parameter.getName().asString());
            arguments.add(arg);
        }

        return method.getSignature();
    }

    public static BlockStmt createGetterBody(Field field) {
        BlockStmt body = new BlockStmt();
        NodeList<Statement> statements = new NodeList<>();
        statements.add(new ReturnStmt(new NameExpr(field.getName())));
        body.setStatements(statements);
        return body;
    }
    
    public static BlockStmt createSetterBody(Field field) {
        BlockStmt body = new BlockStmt();
        NodeList<Statement> statements = new NodeList<>();
        Expression expr = new FieldAccessExpr(new ThisExpr(),field.getName());
        statements.add(new ExpressionStmt(new AssignExpr(expr,new NameExpr(field.getName()),AssignExpr.Operator.ASSIGN)));
        body.setStatements(statements);
        return body;
    }
    
}
