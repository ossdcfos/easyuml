package org.uml.explorer;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.openide.util.WeakListeners;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ComponentBase;

/**
 *
 * @author Boris
 */
public class ClassDiagramChildrenFactory extends ChildFactory<ComponentBase> implements PropertyChangeListener {

    private final ClassDiagram classDiagram;

    public ClassDiagramChildrenFactory(ClassDiagram classDiagram) {
        this.classDiagram = classDiagram;
        this.classDiagram.addPropertyChangeListener(WeakListeners.propertyChange(this, this.classDiagram));
    }

    @Override
    protected boolean createKeys(List<ComponentBase> toPopulate) {
        if (classDiagram.getComponents() != null) {
            for (ComponentBase component : classDiagram.getComponents()) {
                toPopulate.add(component);
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(ComponentBase key) {
        return new ComponentNode(key);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if (null != evt.getPropertyName()) {
            switch (evt.getPropertyName()) {
                case "ADD":
                    refresh(true);
                    break;
                case "REMOVE":
                    refresh(true);
                    break;
            }
        }
    }
}
