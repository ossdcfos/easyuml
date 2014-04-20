/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;
import org.uml.model.InterfaceComponent;
import org.uml.model.PackageComponent;
import org.uml.model.RelationComponent;

/**
 *
 * @author zoran
 */
public class ClassDiagramCodeGenerator implements CodeGenerator {

    private static ClassDiagramCodeGenerator instance;
    ClassDiagram classDiagram;
    HashMap<Class, CodeGenerator> generators;

    public ClassDiagramCodeGenerator() {
        this.generators = new HashMap<Class, CodeGenerator>();
        generators.put(ClassComponent.class, new ClassCodeGenerator());
        generators.put(InterfaceComponent.class, new InterfaceCodeGenerator());
        generators.put(EnumComponent.class, new EnumCodeGenerator());
    }

    public static ClassDiagramCodeGenerator getInstance() {
        if (instance == null) {
            instance = new ClassDiagramCodeGenerator();
        }

        return instance;
    }

    public void setClassDiagram(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
    }

    @Override
    public String generateCode() {
        StringBuilder sb = new StringBuilder();

        for (ClassDiagramComponent comp : classDiagram.getComponents().values()) {
            CodeGenerator codeGen = generators.get(comp.getClass());
            codeGen.setClassDiagramComponent(comp);
            if (comp instanceof ClassComponent) ((ClassCodeGenerator) codeGen).setRelevantRelations(getRelevantRelations(comp, classDiagram.getRelations()));
            String code = codeGen.generateCode();
            PackageComponent pc = comp.getParentPackage();
            String packName;
            if (pc == null) {
                packName = null;
            } else {
                packName = pc.getName();
            }
            FileWriter.getInstance().writeFiles(code, comp.getName(), packName);

            sb.append(code);
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public List<RelationComponent> getRelevantRelations(ClassDiagramComponent component, HashMap<String, RelationComponent> relations) {
        List<RelationComponent> relevantRelations = new LinkedList<RelationComponent>(); 
        for (RelationComponent rc : relations.values()) {
            if (rc.getSource().equals(component)) {
                relevantRelations.add(rc);
            }
        }
        return relevantRelations;
    }
    
    //this method must be implemented because of the CodeGenerator interface, although it is never used
    @Override
    public void setClassDiagramComponent(ClassDiagramComponent component) {
    }
    
    
}
