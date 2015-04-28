package org.uml.model.relations;

/**
 * Composition has relation, subclassed from base relation.
 * @author Boris Perović
 */
public class CompositionRelation extends HasBaseRelation {

    public CompositionRelation() {
        super(Type.COMPOSITION);
    }
}
