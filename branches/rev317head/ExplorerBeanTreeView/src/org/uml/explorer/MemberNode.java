/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.explorer;

import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.FilterNode;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.Constructor;
import org.uml.model.Field;
import org.uml.model.Literal;
import org.uml.model.Member;
import org.uml.model.Method;

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

    @Override
    public Image getIcon(int type) {
        if (member instanceof Field) {
            return ImageUtilities.loadImage("org/uml/explorer/atributPublicIcon.png");
        }
        if (member instanceof Method) {
            return ImageUtilities.loadImage("org/uml/explorer/metodaPublicIcon.png");
        }
        if (member instanceof Constructor) {
            return ImageUtilities.loadImage("org/uml/explorer/konstruktorPublicIcon.png");
        }
        if (member instanceof Literal) {
            return ImageUtilities.loadImage("org/uml/explorer/literalIcon.png");
        }
        return super.getIcon(type);
    }
    
    
}
