/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
public abstract class MemberBasePopupProvider implements PopupMenuProvider {
        
    protected MemberWidgetBase widget;
    protected JPopupMenu menu;
    private JMenuItem deleteMember;
    
    protected MemberBasePopupProvider(MemberWidgetBase widget){
        this.widget = widget;
        String name = widget.getMember().getClass().getSimpleName();
        menu = new JPopupMenu(name+" Menu");
        
        addDelete();
    }
    
    protected final void addDelete(){
        String name = widget.getMember().getClass().getSimpleName();
        (deleteMember = new JMenuItem("Delete "+name)).addActionListener(removeWidgetListener);
        menu.add(deleteMember);
    }
    
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MemberBase member = widget.getMember();
            member.getDeclaringClass().removeMember(widget.getName());
            member.getDeclaringClass().removeMemberFromContainer(member);
            widget.getClassDiagramScene().removeObject(member);
            widget.removeFromParent();
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point localLocation) {
        return menu;
    }
    
}
