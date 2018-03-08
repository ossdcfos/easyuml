package org.uml.newcode.components;

import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import org.uml.newcode.members.LiteralCodeGenerator;
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
    protected CompilationUnit generateCode(EnumComponent component) {
        CompilationUnit cu = new CompilationUnit();
        cu.setTypes(new NodeList<TypeDeclaration<?>>());
        String parentPackage = component.getFullParentPackage();
        if (!parentPackage.isEmpty()) {
            cu.setPackageDeclaration(parentPackage);
        }
        createSkeleton(component, cu);
        LiteralCodeGenerator.createLiterals(component, cu);

        return cu;
    }

    private static void createSkeleton(EnumComponent component, CompilationUnit cu) {
        EnumDeclaration declaration = new EnumDeclaration();
        declaration.setName(component.getName());
        switch (component.getVisibility()) {
            case PUBLIC:
                declaration.setModifier(Modifier.PUBLIC, true);
                break;
            case PROTECTED:
                declaration.setModifier(Modifier.PROTECTED, true);
                break;
            case PRIVATE:
                declaration.setModifier(Modifier.PRIVATE, true);
                break;
        }

        declaration.setEntries(new NodeList<EnumConstantDeclaration>());
        cu.getTypes().add(declaration);
    }

    @Override
    protected CompilationUnit updateCode(EnumComponent component, MyClassDiagramRenameTable renames, CompilationUnit cu) {
        String parentPackage = component.getFullParentPackage();
        if (!parentPackage.isEmpty()) {
            cu.setPackageDeclaration(parentPackage);
        }
        updateSkeleton(component, cu);
        LiteralCodeGenerator.updateLiterals(component, renames.getComponentRenames().getMembersRenameTable(component), cu);

        return cu;
    }

    private static void updateSkeleton(EnumComponent component, CompilationUnit cu) {
        NodeList<TypeDeclaration<?>> types = cu.getTypes();
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
        declaration.removeModifier(Modifier.PUBLIC,Modifier.PROTECTED,Modifier.PRIVATE);
        switch (visibility) {
            case PUBLIC:
                declaration.setModifier(Modifier.PUBLIC, true);
                break;
            case PROTECTED:
                declaration.setModifier(Modifier.PROTECTED, true);
                break;
            case PRIVATE:
                declaration.setModifier(Modifier.PRIVATE, true);
                break;
        }
    }
}
