package org.uml.filetype.cdg.renaming;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import org.uml.model.ClassDiagram;
import org.uml.model.relations.HasBaseRelation;
import org.uml.model.relations.RelationBase;

/**
 *
 * @author Boris
 */
public class MyRelationsRenameTable implements PropertyChangeListener {

    private final MyClassDiagramRenameTable parentTable;
    private final HashMap<String, String> relationRenames;

    public MyRelationsRenameTable(MyClassDiagramRenameTable parentTable, ClassDiagram classDiagram) {
        this.parentTable = parentTable;
        this.relationRenames = new HashMap<>();

        for (RelationBase relation : classDiagram.getRelations()) {
            relation.addPropertyChangeListener(this);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (prop.equals("name") && evt.getSource() instanceof HasBaseRelation) {
            HasBaseRelation relation = (HasBaseRelation) evt.getSource();
            String oldValue = (String) evt.getOldValue();

            // Determine previous signature and the current signature
            String prevSignature = relation.deriveFieldSignatureFromName(oldValue);
            String currentSignature = relation.getFieldSignature();

            // Insert or update the tables based on current signature 
            if (!relationRenames.containsKey(prevSignature)) {
                // Inserting for the first time, on first rename
                relationRenames.put(currentSignature, prevSignature);
            } else {
                // Updating for second or later rename
                String originalSignature = relationRenames.remove(prevSignature);
                relationRenames.put(currentSignature, originalSignature);
            }
        }
    }

    public boolean contains(HasBaseRelation hasRelation) {
        return relationRenames.containsKey(hasRelation.getFieldSignature());
    }

    public String getOriginalSignature(HasBaseRelation hasRelation) {
        return relationRenames.get(hasRelation.getFieldSignature());
    }

    void updateRelations(ClassDiagram classDiagram) {
        for (RelationBase relation : classDiagram.getRelations()) {
            relation.addPropertyChangeListener(this);
        }
    }

    void clear() {
        relationRenames.clear();
    }
}
