package org.uml.model.relations;

/**
 * Aggregation has relation, subclassed from base relation.
 *
 * @author Boris Perović
 */
public class AggregationRelation extends HasBaseRelation {

    public AggregationRelation() {
        super(Type.AGGREGATION);
    }
}
