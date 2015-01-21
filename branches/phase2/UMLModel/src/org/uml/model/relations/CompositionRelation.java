package org.uml.model.relations;

//import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 *
 * @author Boris
 */
//@XStreamAlias("CompositionRelation")
public class CompositionRelation extends HasBaseRelation {

    public CompositionRelation() {
        super(Type.COMPOSITION);
    }
}
