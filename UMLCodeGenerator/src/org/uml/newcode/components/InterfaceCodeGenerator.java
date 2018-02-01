package org.uml.newcode.components;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import java.util.LinkedList;
import java.util.List;
import org.uml.filetype.cdg.renaming.MyClassDiagramRenameTable;
import org.uml.model.Visibility;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.RelationBase;
import org.uml.newcode.members.MethodCodeGenerator;

/**
 * Code generator for interfaces, produces one .java file.
 *
 * @author Boris Perović
 */
public class InterfaceCodeGenerator extends ComponentCodeGeneratorBase<InterfaceComponent> {

    private static InterfaceCodeGenerator instance;

    public static InterfaceCodeGenerator getInstance() {
        if (instance == null) instance = new InterfaceCodeGenerator();
        return instance;
    }

    @Override
    protected String generateCode(InterfaceComponent component) {
        CompilationUnit cu = new CompilationUnit();
        cu.setTypes(new LinkedList<TypeDeclaration>());
        String parentPackage = component.getFullParentPackage();
        if (!parentPackage.isEmpty()) {
            cu.setPackage(new PackageDeclaration(new NameExpr(parentPackage)));
        }
        createSkeleton(component, cu);
        MethodCodeGenerator.createMethods(component, cu);

        return cu.toString();
    }

    private static void createSkeleton(InterfaceComponent component, CompilationUnit cu) {
        ClassOrInterfaceDeclaration declaration = new ClassOrInterfaceDeclaration();
        declaration.setInterface(true);
        declaration.setName(component.getName());
        switch (component.getVisibility()) {
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
        if (component.isStatic()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));

        ComponentBase extendedInterface = getExtendedInterface(component);
        if (extendedInterface != null) {
            List<ClassOrInterfaceType> extended = new LinkedList<>();
            extended.add(new ClassOrInterfaceType(extendedInterface.getName()));
            declaration.setExtends(extended);
        }

        declaration.setMembers(new LinkedList<BodyDeclaration>());

        cu.getTypes().add(declaration);
    }

    private static ComponentBase getExtendedInterface(InterfaceComponent component) {
        for (RelationBase relation : component.getParentDiagram().getRelations()) {
            if (relation instanceof IsRelation && relation.getSource() == component) {
                return relation.getTarget();
            }
        }
        return null;
    }

    @Override
    protected String updateCode(InterfaceComponent component, MyClassDiagramRenameTable renames, CompilationUnit cu) {
        String parentPackage = component.getFullParentPackage();
        if (!parentPackage.isEmpty()) {
            cu.setPackage(new PackageDeclaration(new NameExpr(parentPackage)));
        }
        updateHeader(component, cu);
        MethodCodeGenerator.updateMethods(component, renames.getComponentRenames().getMembersRenameTable(component), cu);

        return cu.toString();
    }

    private static void updateHeader(InterfaceComponent component, CompilationUnit cu) {
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            if (type instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration declaration = (ClassOrInterfaceDeclaration) type;
                declaration.setName(component.getName());

                updateVisibility(declaration, component.getVisibility());

                if (component.isStatic()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
                else declaration.setModifiers(ModifierSet.removeModifier(declaration.getModifiers(), ModifierSet.STATIC));

                ComponentBase extendedInterface = getExtendedInterface(component);
                if (extendedInterface != null) {
                    List<ClassOrInterfaceType> extended = new LinkedList<>();
                    extended.add(new ClassOrInterfaceType(extendedInterface.getName()));
                    declaration.setExtends(extended);
                }
            }
            // Process only the first one
            // TODO what if there are more classes in one file
            break;
        }
    }

    private static void updateVisibility(TypeDeclaration declaration, Visibility visibility) {
        int modifiers = declaration.getModifiers();
        modifiers = ModifierSet.removeModifier(modifiers, ModifierSet.PUBLIC);
        modifiers = ModifierSet.removeModifier(modifiers, ModifierSet.PROTECTED);
        modifiers = ModifierSet.removeModifier(modifiers, ModifierSet.PRIVATE);

        switch (visibility) {
            case PUBLIC:
                declaration.setModifiers(ModifierSet.addModifier(modifiers, ModifierSet.PUBLIC));
                break;
            case PROTECTED:
                declaration.setModifiers(ModifierSet.addModifier(modifiers, ModifierSet.PROTECTED));
                break;
            case PRIVATE:
                declaration.setModifiers(ModifierSet.addModifier(modifiers, ModifierSet.PRIVATE));
                break;
        }
    }
}
