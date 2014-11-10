/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import java.util.Iterator;
import java.util.List;
import org.openide.nodes.ChildFactory;
import org.openide.nodes.Node;
import org.uml.model.ComponentBase;
import org.uml.model.members.Member;

/**
 *
 * @author Boris
 */
public class ClassDiagramComponentChildrenFactory extends ChildFactory<Member> {

    private ComponentBase component;

    public ClassDiagramComponentChildrenFactory(ComponentBase component) {
        this.component = component;
    }

    @Override
    protected boolean createKeys(List<Member> toPopulate) {
        if (component.getMembers() != null) {
            Iterator<java.util.Map.Entry<String, Member>> it = component.getMembers().entrySet().iterator();
            while (it.hasNext()) {
                java.util.Map.Entry<String, Member> pairs = it.next();
                Member member = pairs.getValue();
                toPopulate.add(member);
            }
        }
        return true;
    }

    @Override
    protected Node createNodeForKey(Member key) {
        return new MemberNode(key);
    }

}
