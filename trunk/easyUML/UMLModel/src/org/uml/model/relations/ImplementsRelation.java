package org.uml.model.relations;

import java.util.Objects;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.InterfaceComponent;

/**
 * Implements relation in UML class diagrams. 
 * Describes relation used when an object <i>implements</i> another objects
 * behaviour e.g. a class implements an interface.
 *
 * @author NUGS
 */
public class ImplementsRelation extends RelationBase {

    /**
     * Returns the name of the relation.
     *
     * @return "Implements"
     */
    @Override
    public String toString() {
        return "Implements";
    }

    @Override
    public boolean canConnect(ComponentBase source, ComponentBase target) {
        Class<?> sc = source.getClass();
        Class<?> tc = target.getClass();
        if(source == target) return false;
        if(sc == ClassComponent.class && tc == InterfaceComponent.class) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.source);
        hash = 17 * hash + Objects.hashCode(this.target);
        return hash;
    }

    // Implements relation is the same if source and target are the same.
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RelationBase other = (RelationBase) obj;
        if (!Objects.equals(this.source, other.source)) {
            return false;
        }
        if (!Objects.equals(this.target, other.target)) {
            return false;
        }
        return true;
    }    
}
