package org.uml.newcode.components;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
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
    protected CompilationUnit generateCode(InterfaceComponent component) {
        CompilationUnit cu = new CompilationUnit();
        cu.setTypes(new NodeList<TypeDeclaration<?>>());
        String parentPackage = component.getFullParentPackage();
        if (!parentPackage.isEmpty()) {
            cu.setPackageDeclaration(parentPackage);
        }
        createSkeleton(component, cu);
        MethodCodeGenerator.createMethods(component, cu);

        return cu;
    }

    private static void createSkeleton(InterfaceComponent component, CompilationUnit cu) {
        ClassOrInterfaceDeclaration declaration = new ClassOrInterfaceDeclaration();
        declaration.setInterface(true);
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
        if (component.isStatic())
            declaration.setModifier(Modifier.STATIC, true);
        
        ComponentBase extendedClass = getExtendedInterface(component);
        if (extendedClass != null) {
            NodeList<ClassOrInterfaceType> extended = new NodeList();
            extended.add(JavaParser.parseClassOrInterfaceType(extendedClass.getName()));
            declaration.setExtendedTypes(extended);
        }        

        declaration.setMembers(new NodeList<BodyDeclaration<?>>());

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
    protected CompilationUnit updateCode(InterfaceComponent component, MyClassDiagramRenameTable renames, CompilationUnit cu) {
        String parentPackage = component.getFullParentPackage();
        if (!parentPackage.isEmpty()) {
            cu.setPackageDeclaration(parentPackage);
        }
        updateHeader(component, cu);
        MethodCodeGenerator.updateMethods(component, renames.getComponentRenames().getMembersRenameTable(component), cu);

        return cu;
    }

    private static void updateHeader(InterfaceComponent component, CompilationUnit cu) {
        NodeList<TypeDeclaration<?>> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            if (type instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration declaration = (ClassOrInterfaceDeclaration) type;
                declaration.setName(component.getName());

                updateVisibility(declaration, component.getVisibility());

                declaration.setModifier(Modifier.STATIC, component.isStatic());

                ComponentBase extendedClass = getExtendedInterface(component);
                if (extendedClass != null) {
                    NodeList<ClassOrInterfaceType> extended = new NodeList();
                    extended.add(JavaParser.parseClassOrInterfaceType(extendedClass.getName()));
                    declaration.setExtendedTypes(extended);
                }   
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
