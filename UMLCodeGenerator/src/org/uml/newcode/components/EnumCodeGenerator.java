package org.uml.newcode.components;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.ModifierSet;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.expr.NameExpr;
import org.uml.newcode.members.LiteralCodeGenerator;
import java.util.LinkedList;
import java.util.List;
import org.uml.filetype.cdg.renaming.MyClassDiagramRenameTable;
import org.uml.model.Visibility;
import static org.uml.model.Visibility.PRIVATE;
import static org.uml.model.Visibility.PROTECTED;
import static org.uml.model.Visibility.PUBLIC;
import org.uml.model.components.EnumComponent;

/**
 * Code generator for enums, produces one .java file.
 * @author Boris Perović
 */
public class EnumCodeGenerator extends ComponentCodeGeneratorBase<EnumComponent> {

    private static EnumCodeGenerator instance;

    public static EnumCodeGenerator getInstance() {
        if (instance == null) instance = new EnumCodeGenerator();
        return instance;
    }

    @Override
    protected String generateCode(EnumComponent component) {
        CompilationUnit cu = new CompilationUnit();
        cu.setTypes(new LinkedList<TypeDeclaration>());
        String parentPackage = component.getFullParentPackage();
        if (!parentPackage.isEmpty()) {
            cu.setPackage(new PackageDeclaration(new NameExpr(parentPackage)));
        }
        createSkeleton(component, cu);
        LiteralCodeGenerator.createLiterals(component, cu);

        return cu.toString();
    }

    private static void createSkeleton(EnumComponent component, CompilationUnit cu) {
        EnumDeclaration declaration = new EnumDeclaration();
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

        declaration.setEntries(new LinkedList<EnumConstantDeclaration>());
        cu.getTypes().add(declaration);
    }

    @Override
    protected String updateCode(EnumComponent component, MyClassDiagramRenameTable renames, CompilationUnit cu) {
        String parentPackage = component.getFullParentPackage();
        if (!parentPackage.isEmpty()) {
            cu.setPackage(new PackageDeclaration(new NameExpr(parentPackage)));
        }
        updateSkeleton(component, cu);
        LiteralCodeGenerator.updateLiterals(component, renames.getComponentRenames().getMembersRenameTable(component), cu);

        return cu.toString();
    }

    private static void updateSkeleton(EnumComponent component, CompilationUnit cu) {
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            if (type instanceof EnumDeclaration) {
                EnumDeclaration declaration = (EnumDeclaration) type;
                declaration.setName(component.getName());

                updateVisibility(declaration, component.getVisibility());
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
