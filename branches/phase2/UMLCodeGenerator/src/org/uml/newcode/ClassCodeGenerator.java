package org.uml.newcode;

import de.hunsicker.jalopy.Jalopy;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.Token;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.PackageDeclaration;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;
import japa.parser.ast.body.ModifierSet;
import japa.parser.ast.body.TypeDeclaration;
import japa.parser.ast.expr.NameExpr;
import japa.parser.ast.type.ClassOrInterfaceType;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;
import org.openide.util.Exceptions;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.relations.ImplementsRelation;
import org.uml.model.relations.IsRelation;
import org.uml.model.relations.RelationBase;
import org.uml.newcode.renaming.ComponentRenameTable;

/**
 *
 * @author Boris
 */
class ClassCodeGenerator {

    public static void generateOrUpdateCode(ClassComponent component, String sourcePath) {

        String code = "";
        File sourceFile;
        // if component has been renamed, the source file should have the old name
        if (ComponentRenameTable.components.containsKey(component.getSignature())) {  //-> new to old
            String newSignature = component.getSignature();
            String oldFullQualifiedName = ComponentRenameTable.components.get(newSignature);
            String pathToOldFile = sourcePath + oldFullQualifiedName.replace(".", File.separator) + ".java";
            sourceFile = new File(pathToOldFile);
        } 
        // if the component has not been renamed, the source should have the new name
        else {
            String fullQualifiedName = component.getSignature();
            String pathToFile = sourcePath + fullQualifiedName.replace(".", File.separator) + ".java";
            sourceFile = new File(pathToFile);
        }

        // if source exists, update code
        if (sourceFile.exists()) {
            try {
                FileReader fileReader = new FileReader(sourceFile);
                CompilationUnit cu = JavaParser.parse(fileReader, true);
                fileReader.close();
                code = updateCode(component, cu);
                sourceFile.delete();
            } catch (ParseException ex) {
                Token tok = ex.currentToken;
                JOptionPane.showMessageDialog(null, "Malformed code at line " + tok.beginLine + " column " + tok.beginColumn + ". Cannot update!", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (FileNotFoundException ex) {
                // Already checked for file existance, but if file is somehow deleted, generate code.
                code = generateCode(component);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "IOException!", "Error", JOptionPane.ERROR_MESSAGE);
                Exceptions.printStackTrace(ex);
            }
        } 
        // if source does not exist, generate code from scratch
        else {
            code = generateCode(component);
        }

        // Add package to path
        String pack = component.getParentPackage();
        String fullPackagePath;
        if (!pack.equals("")) {
            String packagePath = pack.replace(".", File.separator);
            fullPackagePath = sourcePath + packagePath + File.separator;
        } else {
            fullPackagePath = sourcePath;
        }

        // Create path folder structure
        new File(fullPackagePath).mkdirs();

        String name = component.getName();
        // Write-out the source file
        File outSourceFile = new File(fullPackagePath + name + ".java");
        try {
            FileUtils.writeStringToFile(outSourceFile, code);
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Cannot write file " + outSourceFile.getName() + "!", "Error", JOptionPane.ERROR_MESSAGE);
        }

        // Format the source file
        try {
            Jalopy jlp = new Jalopy();
            jlp.setInput(outSourceFile);
            jlp.setOutput(outSourceFile);
            jlp.format();
        } catch (FileNotFoundException ex) {
        }
    }

    private static String generateCode(ClassComponent component) {
        CompilationUnit cu = new CompilationUnit();
        cu.setTypes(new LinkedList<TypeDeclaration>());
        if (!component.getParentPackage().equals("")) cu.setPackage(new PackageDeclaration(new NameExpr(component.getParentPackage())));
        createHeader(component, cu);
        FieldCodeGenerator.createFields(component, cu);
        ConstructorCodeGenerator.createConstructors(component, cu);
        MethodCodeGenerator.createMethods(component, cu);

        return cu.toString();
    }

    private static void createHeader(ClassComponent component, CompilationUnit cu) {
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

        ComponentBase extendedComponent = getClassThatIsExtended(component);
        if (extendedComponent != null) {
            List<ClassOrInterfaceType> extended = new LinkedList<>();
            extended.add(new ClassOrInterfaceType(extendedComponent.getName()));
            declaration.setExtends(extended);
        }
        List<ComponentBase> implementedComponents = getClassesThatAreImplemented(component);
        if (!implementedComponents.isEmpty()) {
            List<ClassOrInterfaceType> implemented = new LinkedList<>();
            for (ComponentBase implementedComponent : implementedComponents) {
                implemented.add(new ClassOrInterfaceType(implementedComponent.getName()));
            }
            declaration.setImplements(implemented);
        }

        declaration.setMembers(new LinkedList<BodyDeclaration>());

        cu.getTypes().add(declaration);
    }

    private static ComponentBase getClassThatIsExtended(ClassComponent component) {
        for (RelationBase relation : component.getParentDiagram().getRelations()) {
            if (relation instanceof IsRelation && relation.getSource() == component) {
                return relation.getTarget();
            }
        }
        return null;
    }

    private static List<ComponentBase> getClassesThatAreImplemented(ClassComponent component) {
        List<ComponentBase> implemented = new LinkedList<>();
        for (RelationBase relation : component.getParentDiagram().getRelations()) {
            if (relation instanceof ImplementsRelation && relation.getSource() == component) {
                implemented.add(relation.getTarget());
            }
        }
        return implemented;
    }

    private static String updateCode(ClassComponent component, CompilationUnit cu) {
        if (!component.getParentPackage().equals("")) cu.setPackage(new PackageDeclaration(new NameExpr(component.getParentPackage())));
        updateHeader(component, cu);
        FieldCodeGenerator.updateFields(component, cu);
        
        // TODO update
//        ConstructorCodeGenerator.createConstructors(component, cu);
//        MethodCodeGenerator.createMethods(component, cu);

        return cu.toString();
    }

    private static void updateHeader(ClassComponent component, CompilationUnit cu) {
        List<TypeDeclaration> types = cu.getTypes();
        for (TypeDeclaration type : types) {
            if (type instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration cidecl = (ClassOrInterfaceDeclaration) type;
                cidecl.setName(component.getName());

                if (component.isAbstract()) cidecl.setModifiers(ModifierSet.addModifier(cidecl.getModifiers(), ModifierSet.ABSTRACT));
                else cidecl.setModifiers(ModifierSet.removeModifier(cidecl.getModifiers(), ModifierSet.ABSTRACT));
                if (component.isFinal()) cidecl.setModifiers(ModifierSet.addModifier(cidecl.getModifiers(), ModifierSet.FINAL));
                else cidecl.setModifiers(ModifierSet.removeModifier(cidecl.getModifiers(), ModifierSet.FINAL));
                if (component.isStatic()) cidecl.setModifiers(ModifierSet.addModifier(cidecl.getModifiers(), ModifierSet.STATIC));
                else cidecl.setModifiers(ModifierSet.removeModifier(cidecl.getModifiers(), ModifierSet.STATIC));

                ComponentBase extendedComponent = getClassThatIsExtended(component);
                if (extendedComponent != null) {
                    List<ClassOrInterfaceType> extended = new LinkedList<>();
                    extended.add(new ClassOrInterfaceType(extendedComponent.getName()));
                    cidecl.setExtends(extended);
                }
                List<ComponentBase> implementedComponents = getClassesThatAreImplemented(component);
                if (!implementedComponents.isEmpty()) {
                    List<ClassOrInterfaceType> implemented = new LinkedList<>();
                    for (ComponentBase implementedComponent : implementedComponents) {
                        implemented.add(new ClassOrInterfaceType(implementedComponent.getName()));
                    }
                    cidecl.setImplements(implemented);
                }
            }
        }
    }
}
