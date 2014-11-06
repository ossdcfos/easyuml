/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.uml.model.ClassDiagram;
import org.uml.model.ClassDiagramComponent;

/**
 *
 * @author Boris
 */
public class ClassDiagramChildrenFactory extends ChildFactory<ClassDiagramComponent> implements NodeListener 
{

    private ClassDiagram classDiagram;

    public ClassDiagramChildrenFactory(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
    }

    @Override
    protected boolean createKeys(List<ClassDiagramComponent> toPopulate) {
        if (classDiagram.getComponents() != null) {
            Iterator<java.util.Map.Entry<String, ClassDiagramComponent>> it = classDiagram.getComponents().entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry<String, ClassDiagramComponent> pairs = it.next();
                ClassDiagramComponent component = pairs.getValue();
                toPopulate.add(component);
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(ClassDiagramComponent key) {
        Node node = new ClassDiagramComponentNode(key);
        node.addNodeListener(this);
        return node;
    }
    
    @Override
    public void nodeDestroyed(NodeEvent ev) {
        refresh(true);
    }

    @Override
    public void childrenAdded(NodeMemberEvent ev) {}
    @Override
    public void childrenRemoved(NodeMemberEvent ev) {}
    @Override
    public void childrenReordered(NodeReorderEvent ev) {}
    @Override
    public void propertyChange(PropertyChangeEvent evt) {}
}
