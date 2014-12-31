package org.uml.newcode;

import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ConstructorDeclaration;
import japa.parser.ast.body.FieldDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.Parameter;
import japa.parser.ast.body.VariableDeclarator;
import japa.parser.ast.body.VariableDeclaratorId;
import java.util.LinkedList;
import java.util.List;
import org.uml.model.components.ClassComponent;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.members.MethodArgument;
import org.uml.newcode.renaming.ComponentRenameTable;
import org.uml.newcode.renaming.MemberRenameTable;

/**
 *
 * @author Boris
 */
public class ConstructorCodeGenerator {

    public static void createConstructors(ClassComponent component, CompilationUnit cu) {
        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();

        for (Constructor constructor : component.getConstructors()) {
            ConstructorDeclaration declaration = new ConstructorDeclaration();
            declaration.setName(component.getName());
            switch (constructor.getVisibility()) {
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
            if (!constructor.getArguments().isEmpty()) {
                List<Parameter> parameters = new LinkedList<>();
                for (MethodArgument argument : constructor.getArguments()) {
                    Parameter parameter = new Parameter();
                    String type = argument.getType();
                    parameter.setType(CodeGeneratorUtils.parseType(type));
                    parameter.setId(new VariableDeclaratorId(argument.getName()));
                    parameters.add(parameter);
                }
                declaration.setParameters(parameters);
            }
            members.add(declaration);
        }
    }

    static void updateConstructors(ClassComponent component, CompilationUnit cu) {
//        List<BodyDeclaration> members = cu.getTypes().get(0).getMembers();
//        
//        for(Constructor constructor : component.getConstructors()){
//            if(ComponentRenameTable.components.containsKey(component.getSignature())){
//                String oldSignature = ComponentRenameTable.components.get(component.getSignature());
//                boolean found = false;
//                for(BodyDeclaration member : members){
//                    if(member instanceof ConstructorDeclaration){
//                        ConstructorDeclaration declaration = (ConstructorDeclaration)member;
//                        if(oldSignature.equals(getConstructorDeclarationSignature(declaration))){
//                            declaration.setName(component.getName());
//                        }
//                    }
//                }
//            }
//        }
//        for (Field field : component.getFields()) {
//            // add rename listener if it doesn't exist
//            if (!field.listenerTypeExists(MemberRenameTable.class)) {
//                field.addPropertyChangeListener(new MemberRenameTable());
//            }
//            // if the field has been renamed update or generate
//            if (oldToNew.containsKey(field.getSignature())) {
//                String oldSignature = oldToNew.get(field.getSignature());
//                boolean found = false;
//                for (BodyDeclaration member : members) {
//                    if (member instanceof FieldDeclaration) {
//                        FieldDeclaration declaration = (FieldDeclaration) member;
//                        if (oldSignature.equals(getFieldDeclarationSignature(declaration))) {
//                            declaration.setType(CodeGeneratorUtils.parseType(field.getType()));
//                            declaration.getVariables().get(0).getId().setName(field.getName());
//                            found = true;
//                            break;
//                        }
//                    }
//                }
//                if (!found) {
//                    FieldDeclaration declaration = createFieldDeclaration(field);
//                    members.add(declaration);
//                }
//            } else {
//                // if it has not been renamed, confirm it exists
//                FieldDeclaration declaration = findExistingDeclaration(members, field);
//                // if it doesn't exits, create and add it
//                if (declaration == null) {
//                    declaration = createFieldDeclaration(field);
//                    members.add(declaration);
//                }
//            }
//        }
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

    private static Object getConstructorDeclarationSignature(ConstructorDeclaration declaration) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
