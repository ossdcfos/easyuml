package org.uml.visual.widgets.providers.popups;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.members.MemberBase;
import org.uml.visual.widgets.members.MemberWidgetBase;

/**
 *
 * @author Boris
 */
public class MemberBasePopupProvider implements PopupMenuProvider {
        
    protected MemberWidgetBase widget;
    protected JPopupMenu menu;
    private JMenuItem deleteMember;
    
    public MemberBasePopupProvider(MemberWidgetBase widget){
        this.widget = widget;
        String name = widget.getMember().getClass().getSimpleName();
        menu = new JPopupMenu(name+" Menu");
        
        (deleteMember = new JMenuItem("Delete "+name)).addActionListener(removeWidgetListener);
        menu.add(deleteMember);
    }
    
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MemberBase member = widget.getMember();
            member.getDeclaringComponent().removeComponent(widget.getMember());
            member.getDeclaringComponent().removeMemberFromContainer(member);
            widget.getClassDiagramScene().removeObject(member);
            widget.removeFromParent();
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
        return menu;
    }
    
}
