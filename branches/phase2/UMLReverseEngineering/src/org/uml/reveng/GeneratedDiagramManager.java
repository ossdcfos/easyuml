package org.uml.reveng;

import java.awt.Point;
import java.util.HashMap;
import org.uml.model.ClassDiagram;

/**
 * A singleton-type class that is used for interchanging generated Relation
 * components among objects. Also stores generated Class diagrams, and keeps
 * track and increments components's early position on scene, count of the
 * relations generated and a flag that is used to signal whether compilation
 * process is successful.
 *
 * @author Milan Djoric
 */
public class GeneratedDiagramManager {

    private static GeneratedDiagramManager instance;
    private ClassDiagram classDiagram;
    private HashMap<String, HashMap<String, Object>> hasRelationships;
    private HashMap<String, HashMap<String, Object>> usesRelationships;
    private HashMap<String, HashMap<String, Object>> isRelationships;
    private HashMap<String, HashMap<String, Object>> implementsRelationships;
    private int relationCounter;
    private Point componentPosition;
    private boolean zeroClassesGenerated;

    public boolean isZeroClassesGenerated() {
        return zeroClassesGenerated;
    }

    public void setZeroClassesGenerated(boolean zeroClassesGenerated) {
        this.zeroClassesGenerated = zeroClassesGenerated;
    }

    /**
     * Default parameterless constructor that initialises all necessary fields.
     *
     */
    private GeneratedDiagramManager() {
        classDiagram = new ClassDiagram();
        hasRelationships = new HashMap<>();
        usesRelationships = new HashMap<>();
        isRelationships = new HashMap<>();
        implementsRelationships = new HashMap<>();
        relationCounter = 0;
        componentPosition = new Point(50, 50);
    }

    /**
     * Resets all fields of this singleton to their default values.
     */
    public void clearContent() {
        instance = new GeneratedDiagramManager();
    }

    /**
     * Singleton-type getter
     *
     * @return instance of GeneratedDiagramManager
     */
    public static GeneratedDiagramManager getInstance() {
        if (instance == null) {
            instance = new GeneratedDiagramManager();
        }
        return instance;
    }

    public ClassDiagram getClassDiagram() {
        return classDiagram;
    }

    public void setClassDiagram(ClassDiagram cd) {
        classDiagram = cd;
    }

    public HashMap<String, HashMap<String, Object>> getHasRelationships() {
        return hasRelationships;
    }

    public void setHasRelationships(HashMap<String, HashMap<String, Object>> hasRelationships) {
        this.hasRelationships = hasRelationships;
    }

    public HashMap<String, HashMap<String, Object>> getUsesRelationships() {
        return usesRelationships;
    }

    public void setUsesRelationships(HashMap<String, HashMap<String, Object>> usesRelationships) {
        this.usesRelationships = usesRelationships;
    }

    public HashMap<String, HashMap<String, Object>> getIsRelationships() {
        return isRelationships;
    }

    public void setIsRelationships(HashMap<String, HashMap<String, Object>> isRelationships) {
        this.isRelationships = isRelationships;
    }

    public HashMap<String, HashMap<String, Object>> getImplementsRelationships() {
        return implementsRelationships;
    }

    public void setImplementsRelationships(HashMap<String, HashMap<String, Object>> implementsRelationships) {
        this.implementsRelationships = implementsRelationships;
    }

    public void incrementRelationCounter() {
        relationCounter++;
    }

    public void resetRelationCounter() {
        relationCounter = 0;
    }

    public int getRelationCounter() {
        return relationCounter;
    }

    /**
     * Creates the position coordinates of a component. Auto increments for each
     * successive component.
     *
     * @return Point object that consists of X and Y position on scene
     */
    public Point getNextComponentPosition() {
        Point oldPoint = new Point();
        if (componentPosition.getX() > 1000) {
            componentPosition.move(50, (int) componentPosition.getY() + 400);
        }
        oldPoint.x = (int) componentPosition.getX();
        oldPoint.y = (int) componentPosition.getY();
        componentPosition.translate(250, 0);
        return oldPoint;
    }
}
