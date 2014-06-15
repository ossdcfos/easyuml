/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.reveng;

import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import org.uml.model.ClassDiagram;

/**
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
   
    private GeneratedDiagramManager() {
        classDiagram = new ClassDiagram();
        hasRelationships = new HashMap<String, HashMap<String, Object>>();
        usesRelationships = new HashMap<String, HashMap<String, Object>>();
        isRelationships = new HashMap<String, HashMap<String, Object>>();
        implementsRelationships = new HashMap<String, HashMap<String, Object>>();
        relationCounter = 0;
        componentPosition = new Point(50, 50);
    }

    public void clearContent() {
        instance = new GeneratedDiagramManager();
    }

    public static GeneratedDiagramManager getDefault() {
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

    public Point getComponentPosition() {
        Point oldPoint = new Point();
        if (componentPosition.getX() > 1000) {
            componentPosition.move(50, (int) componentPosition.getY() + 800);
        }
        oldPoint.x = (int) componentPosition.getX();
        oldPoint.y = (int) componentPosition.getY();
        componentPosition.translate(250, 0);
        return oldPoint;
    }
}
