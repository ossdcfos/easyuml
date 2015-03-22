package org.uml.filetype.cdg.renaming;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import org.uml.model.components.ComponentBase;
import org.uml.model.members.MemberBase;

/**
 *
 * @author Boris Perović
 */
public class MyMembersRenameTable implements PropertyChangeListener {

    private final MyComponentsRenameTable parentTable;
    private final HashMap<String, String> memberRenames; // component -> (MembersNew->MembersOld)

    public MyMembersRenameTable(MyComponentsRenameTable parentTable, ComponentBase component) {
        this.parentTable = parentTable;
        this.memberRenames = new HashMap<>();

        for (MemberBase member : component.getMembers()) {
            member.addPropertyChangeListener(this);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (prop.equals("name") || prop.equals("type")) {
            MemberBase member = (MemberBase) evt.getSource();
            String oldValue = (String) evt.getOldValue();

            // Determine previous signature and the current signature
            String prevSignature;
            String currentSignature;
            if (prop.equals("name")) {
                prevSignature = member.deriveSignatureFromName(oldValue);
            } else {
                prevSignature = member.deriveSignatureFromType(oldValue);
            }
            currentSignature = member.getSignature();

            // Insert or update the tables based on current signature 
            if (!memberRenames.containsKey(prevSignature)) {
                // Inserting for the first time, on first rename
                memberRenames.put(currentSignature, prevSignature);
            } else {
                // Updating for second or later rename
                String originalSignature = memberRenames.remove(prevSignature);
                memberRenames.put(currentSignature, originalSignature);
            }
        }
    }

    public boolean contains(MemberBase member) {
        return memberRenames.containsKey(member.getSignature());
    }

    public String getOriginalSignature(MemberBase member) {
        return memberRenames.get(member.getSignature());
    }

    void updateMembers(ComponentBase component) {
        for (MemberBase member : component.getMembers()) {
            member.addPropertyChangeListener(this);
        }
    }

    void clear() {
        memberRenames.clear();
    }

//    public HashMap<String, String> createIfMissing(String componentSignature) {
//        // Create member rename table for component, if non-existant
//        if (!componentsToMembers.containsKey(componentSignature)) {
//            componentsToMembers.put(componentSignature, new HashMap<String, String>());
//        }
//        return componentsToMembers.get(componentSignature);
//    }
//
//    public void replaceComponentSignature(String oldComponentSignature, String newComponentSignature) {
//        if (componentsToMembers.containsKey(oldComponentSignature)) {
//            HashMap<String, String> membersForComponent = componentsToMembers.remove(oldComponentSignature);
//            componentsToMembers.put(newComponentSignature, membersForComponent);
//        } else {
//            createIfMissing(newComponentSignature);
//        }
//    }
}
