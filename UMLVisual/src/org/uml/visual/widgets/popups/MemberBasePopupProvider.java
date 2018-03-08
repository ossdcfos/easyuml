package org.uml.visual.widgets.popups;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.members.MemberBase;
import org.uml.visual.widgets.components.MemberContainerWidget;
import org.uml.visual.widgets.members.MemberWidgetBase;

/**
 *
 * @author Boris Perović
 */
public class MemberBasePopupProvider implements PopupMenuProvider {
        
    protected MemberWidgetBase widget;
    protected JPopupMenu menu;
    private JMenuItem moveUp,moveDown,deleteMember;
    
    public MemberBasePopupProvider(MemberWidgetBase widget){
        this.widget = widget;
        String name = widget.getMember().getClass().getSimpleName();
        menu = new JPopupMenu(name+" Menu");

        (moveUp = new JMenuItem("Move up")).addActionListener(moveUpWidgetListener);
        menu.add(moveUp);

        (moveDown = new JMenuItem("Move down")).addActionListener(moveDownWidgetListener);
        menu.add(moveDown);
        
        (deleteMember = new JMenuItem("Delete "+name)).addActionListener(removeWidgetListener);
        menu.add(deleteMember);
    }
    
    ActionListener moveUpWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Widget container = widget.getParentWidget();
            if (!(container instanceof MemberContainerWidget)) {
                return;                
            }            
            MemberBase member = widget.getMember();
            if (member.getDeclaringComponent().moveUpMember(member)) {
                MemberContainerWidget memberContainerWidget = (MemberContainerWidget)container;
                memberContainerWidget.moveUpMemberWidget(widget);
            }
        }
    };
    
    ActionListener moveDownWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Widget container = widget.getParentWidget();
            if (!(container instanceof MemberContainerWidget)) {
                return;                
            }
            MemberBase member = widget.getMember();
            if (member.getDeclaringComponent().moveDownMember(member)) {
                MemberContainerWidget memberContainerWidget = (MemberContainerWidget)container;
                memberContainerWidget.moveDownMemberWidget(widget);
            }
        }
    };    

    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MemberBase member = widget.getMember();
            member.getDeclaringComponent().removeMember(member);
            widget.getClassDiagramScene().removeObject(member);
            widget.removeFromParent();
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
        return menu;
    }
    
}
