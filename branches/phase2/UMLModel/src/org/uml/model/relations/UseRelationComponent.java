package org.uml.model.relations;

import java.util.Objects;
import org.uml.model.ClassComponent;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.InterfaceComponent;

/**
 * Use(s) relation in UML class diagrams. Describes relation
 * used usually when an object depends of another one - most often the "using"
 * end in a relationship will be aware of the other end it's using, but it's not
 * always the case the "used" end is aware who is using it. Implemented usually
 * when there is an input variable in a class' method which is type of another
 * class.
 *
 * @author "NUGS"
 * @see RelationComponent
 * @see HasRelationComponent
 * @see IsRelationComponent
 * @see ImplementsRelationComponent
 */
public class UseRelationComponent extends RelationComponent {

    private CardinalityEnum cardinalitySource;
    private CardinalityEnum cardinalityTarget;

    /**
     * Returns the name of relation
     *
     * @return "Use"
     */
    @Override
    public String toString() {
        return "Use";
    }

    public CardinalityEnum getCardinalitySource() {
        return cardinalitySource;
    }

    public void setCardinalitySource(CardinalityEnum cardinalitySource) {
        this.cardinalitySource = cardinalitySource;
    }

    public CardinalityEnum getCardinalityTarget() {
        return cardinalityTarget;
    }

    public void setCardinalityTarget(CardinalityEnum cardinalityTarget) {
        this.cardinalityTarget = cardinalityTarget;
    }

    @Override
    public boolean canConnect(ClassDiagramComponent source, ClassDiagramComponent target) {
        Class<?> sc = source.getClass();
        Class<?> tc = target.getClass();
        if(sc == ClassComponent.class || sc == InterfaceComponent.class) return true;
        return false;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.source);
        hash = 53 * hash + Objects.hashCode(this.target);
        hash = hash + Objects.hashCode(this.name);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj)) return false;
        UseRelationComponent other = (UseRelationComponent) obj;
        if (!Objects.equals(this.name, other.name)) {
            return false;
        }
        return true;
    }
}
