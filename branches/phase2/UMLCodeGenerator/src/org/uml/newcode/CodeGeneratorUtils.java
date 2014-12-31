package org.uml.newcode;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.type.Type;
import japa.parser.ast.type.VoidType;
import java.util.LinkedList;
import java.util.List;
import org.openide.util.Exceptions;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.RelationBase;

/**
 *
 * @author Boris
 */
public class CodeGeneratorUtils {

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

    public static List<RelationBase> getRelevantRelations(ComponentBase component) {
        List<RelationBase> relevantRelations = new LinkedList<>();
        for (RelationBase rc : component.getParentDiagram().getRelations()) {
            if (rc.getSource().equals(component)) {
                relevantRelations.add(rc);
            }
        }
        return relevantRelations;
    }
}
