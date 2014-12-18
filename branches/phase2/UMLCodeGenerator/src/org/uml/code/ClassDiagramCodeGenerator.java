package org.uml.code;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.uml.model.components.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.components.EnumComponent;
import org.uml.model.relations.RelationBase;

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
        this.generators = new HashMap<>();
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

        for (ComponentBase comp : classDiagram.getComponents()) {
            CodeGenerator codeGen = generators.get(comp.getClass());
            codeGen.setComponent(comp);
            if (comp instanceof ClassComponent) {
                ((ClassCodeGenerator) codeGen).setRelevantRelations(getRelevantRelations(comp, classDiagram.getRelations()));
            }
            String code = codeGen.generateCode();

            String parentPackage = comp.getParentPackage();
            UMLFileWriter.getInstance().writeFile(comp.getName(), parentPackage, code);

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
    public List<RelationBase> getRelevantRelations(ComponentBase component, HashSet<RelationBase> relations) {
        List<RelationBase> relevantRelations = new LinkedList<>();
        for (RelationBase rc : relations) {
            if (rc.getSource().equals(component)) {
                relevantRelations.add(rc);
            }
        }
        return relevantRelations;
    }

    //this method must be implemented because of the CodeGenerator interface, although it is never used
    @Override
    public void setComponent(ComponentBase component) {
    }

}
