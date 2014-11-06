package org.uml.explorer;

import java.awt.Image;
import org.openide.nodes.AbstractNode;
import org.openide.nodes.FilterNode;
import org.openide.util.ImageUtilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.uml.model.members.*;

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
        setName(member.getName());
        setDisplayName(member.getName());
    }

    @Override
    public Image getIcon(int type) {
        if (member instanceof Field) {
            return ImageUtilities.loadImage("org/uml/explorer/icons/attributePublicIcon.png");
        }
        if (member instanceof Method) {
            return ImageUtilities.loadImage("org/uml/explorer/icons/methodPublicIcon.png");
        }
        if (member instanceof Constructor) {
            return ImageUtilities.loadImage("org/uml/explorer/icons/constructorPublicIcon.png");
        }
        if (member instanceof Literal) {
            return ImageUtilities.loadImage("org/uml/explorer/icons/literalIcon.png");
        }
        return super.getIcon(type);
    }

}
