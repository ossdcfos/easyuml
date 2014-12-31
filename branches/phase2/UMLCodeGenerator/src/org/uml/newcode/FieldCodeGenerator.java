package org.uml.newcode;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.openide.util.Exceptions;
import org.uml.model.components.ClassComponent;
import org.uml.model.members.Field;
import org.uml.model.relations.CardinalityEnum;
import org.uml.model.relations.HasBaseRelation;
import org.uml.model.relations.RelationBase;
import org.uml.newcode.renaming.MemberRenameTable;

/**
 *
 * @author Boris
 */
public class FieldCodeGenerator {

    public static void createFields(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        // generate direct fields
        for (Field field : component.getFields()) {
            // add rename listener if it doesn't exist
            if (!field.listenerTypeExists(MemberRenameTable.class)) {
                field.addPropertyChangeListener(new MemberRenameTable());
            }
            // create and add field declaration
            FieldDeclaration declaration = createFieldDeclaration(field);
            members.add(declaration);
        }

        // generate fields from has relations
        for (RelationBase relation : CodeGeneratorUtils.getRelevantRelations(component)) {
            if (relation instanceof HasBaseRelation) {
                HasBaseRelation hasRelation = (HasBaseRelation) relation;
                String fieldType = hasRelation.getTarget().getName();
                String fieldName;
                if (!hasRelation.getName().equals("")) fieldName = hasRelation.getName();
                else fieldName = (fieldType.substring(0, 1)).toLowerCase() + fieldType.substring(1);

                StringBuilder fieldSignature = new StringBuilder();
                if (hasRelation.getCardinalityTarget().equals(CardinalityEnum.One2Many) || hasRelation.getCardinalityTarget().equals(CardinalityEnum.Zero2Many)) {
                    fieldSignature.append(hasRelation.getCollectionType()).append("<").append(fieldType).append("> ").append(fieldName);
                    if (!hasRelation.getName().equals("")) fieldSignature.append("s");
                    fieldSignature.append(";");
                } else {
                    fieldSignature.append(fieldType).append(" ").append(fieldName).append(";");
                }
                String signature = fieldSignature.toString();

                try {
                    BodyDeclaration bd = JavaParser.parseBodyDeclaration(signature);
                    FieldDeclaration declaration = (FieldDeclaration) bd;
                    members.add(declaration);
                } catch (ParseException ex) {
                    Exceptions.printStackTrace(ex);
                }
            }
        }

////        fields += getFieldsFromUseRelations(relevantRelations);
    }

    public static void updateFields(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();
        HashMap<String, String> oldToNew = MemberRenameTable.members.get(component.getSignature());
        // generate or update all direct fields
        for (Field field : component.getFields()) {
            // add rename listener if it doesn't exist
            if (!field.listenerTypeExists(MemberRenameTable.class)) {
                field.addPropertyChangeListener(new MemberRenameTable());
            }
            // if the field has been renamed update or generate
            if (oldToNew.containsKey(field.getSignature())) {
                String oldSignature = oldToNew.get(field.getSignature());
                boolean found = false;
                for (BodyDeclaration member : members) {
                    if (member instanceof FieldDeclaration) {
                        FieldDeclaration declaration = (FieldDeclaration) member;
                        if (oldSignature.equals(getFieldDeclarationSignature(declaration))) {
                            declaration.setType(CodeGeneratorUtils.parseType(field.getType()));
                            declaration.getVariables().get(0).getId().setName(field.getName());
                            found = true;
                            break;
                        }
                    }
                }
                if (!found) {
                    FieldDeclaration declaration = createFieldDeclaration(field);
                    members.add(declaration);
                }
            } else {
                // if it has not been renamed, confirm it exists
                FieldDeclaration declaration = findExistingDeclaration(members, field);
                // if it doesn't exits, create and add it
                if (declaration == null) {
                    declaration = createFieldDeclaration(field);
                    members.add(declaration);
                }
            }
        }
        
        // generate or update fields from has relations
    }

    private static FieldDeclaration findExistingDeclaration(List<BodyDeclaration> declarations, Field field) {
        for (BodyDeclaration declaration : declarations) {
            if (declaration instanceof FieldDeclaration && field.getSignature().equals(getFieldDeclarationSignature((FieldDeclaration) declaration))) {
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
        if (field.isStatic()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
        if (field.isFinal()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.FINAL));
        if (field.isVolatile()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.VOLATILE));
        if (field.isTransient()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.TRANSIENT));
        return declaration;
    }

    private static String getFieldDeclarationSignature(FieldDeclaration declaration) {
        String type = declaration.getType().toString();
        String name = declaration.getVariables().get(0).getId().getName();
        return type + " " + name;
    }
}
