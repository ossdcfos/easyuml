/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import org.openide.nodes.AbstractNode;
import org.openide.nodes.FilterNode;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.Member;

/**
 *
 * @author Jelena
 */
public class MemberNode extends AbstractNode {

    private Member member;

    public MemberNode(Member member) {
        this(member, new InstanceContent());
    }

    private MemberNode(Member member, InstanceContent content) {
        super(FilterNode.Children.LEAF, new AbstractLookup(content));
        content.add(this);

        this.member = member;
        this.setDisplayName(member.getName());
    }
    
}
