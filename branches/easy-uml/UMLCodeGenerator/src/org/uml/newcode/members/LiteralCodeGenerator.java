package org.uml.newcode.members;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import java.util.List;
import org.uml.filetype.cdg.renaming.MyMembersRenameTable;
import org.uml.model.components.EnumComponent;
import org.uml.model.members.Literal;

/**
 * Code generator for literals.
 *
 * @author Boris Perović
 */
public class LiteralCodeGenerator {

    public static void createLiterals(EnumComponent component, CompilationUnit cu) {
        EnumDeclaration enumDeclaration = (EnumDeclaration) cu.getTypes().get(0);
        List<EnumConstantDeclaration> literals = enumDeclaration.getEntries();

        // Generate direct literals
        for (Literal literal : component.getLiterals()) {
            // Create and add literal declaration
            EnumConstantDeclaration declaration = createLiteralDeclaration(literal);
            literals.add(declaration);
        }
    }

    public static void updateLiterals(EnumComponent component, MyMembersRenameTable memberRenames, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();
        // Generate or update all direct fields
        for (Literal literal : component.getLiterals()) {
            EnumConstantDeclaration existingDeclaration = findExistingDeclaration(members, literal.getSignature());
            if (existingDeclaration == null) { // If there is not existing declaration in the class body
                if (memberRenames.contains(literal)) { // If the lieteral has been renamed
                    // Find the old literal declaration
                    String oldSignature = memberRenames.getOriginalSignature(literal);
                    boolean found = false;
                    for (BodyDeclaration member : members) {
                        if (member instanceof EnumConstantDeclaration) {
                            EnumConstantDeclaration declaration = (EnumConstantDeclaration) member;
                            if (oldSignature.equals(getEnumConstantDeclarationSignature(declaration))) {
                                // Update the old literal declaration
                                declaration.setName(literal.getName());
                                // Finish updating the old literal declaration
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) { // If the old literal declaration has not been found and updated, create it and add it
                        EnumConstantDeclaration declaration = createLiteralDeclaration(literal);
                        members.add(declaration);
                    }
                } else { // If the literal has not been renamed, there is nothing to update, so create and add it
                    EnumConstantDeclaration declaration = createLiteralDeclaration(literal);
                    members.add(declaration);
                }
            }
        }
    }

    private static EnumConstantDeclaration findExistingDeclaration(List<BodyDeclaration> declarations, String signature) {
        for (BodyDeclaration declaration : declarations) {
            if (declaration instanceof EnumConstantDeclaration && signature.equals(((EnumConstantDeclaration) declaration).getName())) {
                return (EnumConstantDeclaration) declaration;
            }
        }
        return null;
    }

    private static EnumConstantDeclaration createLiteralDeclaration(Literal literal) {
        EnumConstantDeclaration declaration = new EnumConstantDeclaration();
        declaration.setName(literal.getName());
        return declaration;
    }

    private static String getEnumConstantDeclarationSignature(EnumConstantDeclaration declaration) {
        return declaration.getName();
    }
}
