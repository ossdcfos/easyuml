package org.uml.visual.parser;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import java.util.LinkedHashSet;
import org.openide.util.Exceptions;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;

public class MemberParser {

    /**
     * Sets attributes of the Field object by parsing the fieldWidgetText.
     *
     * @param f represents Field object passed from FieldWidget
     * @param fieldWidgetText represents string typed in the class diagram
     */
    public static void fillFieldComponents(Field f, String fieldWidgetText) {
        f.setType(getFieldOrArgumentType(fieldWidgetText));
        f.setName(getFieldName(fieldWidgetText));
    }

    /**
     * Parses stringToParse to extract type of a field
     *
     * @param signature
     * @return type of a field
     */
    private static String getFieldOrArgumentType(String signature) {
        String type = null;
        try {
            BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature + ";");
            FieldDeclaration declaration = (FieldDeclaration) bd;
            type = declaration.getType().toString();
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
        return type;
    }

    /**
     * Parses stringToParse string to extract name.
     *
     * @param signature
     * @return name
     */
    private static String getFieldName(String signature) {
        String name = null;
        try {
            BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature + ";");
            FieldDeclaration declaration = (FieldDeclaration) bd;
            name = declaration.getVariables().get(0).getId().getName();
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
        return name;
    }

    /**
     * Sets attributes of the Method object by parsing the methodWidgetText.
     *
     * @param m represents Method object passed from MethodWidget
     * @param methodWidgetText represents string typed in the class diagram
     */
    public static void fillMethodComponents(Method m, String methodWidgetText) {
//        setMethodModifiers(getAllMethodModifiers());
        m.setArguments(getArguments(methodWidgetText));
        m.setType(getReturnType(methodWidgetText));
        m.setName(getMethodName(methodWidgetText));
    }

    public static void fillConstructorComponents(Constructor c, String constructorWidgetText) {
        c.setArguments(getArguments(constructorWidgetText));
    }

    private static LinkedHashSet<MethodArgument> getArguments(String signature) {
        LinkedHashSet<MethodArgument> arguments = new LinkedHashSet<>();
        String argumentString = signature.substring(signature.indexOf("(") + 1, signature.indexOf(")"));
        String[] argumentsArray = argumentString.split(",");
        for (String argument : argumentsArray) {
            if (!argument.equals("")) {
                argument = argument.trim() + ";";

                BodyDeclaration bd;
                try {
                    bd = JavaParser.parseBodyDeclaration(argument);
                    FieldDeclaration declaration = (FieldDeclaration) bd;
                    arguments.add(new MethodArgument(declaration.getType().toString(), declaration.getVariables().get(0).getId().getName()));
                } catch (ParseException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }
        return arguments;
    }

    /**
     * Parses stringToParse to extract return type of a method.
     *
     * @param signature
     * @return return type of a method
     */
    private static String getReturnType(String signature) {
        String type = null;
        // has to empty the arguments list in order not to an argument exception
        signature = signature.replaceAll("\\(.*\\)", "()");
        if (signature.contains("void")) type = "void";
        else {
            try {
                BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature + ";");

                AnnotationMemberDeclaration declaration = (AnnotationMemberDeclaration) bd;
                type = declaration.getType().toString();
            } catch (ParseException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
        return type;
    }

    /**
     * Parses stringToParse string to extract name.
     *
     * @param signature
     * @return name
     */
    private static String getMethodName(String signature) {
        String name = null;
        // has to empty the arguments list in order not to an argument exception
        signature = signature.replaceAll("\\(.*\\)", "()");
        try {
            if (signature.contains("void")) signature = signature.replace("void", "int"); // arbitrary type, not to have exception because of void
            BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature + ";");
            AnnotationMemberDeclaration declaration = (AnnotationMemberDeclaration) bd;
            name = declaration.getName();
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
        return name;
    }
}
