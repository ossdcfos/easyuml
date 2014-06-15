/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
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
import javax.lang.model.element.ElementKind.*;
import javax.lang.model.element.TypeElement;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;
import org.uml.model.InterfaceComponent;

/**
 *
 * @author Milan Djoric
 */
@SupportedSourceVersion(SourceVersion.RELEASE_7)
@SupportedAnnotationTypes("*")
public class CompilationProcessor extends AbstractProcessor {

    public static ClassDiagram generatedDiagram = new ClassDiagram();
    
    public static List<ClassDiagramComponent> allFoundClasses = new ArrayList<ClassDiagramComponent>();
    
    public static HashMap<String, HashMap<String, Object>> hasRelationships = new HashMap<String, HashMap<String, Object>>();
    public static HashMap<String, HashMap<String, Object>> useRelationships = new HashMap<String, HashMap<String, Object>>();
    public static HashMap<String, HashMap<String, Object>> isRelationships = new HashMap<String, HashMap<String, Object>>();
    public static HashMap<String, HashMap<String, Object>> implementsRelationships = new HashMap<String, HashMap<String, Object>>();
    
    public static ClassComponent genClass = null;
    public static InterfaceComponent genInterface = null;
    public static EnumComponent genEnum = null;

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnvironment) {
        for (Element e : roundEnvironment.getRootElements()) {
            ComponentCreation.createTopElementComponent(e);
            /*System.out.println(
                    e.getKind() + " "
                    + e.getModifiers() + " "
                    + e.asType().getKind() + " "
                    + e.getSimpleName() + "// "
                    + e.getClass() + " "
                    + e.toString() + " "
                    + e.getEnclosingElement().getSimpleName());*/
            ComponentCreation.populateTopElementComponent(e);
        }
        GeneratedDiagramManager.getDefault().setClassDiagram(generatedDiagram);
        GeneratedDiagramManager.getDefault().setUsesRelationships(useRelationships);
        GeneratedDiagramManager.getDefault().setHasRelationships(hasRelationships);
        GeneratedDiagramManager.getDefault().setIsRelationships(isRelationships);
        GeneratedDiagramManager.getDefault().setImplementsRelationships(implementsRelationships);
        RelationshipResolver.resolveRelationsHasAndUses();
        return true;
    }
}