package org.uml.newcode.renaming;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import org.uml.model.members.MemberBase;

/**
 *
 * @author Boris
 */
public class MemberRenameTable implements PropertyChangeListener {

    public static HashMap<String, HashMap<String, String>> members = new HashMap<>(); // component -> (Membersold->MembersNew)

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (prop.equals("name") || prop.equals("type")) {
            MemberBase member = (MemberBase) evt.getSource();
            String oldValue = (String) evt.getOldValue();
            String newValue = (String) evt.getNewValue();
            
            String oldSignature;
            String newReplacementSignature;
            if (prop.equals("name")) {
                oldSignature = member.deriveSignatureFromName(oldValue);
                newReplacementSignature = member.deriveSignatureFromName(newValue);
            } else {
                oldSignature = member.deriveSignatureFromType(oldValue);
                newReplacementSignature = member.deriveSignatureFromType(newValue);
            }

            String parentSignature = member.getDeclaringComponent().getSignature();
            if (!members.containsKey(parentSignature)) {
                members.put(parentSignature, new HashMap<String, String>());
            }
            HashMap<String, String> oldToNew = members.get(parentSignature);
            if (oldToNew.containsKey(oldSignature)) {
                String originalSignature = oldToNew.get(oldSignature);
                oldToNew.remove(oldSignature);
                oldToNew.put(newReplacementSignature, originalSignature);
            } else {
                oldToNew.put(newReplacementSignature, oldSignature);
            }
        }
    }

    public static void clear() {
        for (HashMap<String, String> memberMap : members.values()) {
            memberMap.clear();
        }
    }
}
