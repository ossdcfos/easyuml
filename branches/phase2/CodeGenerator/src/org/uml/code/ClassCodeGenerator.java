package org.uml.code;

import java.util.Iterator;
import java.util.List;
import org.uml.model.CardinalityEnum;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.HasRelationComponent;
import org.uml.model.ImplementsRelationComponent;
import org.uml.model.InterfaceComponent;
import org.uml.model.IsRelationComponent;
import org.uml.model.PackageComponent;
import org.uml.model.RelationComponent;
import org.uml.model.UseRelationComponent;

/**
 *
 * @author zoran
 */
public class ClassCodeGenerator implements CodeGenerator {

    ClassComponent classComponent;
    List<RelationComponent> relevantRelations;
    
    public ClassCodeGenerator() {
    }

    public void setClassComponent(ClassComponent classComponent) {
        this.classComponent = classComponent;
    }

    public void setRelevantRelations(List<RelationComponent> relevantRelations) {
        this.relevantRelations = relevantRelations;
    }

    @Override
    public String generateCode() {
        String code = "";
        PackageComponent pc= classComponent.getParentPackage();
        if (pc != null && !pc.getName().equals("")) {
            String pack = pc.getName();
            code += "package " + pack + "; \n";
        }
        String abstractModifierStr = "";
        if (classComponent.isIsAbstract()) {
            abstractModifierStr = "abstract";
        }
        String header = "public " + abstractModifierStr + " class " + classComponent.getName();
        String extendsClass = getClassThatIsExtended(relevantRelations);
        if (extendsClass != null) {
            header += " extends " + extendsClass;
        }
        String implementedClasses = getClassesThatAreImplemented(relevantRelations);
        if (!implementedClasses.equals("")) {
            header += " implements " + implementedClasses;
        }
        header += " { ";
        ConstructorCodeGenerator ccg = new ConstructorCodeGenerator(classComponent.getConstructors());
        String constructors = ccg.generateCode();
        FieldCodeGenerator fcg = new FieldCodeGenerator(classComponent.getFields());
        String fields = fcg.generateCode();
        fields += getFieldsFromUseRelations(relevantRelations);
        fields += getFieldsFromHasRelations(relevantRelations);
        MethodCodeGenerator mcg = new MethodCodeGenerator(classComponent.getMethods());
        String methods = mcg.generateCode();
        String methodsFromInterfaces = getMethodsFromInterfaces(relevantRelations);
        if (!methodsFromInterfaces.equals("")) {
            methods += methodsFromInterfaces;
        }
        String end = "\n }";
        //classComponent.get

        code += header + "\n";
        code += constructors + "\n";
        code += fields + "\n";
        code += methods + "\n";
        code += end;
        return code;
    }

    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
        classComponent = (ClassComponent) component;
    }
    
    public String getClassThatIsExtended(List<RelationComponent> relations) {
        String extendedClassName = null;
        for (int i = 0; i < relations.size(); i++) {
            if (relations.get(i) instanceof IsRelationComponent) {
                extendedClassName = ((ClassComponent) relations.get(i).getTarget()).getName();
            }
        }
        return extendedClassName;        
    }
    
    public String getClassesThatAreImplemented(List<RelationComponent> relations) {
        String implementedClasses = "";
        for (int i = 0; i < relations.size(); i++) {
            if (relations.get(i) instanceof ImplementsRelationComponent) {
                implementedClasses += relations.get(i).getTarget().getName() + ", ";
            }
        }
        if (!implementedClasses.equals("")) {
            implementedClasses = implementedClasses.substring(0, implementedClasses.length() - 2);
        }
        return implementedClasses;
    }
    
    public String getMethodsFromInterfaces(List<RelationComponent> relations) {
        String methods = "";
        for (int i = 0; i < relations.size(); i++) {
            if (relations.get(i) instanceof ImplementsRelationComponent) {
                MethodCodeGenerator generator = new MethodCodeGenerator(((InterfaceComponent) relations.get(i).getTarget()).getMethods());
                methods += generator.generateCode();
            }
        }
        return methods;
    }
    public String getFieldsFromUseRelations(List<RelationComponent> relations) {
        String fields = "";
        for (int i = 0; i < relations.size(); i++) {
            if (relations.get(i) instanceof UseRelationComponent) {
                String fieldType = relations.get(i).getTarget().getName();
                String fieldName = (fieldType.substring(0, 1)).toLowerCase() + fieldType.substring(1, fieldType.length());
                fields += "private " + fieldType + " " + fieldName + ";\n"; 
            }
        }
        return fields;
    }
    
    public String getFieldsFromHasRelations(List<RelationComponent> relations) {
        String fields = "";
        for (int i = 0; i < relations.size(); i++) {
            if (relations.get(i) instanceof HasRelationComponent) {
                HasRelationComponent hasRelation = (HasRelationComponent) relations.get(i);
                String fieldType = hasRelation.getTarget().getName();
                String fieldName = (fieldType.substring(0, 1)).toLowerCase() + fieldType.substring(1, fieldType.length());
                if (hasRelation.getCardinalityTarget().equals(CardinalityEnum.One2Many) || hasRelation.getCardinalityTarget().equals(CardinalityEnum.Zero2Many)) {
                    String field = "private " + hasRelation.getCollectionType() + "<" + hasRelation.getTarget().getName() + "> " + fieldName + "s;\n";
                    fields += field;
                }else {
                    String field = "private " + fieldType + " " + fieldName + ";\n";
                    fields += field;
                }
            }
        }
        return fields;
    }
}
