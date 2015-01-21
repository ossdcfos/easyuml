package org.uml.model.relations;

//import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author Boris
 */
//@XStreamAlias("AggregationRelation")
public class AggregationRelation extends HasBaseRelation{

    public AggregationRelation() {
        super(Type.AGGREGATION);
    }
}
