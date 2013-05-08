/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import org.openide.nodes.Children;
import org.openide.nodes.Index;
import org.openide.nodes.Node;
import org.uml.model.ClassComponent;
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
            ClassDiagramComponentNode classComponentNode = new ClassDiagramComponentNode(classDiagramComponent); 
            classComponentNode.setName(classDiagramComponent.getName());
            return new Node[] { classComponentNode }; 
        }else { 
            return new Node[] {  };
        }
    }
    
    @Override
    protected void addNotify() {
        super.addNotify();
        ArrayList<Object> keys = new ArrayList<Object>();
        if(classDiagram.getComponents()!=null){
            Iterator it = classDiagram.getComponents().entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry pairs = (java.util.Map.Entry) it.next();
                ClassDiagramComponent component = (ClassDiagramComponent) pairs.getValue();
                keys.add(component);
            }
        }
        
        setKeys(keys); 
        
    }
}
