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
import static org.openide.nodes.Node.PROP_DISPLAY_NAME;
import org.openide.nodes.NodeEvent;
import org.openide.nodes.NodeListener;
import org.openide.nodes.NodeMemberEvent;
import org.openide.nodes.NodeReorderEvent;
import org.uml.model.ClassDiagram;
import org.uml.model.ComponentBase;

/**
 *
 * @author Boris
 */
public class ClassDiagramChildrenFactory extends ChildFactory<ComponentBase> implements NodeListener 
{

    private ClassDiagram classDiagram;

    public ClassDiagramChildrenFactory(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
    }

    @Override
    protected boolean createKeys(List<ComponentBase> toPopulate) {
        if (classDiagram.getComponents() != null) {
            Iterator<java.util.Map.Entry<String, ComponentBase>> it = classDiagram.getComponents().entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry<String, ComponentBase> pairs = it.next();
                ComponentBase component = pairs.getValue();
                toPopulate.add(component);
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(ComponentBase key) {
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
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals(PROP_DISPLAY_NAME)){
            System.out.println("Changed name to: "+evt.getNewValue());
        }
    }
}
