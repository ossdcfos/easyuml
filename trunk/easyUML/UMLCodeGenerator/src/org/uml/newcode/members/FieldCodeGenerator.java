package org.uml.newcode.members;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import java.util.LinkedList;
import java.util.List;
import org.openide.util.Exceptions;
import org.uml.filetype.cdg.renaming.MyMembersRenameTable;
import org.uml.filetype.cdg.renaming.MyRelationsRenameTable;
import org.uml.model.components.ClassComponent;
import org.uml.model.members.Field;
import org.uml.model.relations.HasBaseRelation;
import org.uml.newcode.CodeGeneratorUtils;

/**
 * Code generator for fields.
 *
 * @author Boris Perović
 */
public class FieldCodeGenerator {

    public static void createFields(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        // Generate direct fields
        for (Field field : component.getFields()) {
            // Create and add field declaration
            FieldDeclaration declaration = createFieldDeclaration(field);
            members.add(declaration);
        }

        // Generate fields from has relations
        // TODO disabled, because of Has relations don't yet have field modifiers etc.
        // Should explore how the standard deals with this and decide on a solution.
//        for (RelationBase relation : component.getRelevantRelations()) {
//            if (relation instanceof HasBaseRelation) {
//                HasBaseRelation hasRelation = (HasBaseRelation) relation;
//                FieldDeclaration declaration = createFieldDeclaration(hasRelation);
//                members.add(declaration);
//            }
//        }
    }

    public static void updateFields(ClassComponent component, MyMembersRenameTable memberRenames, MyRelationsRenameTable relationRenames, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();
        // Generate or update all direct fields
        for (Field field : component.getFields()) {
            FieldDeclaration existingDeclaration = findExistingDeclaration(members, field.getSignature());
            if (existingDeclaration == null) { // If there is not existing declaration in the class body
                if (memberRenames.contains(field)) { // If the field has been renamed
                    // Find the old field declaration
                    String oldSignature = memberRenames.getOriginalSignature(field);
                    boolean found = false;
                    for (BodyDeclaration member : members) {
                        if (member instanceof FieldDeclaration) {
                            FieldDeclaration declaration = (FieldDeclaration) member;
                            if (oldSignature.equals(getFieldDeclarationSignature(declaration))) {
                                // Update the old field declaration
                                declaration.setType(CodeGeneratorUtils.parseType(field.getType()));
                                declaration.getVariables().get(0).getId().setName(field.getName());
                                // Finish updating the old field declaration
                                found = true;
                                break;
                            }
                        }
                    }
                    if (!found) { // If the old field declaration has not been found and updated, create it and add it
                        FieldDeclaration declaration = createFieldDeclaration(field);
                        members.add(declaration);
                    }
                } else { // If the field has not been renamed, there is nothing to update, so create and add it
                    FieldDeclaration declaration = createFieldDeclaration(field);
                    members.add(declaration);
                }
            }
        }

        // Generate or update fields from has relations
        // TODO disabled, because of Has relations don't yet have field modifiers etc.
        // Should explore how the standard deals with this and decide on a solution.
//        for (RelationBase relation : component.getRelevantRelations()) {
//            if (relation instanceof HasBaseRelation) {
//                HasBaseRelation hasRelation = (HasBaseRelation) relation;
//
//                // If the field has been renamed update or generate
//                if (relationRenames.contains(hasRelation)) {
//                    // Find the original parsed field
//                    String oldSignature = relationRenames.getOriginalSignature(hasRelation);
//                    boolean found = false;
//                    for (BodyDeclaration member : members) {
//                        if (member instanceof FieldDeclaration) {
//                            FieldDeclaration declaration = (FieldDeclaration) member;
//                            // Update the original parsed field
//                            if (oldSignature.equals(getFieldDeclarationSignature(declaration))) {
//                                declaration.getVariables().get(0).getId().setName(hasRelation.getName());
//                                found = true;
//                                break;
//                            }
//                        }
//                    }
//                    // If the field has not been updated, generate it
//                    if (!found) {
//                        FieldDeclaration declaration = createFieldDeclaration(hasRelation);
//                        members.add(declaration);
//                    }
//                } // If it has not been renamed, confirm it exists
//                else {
//                    FieldDeclaration declaration = findExistingDeclaration(members, hasRelation.getFieldSignature());
//                    // If it doesn't exits, create and add it
//                    if (declaration == null) {
//                        declaration = createFieldDeclaration(hasRelation);
//                        members.add(declaration);
//                    }
//                }
//            }
//        }
    }

    private static FieldDeclaration findExistingDeclaration(List<BodyDeclaration> declarations, String signature) {
        for (BodyDeclaration declaration : declarations) {
            if (declaration instanceof FieldDeclaration && signature.equals(getFieldDeclarationSignature((FieldDeclaration) declaration))) {
                return (FieldDeclaration) declaration;
            }
        }
        return null;
    }

    private static FieldDeclaration createFieldDeclaration(Field field) {
        FieldDeclaration declaration = new FieldDeclaration();
        VariableDeclarator variable = new VariableDeclarator(new VariableDeclaratorId(field.getName()));
        List<VariableDeclarator> variables = new LinkedList<>();
        variables.add(variable);
        declaration.setVariables(variables);
        String type = field.getType();
        declaration.setType(CodeGeneratorUtils.parseType(type));
        switch (field.getVisibility()) {
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
        if (field.isStatic()) {
            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
        }
        if (field.isFinal()) {
            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.FINAL));
        }
        if (field.isVolatile()) {
            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.VOLATILE));
        }
        if (field.isTransient()) {
            declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.TRANSIENT));
        }
        return declaration;
    }

    private static FieldDeclaration createFieldDeclaration(HasBaseRelation hasRelation) {
        try {
            String signature = hasRelation.getFieldSignature() + ";";
            BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature);
            FieldDeclaration declaration = (FieldDeclaration) bd;
            return declaration;
        } catch (ParseException ex) {
            Exceptions.printStackTrace(ex);
        }
        return null;
    }

    private static String getFieldDeclarationSignature(FieldDeclaration declaration) {
        String type = declaration.getType().toString();
        String name = declaration.getVariables().get(0).getId().getName();
        return type + " " + name;
    }
}
