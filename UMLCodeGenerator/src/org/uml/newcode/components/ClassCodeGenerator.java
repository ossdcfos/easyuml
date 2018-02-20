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
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.RelationBase;
import org.uml.newcode.members.ConstructorCodeGenerator;
import org.uml.newcode.members.FieldCodeGenerator;
import org.uml.newcode.members.MethodCodeGenerator;

/**
 * Code generator for classes, produces one .java file.
 *
 * @author Boris Perović
 */
public class ClassCodeGenerator extends ComponentCodeGeneratorBase<ClassComponent>{

    private static ClassCodeGenerator instance;

    public static ClassCodeGenerator getInstance() {
        if (instance == null) instance = new ClassCodeGenerator();
        return instance;
    }
    
    @Override
    protected String generateCode(ClassComponent component) {
        System.out.println("Generate class "+component.getSignature());
        CompilationUnit cu = new CompilationUnit();
        cu.setTypes(new LinkedList<TypeDeclaration>());
        String parentPackage = component.getFullParentPackage();
        if (!parentPackage.isEmpty()) {
            System.out.println(parentPackage);
            cu.setPackage(new PackageDeclaration(new NameExpr(parentPackage)));
        }
        createSkeleton(component, cu);
        FieldCodeGenerator.createFields(component, cu);
        ConstructorCodeGenerator.createConstructors(component, cu);
        MethodCodeGenerator.createMethods(component, cu);

        return cu.toString();
    }

    private static void createSkeleton(ClassComponent component, CompilationUnit cu) {
        ClassOrInterfaceDeclaration declaration = new ClassOrInterfaceDeclaration();
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
        if (component.isAbstract()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.ABSTRACT));
        if (component.isStatic()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
        if (component.isFinal()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.FINAL));

        ComponentBase extendedClass = getExtendedClass(component);
        if (extendedClass != null) {
            List<ClassOrInterfaceType> extended = new LinkedList<>();
            extended.add(new ClassOrInterfaceType(extendedClass.getName()));
            declaration.setExtends(extended);
        }
        List<ComponentBase> implementedInterfaces = getImplementedInterfaces(component);
        if (!implementedInterfaces.isEmpty()) {
            List<ClassOrInterfaceType> implemented = new LinkedList<>();
            for (ComponentBase implementedComponent : implementedInterfaces) {
                implemented.add(new ClassOrInterfaceType(implementedComponent.getName()));
            }
            declaration.setImplements(implemented);
        }

        declaration.setMembers(new LinkedList<BodyDeclaration>());

        cu.getTypes().add(declaration);
    }

    private static ComponentBase getExtendedClass(ClassComponent component) {
        for (RelationBase relation : component.getParentDiagram().getRelations()) {
            if (relation instanceof IsRelation && relation.getSource() == component) {
                return relation.getTarget();
            }
        }
        return null;
    }

    private static List<ComponentBase> getImplementedInterfaces(ClassComponent component) {
        List<ComponentBase> implemented = new LinkedList<>();
        for (RelationBase relation : component.getParentDiagram().getRelations()) {
            if (relation instanceof ImplementsRelation && relation.getSource() == component) {
                implemented.add(relation.getTarget());
            }
        }
        return implemented;
    }

    @Override
    protected String updateCode(ClassComponent component, MyClassDiagramRenameTable renames, CompilationUnit cu) {
        System.out.println("Update class "+component.getSignature());
        String parentPackage = component.getFullParentPackage();
        if (!parentPackage.isEmpty()) {
            cu.setPackage(new PackageDeclaration(new NameExpr(parentPackage)));
        }
        updateSkeleton(component, cu);
        FieldCodeGenerator.updateFields(component, renames.getComponentRenames().getMembersRenameTable(component), renames.getRelationRenames(), cu);
        ConstructorCodeGenerator.updateConstructors(component, renames.getComponentRenames().getMembersRenameTable(component), cu);
        MethodCodeGenerator.updateMethods(component, renames.getComponentRenames().getMembersRenameTable(component), cu);

        return cu.toString();
    }

    private static void updateSkeleton(ClassComponent component, CompilationUnit cu) {
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            if (type instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration declaration = (ClassOrInterfaceDeclaration) type;
                declaration.setName(component.getName());
                
                updateVisibility(declaration, component.getVisibility());
                
                if (component.isAbstract()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.ABSTRACT));
                else declaration.setModifiers(ModifierSet.removeModifier(declaration.getModifiers(), ModifierSet.ABSTRACT));
                if (component.isFinal()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.FINAL));
                else declaration.setModifiers(ModifierSet.removeModifier(declaration.getModifiers(), ModifierSet.FINAL));
                if (component.isStatic()) declaration.setModifiers(ModifierSet.addModifier(declaration.getModifiers(), ModifierSet.STATIC));
                else declaration.setModifiers(ModifierSet.removeModifier(declaration.getModifiers(), ModifierSet.STATIC));

                ComponentBase extendedClass = getExtendedClass(component);
                if (extendedClass != null) {
                    List<ClassOrInterfaceType> extended = new LinkedList<>();
                    extended.add(new ClassOrInterfaceType(extendedClass.getName()));
                    declaration.setExtends(extended);
                }
                List<ComponentBase> implementedInterfaces = getImplementedInterfaces(component);
                if (!implementedInterfaces.isEmpty()) {
                    List<ClassOrInterfaceType> implemented = new LinkedList<>();
                    for (ComponentBase implementedComponent : implementedInterfaces) {
                        implemented.add(new ClassOrInterfaceType(implementedComponent.getName()));
                    }
                    declaration.setImplements(implemented);
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
