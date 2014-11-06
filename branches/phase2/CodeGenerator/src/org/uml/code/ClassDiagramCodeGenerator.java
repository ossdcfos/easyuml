/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.code;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.InterfaceComponent;
import org.uml.model.PackageComponent;
import org.uml.model.EnumComponent;
import org.uml.model.relations.RelationComponent;

/**
 * Main class for the start of code generating. Every component is being
 * processed and code is generated code for it.
 *
 * @author zoran
 */
public class ClassDiagramCodeGenerator implements CodeGenerator {

    private static ClassDiagramCodeGenerator instance;
    ClassDiagram classDiagram;
    HashMap<Class<?>, CodeGenerator> generators;

    /**
     * Constructor without parameters that instantiates and fills generators
     * field with needed classes.
     */
    public ClassDiagramCodeGenerator() {
        this.generators = new HashMap<Class<?>, CodeGenerator>();
        generators.put(ClassComponent.class, new ClassCodeGenerator());
        generators.put(InterfaceComponent.class, new InterfaceCodeGenerator());
        generators.put(EnumComponent.class, new EnumCodeGenerator());
    }

    /**
     * Used to instantiate and get this singleton's instance. Basic singleton
     * implementation method.
     *
     * @return ClassDiagramCodeGenerator shared instance
     */
    public static ClassDiagramCodeGenerator getInstance() {
        if (instance == null) {
            instance = new ClassDiagramCodeGenerator();
        }

        return instance;
    }

    /**
     * Sets class diagram that is currently worked on.
     *
     * @param classDiagram to be processed
     */
    public void setClassDiagram(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
    }

    /**
     * Generates code for every variable (Class, Interface, Enum) inside
     * previously set classDiagram.
     *
     * @return generated code for the Class diagram
     * @see ClassDiagram
     */
    @Override
    public String generateCode() {
        StringBuilder sb = new StringBuilder();

        for (ClassDiagramComponent comp : classDiagram.getComponents().values()) {
            CodeGenerator codeGen = generators.get(comp.getClass());
            codeGen.setClassDiagramComponent(comp);
            if (comp instanceof ClassComponent) {
                ((ClassCodeGenerator) codeGen).setRelevantRelations(getRelevantRelations(comp, classDiagram.getRelations()));
            }
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

    /**
     * Finds relations which source is equal to ClassDiagramComponent given, and
     * returns a List of them (relations).
     *
     * @param component whose relations need to be found
     * @param relations among components that need to be searched
     * @return List<RelationComponent> where component given is source
     */
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
