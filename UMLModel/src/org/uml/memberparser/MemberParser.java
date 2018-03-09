package org.uml.memberparser;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParseException;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.AnnotationMemberDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.SimpleName;
import com.github.javaparser.ast.type.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;

/**
 * Class to parse member signatures and fill the members. If a signature is of a
 * bad format, exception is thrown.
 *
 * @author Boris Perović
 */
public class MemberParser {

    /**
     * Spaces convention - one or more space characters.
     */
    static final String SPACES = "\\s+";
    /**
     * Optional spaces convention - zero or more space characters.
     */
    static final String OPTIONAL_SPACES = "\\s*";
    /**
     * Name convention - one or more alphanum + underscore characters.
     */
    static final String NAME = "\\w+";
    /**
     * Type convention - one or more alphanum, <, >, [, ] characters
     */
    static final String TYPE = "[\\w\\<\\,\\>\\[\\]]+";

    /**
     * Field convention - optional type, spaces, name. .
     */
    static final String FIELD = TYPE + SPACES + NAME;

    /**
     * Arguments convention - (zero or more characters which are not ')')
     */
    static final String ARGUMENTS_LIST = "\\([^\\)]*\\)";

    static final String METHOD = TYPE + SPACES + NAME + OPTIONAL_SPACES + ARGUMENTS_LIST;
    static final String METHOD_WITHOUT_ARGUMENTS = TYPE + SPACES + NAME;
    static final String METHOD_WITHOUT_RETURN_TYPE = NAME + OPTIONAL_SPACES + ARGUMENTS_LIST;

    /**
     * Sets attributes of the Field object by parsing the field declaration.
     *
     * @param field represents Field object
     * @param fieldDeclaration represents the declaration to set
     * @throws IllegalArgumentException if the field declaration format is not
     * proper
     */
    public static void fillFieldComponents(Field field, String fieldDeclaration) throws IllegalArgumentException {
        if (fieldDeclaration.matches(FIELD)) {

        } else if (fieldDeclaration.matches(NAME)) {
            fieldDeclaration = "Object " + fieldDeclaration;
        } else {
            throw new IllegalArgumentException("Invalid field declaration \"" + fieldDeclaration + "\"!\nExpecting \"[type] name\".");
        }

        try {
            field.setType(getFieldOrArgumentType(fieldDeclaration));
            field.setName(getFieldName(fieldDeclaration));
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Invalid field declaration \"" + fieldDeclaration + "\"!\nExpecting \"[type] name\".");
        }
    }

    /**
     * Sets attributes of the Method object by parsing the method declaration.
     *
     * @param method represents Method object
     * @param methodDeclaration represents the declaration to set
     * @throws IllegalArgumentException if the method declaration format is not
     * proper
     */
    public static void fillMethodComponents(Method method, String methodDeclaration) throws IllegalArgumentException {
        if (methodDeclaration.matches(METHOD)) {

        } else if (methodDeclaration.matches(METHOD_WITHOUT_ARGUMENTS)) {
            methodDeclaration += "()";
        } else if (methodDeclaration.matches(METHOD_WITHOUT_RETURN_TYPE)) {
            methodDeclaration = "void " + methodDeclaration;
        } else if (methodDeclaration.matches(NAME)) {
            methodDeclaration = "void " + methodDeclaration + "()";
        } else {
            throw new IllegalArgumentException("Invalid method declaration \"" + methodDeclaration + "\"!\nExpecting \"[return_type] name [(argument_list)]\".");
        }

        try {
            method.setType(getReturnType(methodDeclaration));
            method.setName(getMethodName(methodDeclaration));
            method.setArguments(getArguments(methodDeclaration));
        } catch (ParseException ex) {
            throw new IllegalArgumentException("Invalid method declaration \"" + methodDeclaration + "\"!\nExpecting \"[return_type] name [(argument_list)]\".");
        }
    }

    /**
     * Sets attributes of the Constructor object by parsing the constructor
     * declaration.
     *
     * @param constructor represents Constructor object
     * @param constructorDeclaration represents the declaration to set
     * @throws IllegalArgumentException if the constructor declaration format is
     * not proper
     */
    public static void fillConstructorComponents(Constructor constructor, String constructorDeclaration) throws IllegalArgumentException {
        if (constructorDeclaration.matches(METHOD_WITHOUT_RETURN_TYPE)) {
            try {
                constructor.setArguments(getArguments(constructorDeclaration));
            } catch (ParseException ex) {
                throw new IllegalArgumentException("Invalid constructor declaration \"" + constructorDeclaration + "\"!\nExpecting \"Class_Name ([argument_list])\".");
            }
        } else {
            throw new IllegalArgumentException("Invalid constructor declaration \"" + constructorDeclaration + "\"!\nExpecting \"Class_Name ([argument_list])\".");
        }
    }

    /**
     * Parses signature to extract type of a field or an argument.
     *
     * @param signature of a field or a declaration
     * @return String representing the type of the field or the argument
     */
    private static String getFieldOrArgumentType(String signature) throws ParseException {
        String type;
        BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature + ";");
        FieldDeclaration declaration = (FieldDeclaration) bd;
        NodeList<VariableDeclarator> variables = declaration.getVariables();
        type = variables.get(0).getType().asString().replaceAll(SPACES,"");
        return type;
    }

    /**
     * Parses stringToParse string to extract name.
     *
     * @param signature
     * @return name
     */
    private static String getFieldName(String signature) throws ParseException {
        String name;
        BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature + ";");
        FieldDeclaration declaration = (FieldDeclaration) bd;
        name = declaration.getVariables().get(0).getName().asString();
        return name;
    }

    /**
     * Parses stringToParse to extract return type of a method.
     *
     * @param signature
     * @return return type of a method
     */
    private static String getReturnType(String signature) throws ParseException {
        BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature + ";");
        if (!(bd instanceof MethodDeclaration)) {
            throw new ParseException("Invalid method '"+signature+"'");
        }
        MethodDeclaration declaration = (MethodDeclaration) bd;
        Type type = declaration.getType();
        return type.asString();        
    }

    /**
     * Parses stringToParse string to extract name.
     *
     * @param signature
     * @return name
     */
    private static String getMethodName(String signature) throws ParseException {
        BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature + ";");
        if (!(bd instanceof MethodDeclaration)) {
            throw new ParseException("Invalid method '"+signature+"'");
        }
        MethodDeclaration declaration = (MethodDeclaration) bd;
        SimpleName name = declaration.getName();
        return name.asString();
    }

    private static LinkedHashSet<MethodArgument> getArguments(String signature) throws ParseException {
        BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature + ";");
        if (!(bd instanceof MethodDeclaration)) {
            throw new ParseException("Invalid method '"+signature+"'");
        }
        MethodDeclaration declaration = (MethodDeclaration) bd;
        LinkedHashSet<MethodArgument> arguments = new LinkedHashSet();
        NodeList<Parameter> parameters = declaration.getParameters();
        for (Parameter parameter : parameters) {
            String name = parameter.getName().asString();
            String type = parameter.getType().asString();
            arguments.add(new MethodArgument(type,name));            
        }
        return arguments;
    }
}
