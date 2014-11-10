package org.uml.model.relations;

import java.util.Objects;
import org.uml.model.ClassComponent;
import org.uml.model.ComponentBase;
import org.uml.model.InterfaceComponent;

/**
 * Is relation in UML class diagrams. Describes relation used usually
 * when an object <i>extends</i> another one.
 *
 * @author "NUGS"
 * @see RelationComponent
 * @see HasRelationComponent
 * @see UseRelationComponent
 * @see ImplementsRelationComponent
 */
public class IsRelationComponent extends RelationComponent {

    /**
     * Returns the name of relation.
     *
     * @return "Is"
     */
    @Override
    public String toString() {
        return "Is";
    }

    @Override
    public boolean canConnect(ComponentBase source, ComponentBase target) {
        Class<?> sc = source.getClass();
        Class<?> tc = target.getClass();
        if(sc == ClassComponent.class && tc == ClassComponent.class) return true;
        if(sc == InterfaceComponent.class && tc == InterfaceComponent.class) return true;
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
