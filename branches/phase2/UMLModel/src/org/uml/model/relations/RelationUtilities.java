package org.uml.model.relations;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Boris
 */
public class RelationUtilities {

    public static List<RelationBase> allRelations() {
        List<RelationBase> list = new LinkedList<>();
        list.add(new IsRelation());
        list.add(new ImplementsRelation());
        list.add(new AggregationRelation());
        list.add(new CompositionRelation());
        list.add(new UseRelation());
        return list;
    }
}
