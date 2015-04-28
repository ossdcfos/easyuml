package org.uml.newcode;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import org.openide.util.Exceptions;

/**
 * Utility methods used in code generation.
 * @author Boris Perović
 */
public class CodeGeneratorUtils {

    /**
     * Parses the type string to type class from JavaParser.
     * @param typeString to be parsed
     * @return parsed Type
     */
    public static Type parseType(String typeString) {
        Type type = null;

        if (typeString.contains("void")) type = new VoidType();
        else {
            try {
                BodyDeclaration bd = JavaParser.parseBodyDeclaration(typeString + " field;");
                FieldDeclaration declaration = (FieldDeclaration) bd;
                type = declaration.getType();
            } catch (ParseException ex) {
                Exceptions.printStackTrace(ex);
            }
        }

        return type;
    }
}
