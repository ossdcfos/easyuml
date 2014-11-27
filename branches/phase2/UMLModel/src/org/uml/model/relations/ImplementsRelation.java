package org.uml.model.relations;

import java.util.Objects;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.InterfaceComponent;

/**
 * Implements relation in UML class diagrams. Describes
 * relation used usually when an object <i>implements</i> another objects
 * behavior; fields, methods, etc.
 *
 * @author "NUGS"
 * @see RelationBase
 * @see HasRelationComponent
 * @see IsRelationComponent
 * @see UseRelationComponent
 */
public class ImplementsRelation extends RelationBase {

    /**
     * Returns the name of relation.
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
        if(sc == ClassComponent.class && tc == InterfaceComponent.class) return true;
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.source);
        hash = 53 * hash + Objects.hashCode(this.target);
        return hash;
    }
    
}
