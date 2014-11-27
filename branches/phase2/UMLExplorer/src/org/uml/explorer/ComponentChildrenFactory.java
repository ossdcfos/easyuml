package org.uml.explorer;

import java.util.Iterator;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.uml.model.components.ComponentBase;
import org.uml.model.members.MemberBase;

/**
 *
 * @author Boris
 */
public class ComponentChildrenFactory extends ChildFactory<MemberBase> {

    private ComponentBase component;

    public ComponentChildrenFactory(ComponentBase component) {
        this.component = component;
    }

    @Override
    protected boolean createKeys(List<MemberBase> toPopulate) {
        if (component.getMembers() != null) {
            Iterator<java.util.Map.Entry<String, MemberBase>> it = component.getMembers().entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry<String, MemberBase> pairs = it.next();
                MemberBase member = pairs.getValue();
                toPopulate.add(member);
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(MemberBase key) {
        return new MemberNode(key);
    }

}
