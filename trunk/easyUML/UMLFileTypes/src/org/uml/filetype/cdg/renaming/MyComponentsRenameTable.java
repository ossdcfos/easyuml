package org.uml.filetype.cdg.renaming;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ComponentBase;

/**
 *
 * @author Boris Perović
 */
public class MyComponentsRenameTable implements PropertyChangeListener {

    private final MyClassDiagramRenameTable parentTable;
    private final HashMap<String, String> componentRenames;
    private final HashMap<String, MyMembersRenameTable> componentMemberRenames;

    public MyComponentsRenameTable(MyClassDiagramRenameTable parentTable, ClassDiagram classDiagram) {
        this.parentTable = parentTable;
        this.componentRenames = new HashMap<>();
        this.componentMemberRenames = new HashMap<>();

        for (ComponentBase component : classDiagram.getComponents()) {
            addComponent(component);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (prop.equals("name") || prop.equals("parentPackage")) {
            ComponentBase component = (ComponentBase) evt.getSource();
            String oldValue = (String) evt.getOldValue();

            // Determine previous signature and the current signature
            String prevSignature;
            String currentSignature;
            if (prop.equals("name")) {
                prevSignature = component.deriveSignatureFromNewName(oldValue);
            } else {
                prevSignature = component.deriveSignatureFromNewPackage(oldValue);
            }
            currentSignature = component.getSignature();

            // Insert or update the tables based on current signature 
            if (!componentRenames.containsKey(prevSignature)) {
                // Inserting for the first time, on first rename
                componentRenames.put(currentSignature, prevSignature);
                componentMemberRenames.put(currentSignature, new MyMembersRenameTable(this, component));
            } else {
                // Updating for second or later rename
                String originalSignature = componentRenames.remove(prevSignature);
                componentRenames.put(currentSignature, originalSignature);

                MyMembersRenameTable memberRenameTable = componentMemberRenames.remove(prevSignature);
                componentMemberRenames.put(currentSignature, memberRenameTable);
            }
        }
    }

    public boolean contains(ComponentBase component) {
        return componentRenames.containsKey(component.getSignature());
    }

    public String getOriginalSignature(ComponentBase component) {
        return componentRenames.get(component.getSignature());
    }

    public MyMembersRenameTable getMembersRenameTable(ComponentBase component) {
        return componentMemberRenames.get(component.getSignature());
    }

    final void addComponent(ComponentBase component) {
        MyMembersRenameTable memberRenames = new MyMembersRenameTable(this, component);
        componentMemberRenames.put(component.getSignature(), memberRenames);

        component.addPropertyChangeListener(this);
    }

    void removeComponent(ComponentBase component) {
        componentRenames.remove(component.getSignature());
        componentMemberRenames.remove(component.getSignature());
    }

    void updateComponents(ClassDiagram classDiagram) {
        for (ComponentBase component : classDiagram.getComponents()) {
            String signature = component.getSignature();
            if (!componentMemberRenames.containsKey(signature)) {
                addComponent(component);
            } else {
                MyMembersRenameTable memberRenames = componentMemberRenames.get(signature);
                component.addPropertyChangeListener(this);
                memberRenames.updateMembers(component);
            }
        }
    }

    void clear() {
        componentRenames.clear();
        for (Map.Entry<String, MyMembersRenameTable> entry : componentMemberRenames.entrySet()) {
            MyMembersRenameTable membersRenameTable = entry.getValue();
            membersRenameTable.clear();
        }
    }
}
