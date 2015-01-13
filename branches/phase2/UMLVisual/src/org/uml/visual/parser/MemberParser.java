package org.uml.visual.parser;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import java.util.LinkedHashSet;
import javax.swing.JOptionPane;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;

public class MemberParser {

//    String whitespaces = "\\s*";
//    String visibility = "public|private|protected";
//    String methodModifiers = "static|final|abstract|synchronized";
//    String allMethodModifiers = visibility + "|" + methodModifiers;
//    String fieldModifiers = "static|final|transient|volatile";
//    String allFieldModifiers = visibility + "|" + fieldModifiers;
//    String returnValueType = "(void|int|float|double|boolean)|([A-Z]\\w+\\s*(<\\s*\\w+(\\s*,\\s*\\w+\\s*)*\\s*>\\s*)*)";
//    String argumentType = "(int|float|double|boolean)|([A-Z]\\w+\\s*(<\\s*([A-Z]\\w+)(\\s*,\\s*([A-Z]\\w+)\\s*)*\\s*>\\s*))|([A-Z]\\w+)";
//    String arrayCheck = "(\\[\\s*\\])?";
//    String name = "\\w+";
//    String argumentBlock = "\\(.*\\)";
    static String NAME = "\\w+";
    static String FIELD = "[\\w\\<\\>\\[\\]]+" + "\\s+" + "(\\w+)";

    static String METHOD = "[\\w\\<\\>\\[\\]]+" + "\\s+" + "(\\w+)" + "\\s*" + "\\([^\\)]*\\)";
    static String METHOD_WITHOUT_ARGUMENTS = "[\\w\\<\\>\\[\\]]+" + "\\s+" + "(\\w+)";

    /**
     * Sets attributes of the Field object by parsing the fieldWidgetText.
     *
     * @param f represents Field object passed from FieldWidget
     * @param fieldWidgetText represents string typed in the class diagram
     */
    public static void fillFieldComponents(Field f, String fieldWidgetText) {
        if (fieldWidgetText.matches(FIELD)) {

        } else if (fieldWidgetText.matches(NAME)) {
            fieldWidgetText = "Object " + fieldWidgetText;
        } else {
            JOptionPane.showMessageDialog(null, "Invalid field declaration \"" + fieldWidgetText + "\"!\nExpecting \"[type] name\".", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            f.setType(getFieldOrArgumentType(fieldWidgetText));
            f.setName(getFieldName(fieldWidgetText));
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Invalid field declaration \"" + fieldWidgetText + "\"!\nExpecting \"[type] name\".", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Sets attributes of the Method object by parsing the methodWidgetText.
     *
     * @param m represents Method object passed from MethodWidget
     * @param methodWidgetText represents string typed in the class diagram
     */
    public static void fillMethodComponents(Method m, String methodWidgetText) {
        if (methodWidgetText.matches(METHOD)) {

        } else if (methodWidgetText.matches(METHOD_WITHOUT_ARGUMENTS)) {
            methodWidgetText += "()";
        } else if (methodWidgetText.matches(NAME)) {
            methodWidgetText = "void " + methodWidgetText + "()";
        } else {
            JOptionPane.showMessageDialog(null, "Invalid method declaration \"" + methodWidgetText + "\"!\nExpecting \"[return_type] name [(argument_list)]\".", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            m.setType(getReturnType(methodWidgetText));
            m.setName(getMethodName(methodWidgetText));
            m.setArguments(getArguments(methodWidgetText));
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Invalid method declaration \"" + methodWidgetText + "\"!\nExpecting \"[return_type] name [([argument_list])]\".", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
//        setMethodModifiers(getAllMethodModifiers());
    }

    public static void fillConstructorComponents(Constructor c, String constructorWidgetText) {
        try {
            c.setArguments(getArguments(constructorWidgetText));
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Invalid constructor declaration \"" + constructorWidgetText + "\"!\nExpecting \"Class_Name ([argument_list])\".", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    }

    /**
     * Parses stringToParse to extract type of a field
     *
     * @param signature
     * @return type of a field
     */
    private static String getFieldOrArgumentType(String signature) throws ParseException {
        String type = "Object";
        BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature + ";");
        FieldDeclaration declaration = (FieldDeclaration) bd;
        type = declaration.getType().toString();
        return type;
    }
    
    /**
     * Parses stringToParse string to extract name.
     *
     * @param signature
     * @return name
     */
    private static String getFieldName(String signature) throws ParseException {
        String name = null;
        BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature + ";");
        FieldDeclaration declaration = (FieldDeclaration) bd;
        name = declaration.getVariables().get(0).getId().getName();
        return name;
    }
    
    /**
     * Parses stringToParse to extract return type of a method.
     *
     * @param signature
     * @return return type of a method
     */
    private static String getReturnType(String signature) throws ParseException {
        String type = null;
        // has to empty the arguments list in order not to an argument exception
        signature = signature.replaceAll("\\(.*\\)", "()");
        if (signature.contains("void")) {
            type = "void";
        } else {
            BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature + ";");

            AnnotationMemberDeclaration declaration = (AnnotationMemberDeclaration) bd;
            type = declaration.getType().toString();
        }
        return type;
    }

    /**
     * Parses stringToParse string to extract name.
     *
     * @param signature
     * @return name
     */
    private static String getMethodName(String signature) throws ParseException {
        String name = null;
        // has to empty the arguments list in order not to an argument exception
        signature = signature.replaceAll("\\(.*\\)", "()");
        if (signature.contains("void")) {
            signature = signature.replace("void", "int"); // arbitrary type, not to have exception because of void
        }
        BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature + ";");
        AnnotationMemberDeclaration declaration = (AnnotationMemberDeclaration) bd;
        name = declaration.getName();
        return name;
    }
    
    private static LinkedHashSet<MethodArgument> getArguments(String signature) throws ParseException {
        LinkedHashSet<MethodArgument> arguments = new LinkedHashSet<>();
        String argumentString = signature.substring(signature.indexOf("(") + 1, signature.indexOf(")"));
        String[] argumentsArray = argumentString.split(",");
        for (String argument : argumentsArray) {
            if (!argument.equals("")) {
                argument = argument.trim() + ";";

                BodyDeclaration bd = JavaParser.parseBodyDeclaration(argument);
                FieldDeclaration declaration = (FieldDeclaration) bd;
                arguments.add(new MethodArgument(declaration.getType().toString(), declaration.getVariables().get(0).getId().getName()));
            }
        }
        return arguments;
    }
}
