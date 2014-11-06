/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.classDiagramManager;

import java.util.HashMap;
import org.uml.model.ClassDiagram;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author stefanpetrovic
 */
public class ClassDiagramManager {
    
    private static ClassDiagramManager instance;
    private HashMap<ClassDiagram, ClassDiagramScene> diagramSceneMapping;
    
    private ClassDiagramManager() {
        diagramSceneMapping = new HashMap<ClassDiagram, ClassDiagramScene>();
    }
    
    public static ClassDiagramManager getDefault() {
        if (instance == null) {
            instance = new ClassDiagramManager();
        }
        return instance;
    }
    
    public void addScene(ClassDiagramScene scene) {
        ClassDiagram diagram = scene.getClassDiagram();
        diagramSceneMapping.put(diagram, scene);
    }
    
    public ClassDiagramScene getSceneForDiagram(ClassDiagram diagram) {
        ClassDiagramScene scene = null;
        if (diagramSceneMapping.containsKey(diagram)) {
            scene = diagramSceneMapping.get(diagram);
        }
        return scene;
    }
    
}
