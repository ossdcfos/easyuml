package org.uml.newcode.members;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.EnumConstantDeclaration;
import japa.parser.ast.body.EnumDeclaration;
import java.util.List;
import org.uml.model.components.EnumComponent;
import org.uml.model.members.Literal;

/**
 *
 * @author Boris
 */
public class LiteralCodeGenerator {

    public static void createLiterals(EnumComponent component, CompilationUnit cu) {
        EnumDeclaration enumDeclaration = (EnumDeclaration) cu.getTypes().get(0);
        List<EnumConstantDeclaration> literals = enumDeclaration.getEntries();

        // generate direct literals
        for (Literal literal : component.getLiterals()) {
            // create and add literal declaration
            EnumConstantDeclaration declaration = createLiteralDeclaration(literal);
            literals.add(declaration);
        }
    }

    private static EnumConstantDeclaration createLiteralDeclaration(Literal literal) {
        EnumConstantDeclaration declaration = new EnumConstantDeclaration();
        declaration.setName(literal.getName());
        return declaration;
    }
}
