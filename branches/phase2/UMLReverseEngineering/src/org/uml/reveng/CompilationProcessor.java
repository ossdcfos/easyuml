package org.uml.reveng;

import componentOriginating.ComponentCreation;
import componentOriginating.RelationshipResolver;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import org.uml.model.components.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.EnumComponent;
import org.uml.model.components.InterfaceComponent;

/**
 * This class plays the most important role during the compilation process. It
 * analyzes every file that is being compiled, and in a manner of a side effect,
 * sends the file to further processing in order to get all the required data
 * from it.
 *
 * @author Milan Djoric
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("*")
public class CompilationProcessor extends AbstractProcessor {

    public static ClassDiagram generatedDiagram = new ClassDiagram();

//    public static List<ComponentBase> allFoundClasses = new ArrayList<>();

    public static HashMap<String, HashMap<String, Object>> hasRelationships = new HashMap<>();
    public static HashMap<String, HashMap<String, Object>> useRelationships = new HashMap<>();
    public static HashMap<String, HashMap<String, Object>> isRelationships = new HashMap<>();
    public static HashMap<String, HashMap<String, Object>> implementsRelationships = new HashMap<>();

    public static ClassComponent genClass = null;
    public static InterfaceComponent genInterface = null;
    public static EnumComponent genEnum = null;

    /**
     * Inherited method that handles every file that is being compiled. Also
     * sends it to further processing.
     *
     * @param annotations 
     * @param roundEnvironment
     * @return true if the compilation was successful
     */
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        //For each element (class) found inside compilation task
        for (Element e : roundEnvironment.getRootElements()) {
            //Creates a Top level component (Class, Enum or Interface component)
            ComponentCreation.createComponent(e);
            /*System.out.println(
             e.getKind() + " "
             + e.getModifiers() + " "
             + e.asType().getKind() + " "
             + e.getSimpleName() + "// "
             + e.getClass() + " "
             + e.toString() + " "
             + e.getEnclosingElement().getSimpleName());*/
            //Populates it based on data it contains
            ComponentCreation.populateComponent(e);
        }
        //When all classes are processed, fill in the adjecent Hash Maps
        GeneratedDiagramManager.getInstance().setClassDiagram(generatedDiagram);
        GeneratedDiagramManager.getInstance().setUsesRelationships(useRelationships);
        GeneratedDiagramManager.getInstance().setHasRelationships(hasRelationships);
        GeneratedDiagramManager.getInstance().setIsRelationships(isRelationships);
        GeneratedDiagramManager.getInstance().setImplementsRelationships(implementsRelationships);
        //Create Has and Use(s) relation components based on preocessed elements
//        RelationshipResolver.resolveRelationsHasAndUses();
        return true;
    }
}
