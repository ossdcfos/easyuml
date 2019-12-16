package org.uml.filetype.cdg.renaming;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ComponentBase;

/**
 *
 * @author Boris Perović
 */
public class MyClassDiagramRenameTable implements PropertyChangeListener {

    MyComponentsRenameTable componentRenames;
    MyRelationsRenameTable relationRenames;

    public MyClassDiagramRenameTable(ClassDiagram classDiagram) {
        classDiagram.updateComponentPackages();
        this.componentRenames = new MyComponentsRenameTable(this, classDiagram);
        this.relationRenames = new MyRelationsRenameTable(this, classDiagram);
        
        classDiagram.addPropertyChangeListener(this);
    }

    public MyComponentsRenameTable getComponentRenames() {
        return componentRenames;
    }

    public MyRelationsRenameTable getRelationRenames() {
        return relationRenames;
    }
    
    public final void updateClassDiagram(ClassDiagram classDiagram) {
        classDiagram.updateComponentPackages();
        componentRenames.updateComponents(classDiagram);
        relationRenames.updateRelations(classDiagram);
        
        classDiagram.addPropertyChangeListener(this);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (prop.equals("ADD_COMPONENT")) {
            ComponentBase component = (ComponentBase) evt.getNewValue();
            componentRenames.addComponent(component);
        } else if (prop.equals("REMOVE_COMPONENT")) {
            ComponentBase component = (ComponentBase) evt.getNewValue();
            componentRenames.removeComponent(component);
        }
    }

    public void clear() {
        componentRenames.clear();
        relationRenames.clear();
    }
}
