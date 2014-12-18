package org.uml.newcode.renaming;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;

/**
 *
 * @author Boris
 */
public class ComponentRenameTable implements PropertyChangeListener {

    public static HashMap<String, String> components = new HashMap<>();

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("name")){
            String oldName = (String)evt.getOldValue();
            String newName = (String)evt.getNewValue();
            components.put(oldName, newName);
        }
    }
    
}
