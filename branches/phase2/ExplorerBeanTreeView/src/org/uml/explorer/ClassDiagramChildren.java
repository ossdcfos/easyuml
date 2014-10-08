/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import java.util.ArrayList;
import java.util.Iterator;
import org.openide.nodes.Children;
import org.openide.nodes.Node;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassDiagramComponent;

/**
 *
 * @author Jelena
 */
public class ClassDiagramChildren  extends Children.Keys<Object> {
    
    private ClassDiagram classDiagram;
    
    public ClassDiagramChildren(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
    }
    
    @Override
    protected Node[] createNodes(Object object) {
        if(object instanceof ClassDiagramComponent){                
            ClassDiagramComponent classDiagramComponent = (ClassDiagramComponent) object;           
            ClassDiagramComponentNode classDiagramComponentNode = new ClassDiagramComponentNode(classDiagramComponent); 
            classDiagramComponentNode.setName(classDiagramComponent.getName());
            return new Node[] { classDiagramComponentNode }; 
        }else { 
            return new Node[] {};
        }
    }
    
    @Override
    protected void addNotify() {
        super.addNotify();
        ArrayList<Object> keys = new ArrayList<Object>();
        if(classDiagram.getComponents()!=null){
            Iterator<java.util.Map.Entry<String,ClassDiagramComponent>> it = classDiagram.getComponents().entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry<String,ClassDiagramComponent> pairs = (java.util.Map.Entry<String,ClassDiagramComponent>) it.next();
                ClassDiagramComponent component = pairs.getValue();
                keys.add(component);
            }
        }
        
        setKeys(keys);
    }
}
