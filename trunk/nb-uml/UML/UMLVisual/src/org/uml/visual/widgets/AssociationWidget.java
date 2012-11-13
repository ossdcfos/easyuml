package org.uml.visual.widgets;

import com.sun.nio.sctp.Association;
import org.netbeans.api.visual.widget.Scene;

/**
 * This class represents a connection between two classes
 * http://bits.netbeans.org/dev/javadoc/org-netbeans-api-visual/org/netbeans/api/visual/widget/ConnectionWidget.html
 *
 * http://platform.netbeans.org/graph/common-usages.html // conenction anchors
 */
/**
 *
 * @author NUGS
 */
public class AssociationWidget {
    private Association association;
    private ClassWidget src;
    private ClassWidget trg;

    public AssociationWidget(Scene scene,Association association, ClassWidget src, ClassWidget trg) {
        this.association = association;
        this.src = src;
        this.trg = trg;
        //popup menu
        //getActions().addAction(ActionFactory.createPopupMenuAction(new NeuronConnectionPopupMenuProvider(src, trg)));
    }
    //needs implementation
    public void removeAssociation () {
        //trg.getNeuron().removeInputConnectionFrom(src.getNeuron());
    }
    
    public Association getAssociation() {
        return association;
    }

    public ClassWidget getSrc() {
        return src;
    }

    public ClassWidget getTrg() {
        return trg;
    }
    
    
}
