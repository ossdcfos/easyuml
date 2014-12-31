package org.uml.newcode.renaming;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import org.uml.model.components.ComponentBase;

/**
 *
 * @author Boris
 */
public class ComponentRenameTable implements PropertyChangeListener {

    public static HashMap<String, String> components = new HashMap<>();

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        if (prop.equals("name") || prop.equals("parentPackage")) {
            ComponentBase component = (ComponentBase) evt.getSource();
            String oldValue = (String) evt.getOldValue();
            String newValue = (String) evt.getNewValue();
            String oldSignature;
            String newReplacementSignature;
            if(prop.equals("name")){
                oldSignature = component.deriveSignatureFromName(oldValue);
                newReplacementSignature = component.deriveSignatureFromName(newValue);
            } else {
                oldSignature = component.deriveSignatureFromPackage(oldValue);
                newReplacementSignature = component.deriveSignatureFromPackage(newValue);
            }

            if (components.containsKey(oldSignature)) {
                String originalSignature = components.get(oldSignature);
                components.remove(oldSignature);
                components.put(newReplacementSignature, originalSignature);
            } else {
                components.put(newReplacementSignature, oldSignature);
            }

            if (MemberRenameTable.members.containsKey(oldSignature)) {
                HashMap<String, String> membersForComponent = MemberRenameTable.members.remove(oldSignature);
                MemberRenameTable.members.put(newReplacementSignature, membersForComponent);
            }
        }
    }

    public static void clear() {
        components.clear();
    }
}
