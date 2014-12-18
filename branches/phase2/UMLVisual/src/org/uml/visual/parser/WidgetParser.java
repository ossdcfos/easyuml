package org.uml.visual.parser;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.body.AnnotationMemberDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import java.util.LinkedHashSet;
import org.openide.util.Exceptions;
import org.uml.model.members.Field;
import org.uml.model.members.MethodBase;
import org.uml.model.members.MethodArgument;

public class WidgetParser {

    /**
     * Sets attributes of the Field object by parsing the fieldWidgetText.
     *
     * @param f represents Field object passed from FieldWidget
     * @param fieldWidgetText represents string typed in the class diagram
     */
    public void fillFieldComponents(Field f, String fieldWidgetText) {
        f.setType(getFieldOrArgumentType(fieldWidgetText));
        f.setName(getFieldName(fieldWidgetText));
    }

    /**
     * Parses stringToParse to extract type of a field
     *
     * @param signature
     * @return type of a field
     */
    public String getFieldOrArgumentType(String signature) {
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
    public String getFieldName(String signature) {
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
    public void fillMethodComponents(MethodBase m, String methodWidgetText) {
//        setMethodModifiers(getAllMethodModifiers());
        m.setArguments(getArguments(methodWidgetText));
        m.setType(getReturnType(methodWidgetText));
        m.setName(getMethodName(methodWidgetText));
    }

    private LinkedHashSet<MethodArgument> getArguments(String signature) {
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
    public String getReturnType(String signature) {
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
    public String getMethodName(String signature) {
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

//    /**
//     * Parses stringToParse string to extract modifiers.
//     *
//     * @return modifiers separated by " "
//     */
//    public String getModifier() {
//        String result = "";
//        skipWhitespaces();
//        Matcher m = methodModifiers.matcher(stringToParse);
//        if (m.lookingAt()) {
//            result = stringToParse.substring(m.start(), m.end()) + " ";
//            stringToParse = stringToParse.substring(m.end());
//        }
//        skipWhitespaces();
//        return result;
//    }
//    private int convertStringToModifier(String modifier) {
//        if ("final".equals(modifier)) {
//            return Modifier.FINAL;
//        }
//        if ("static".equals(modifier)) {
//            return Modifier.STATIC;
//        }
//        if ("abstract".equals(modifier)) {
//            return Modifier.ABSTRACT;
//        }
//        if ("synchronized".equals(modifier)) {
//            return Modifier.SYNCHRONIZED;
//        }
//        return -1;
//    }
//    /**
//     * Parses stringToParse to extract all method modifiers from it.
//     *
//     * @return string array that represents found modifiers
//     */
//    public String[] getAllMethodModifiers() {
//        String[] result = new String[4];
//        boolean hasMoreModifiers = true;
//        while (hasMoreModifiers) {
//            skipWhitespaces();
//            Matcher m = allMethodModifiers.matcher(stringToParse);
//            if (m.lookingAt()) {
//                for (int i = 0; i < 4; i++) {
//                    if (result[i] == null) {
//                        result[i] = stringToParse.substring(m.start(), m.end());
//                        break;
//                    }
//                }
//                stringToParse = stringToParse.substring(m.end());
//            } else {
//                hasMoreModifiers = false;
//            }
//        }
//        return result;
//    }
//    /**
//     * Parses stringToParse to extract all Field modifiers from it.
//     * @return string array that represents found modifiers
//     */
//    public String[] getAllFieldModifiers() {
//        String[] result = new String[3];
//        boolean hasMoreModifiers = true;
//        while(hasMoreModifiers) {
//            skipWhitespaces();
//            Matcher m = allFieldModifiers.matcher(stringToParse);
//            if(m.lookingAt()) {
//                for(int i = 0; i<3; i++) {
//                    if(result[i] == null){
//                        result[i] = stringToParse.substring(m.start(), m.end());
//                        break;
//                    }
//                }
//                stringToParse = stringToParse.substring(m.end());
//            }else {
//                hasMoreModifiers = false;
//            }
//        }
//        return result;
//    }
//    /**
//     * Sets m object's visibility and modifiers fields
//     *
//     * @param allModifiers represents all modifiers to put into object m
//     */
//    public void setMethodModifiers(String[] allModifiers) {
//        m.resetModifiers();
//        for (int i = 0; i < allModifiers.length; i++) {
//            if (allModifiers[i] != null) {
//                Matcher m = visibility.matcher(allModifiers[i]);
//                if (m.lookingAt()) {
//                    setVisibility(this.m, allModifiers[i]);
//                } else {
//                    this.m.addModifier(convertStringToModifier(allModifiers[i]));
//                }
//            }
//        }
//    }
//    /**
//     * Sets f object's  visibility and modifiers fields 
//     * @param allModifiers represents all modifiers to put into object f
//     */
//    public void setFieldModifiers(String[] allModifiers) {
//        f.resetModifiers();
//        for(int i = 0; i<allModifiers.length;i++) {
//            if(allModifiers[i] != null){
//                Matcher m = visibility.matcher(allModifiers[i]);
//                if(m.lookingAt()) {
//                    setVisibility(this.f, allModifiers[i]);
//                }else {
//                    f.setModifier(allModifiers[i]);
//                }
//            }
//        }
//    }
//    /**
//     * Sets object m's visibility field based on the input parameter.
//     *
//     * @param visibility represents value to put into object m's visibility
//     * field.
//     */
//    public void setVisibility(MemberBase m, String visibility) {
////            m.setVisibility(Visibility.valueOf(visibility.toUpperCase()));
//        if (visibility.equals("private")) {
//            m.setVisibility(Visibility.PRIVATE);
//            return;
//        }
//        if (visibility.equals("public")) {
//            m.setVisibility(Visibility.PUBLIC);
//            return;
//        }
//        if (visibility.equals("protected")) {
//            m.setVisibility(Visibility.PROTECTED);
//            return;
//        }
//        m.setVisibility(Visibility.PACKAGE);
//    }
//    /**
//     * Parses stringToParse string to extract visibility.
//     *
//     * @return visibility
//     */
//    public Visibility getVisibility() {
//        String vis = "";
//        skipWhitespaces();
//        Matcher m = visibility.matcher(stringToParse);
//        if (m.lookingAt()) {
//            vis = stringToParse.substring(m.start(), m.end());
//            stringToParse = stringToParse.substring(m.end());
//        }
//        skipWhitespaces();
//        if (vis.equals("private")) {
//            return Visibility.PRIVATE;
//        }
//        if (vis.equals("public")) {
//            return Visibility.PUBLIC;
//        }
//        if (vis.equals("protected")) {
//            return Visibility.PROTECTED;
//        }
//        if (vis.equals("")) {
//            return Visibility.PACKAGE;
//        }
//        return null;
//    }
}
