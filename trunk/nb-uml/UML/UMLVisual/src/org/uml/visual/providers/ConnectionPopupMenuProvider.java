/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.providers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;

/**
 *
 * @author Uros
 */
public class ConnectionPopupMenuProvider implements PopupMenuProvider{
    
    private ConnectionWidget widget;
    private JPopupMenu menu;
    private JMenuItem setName;

    public ConnectionPopupMenuProvider(ConnectionWidget widget) {
        this.widget = widget;
        menu=new JPopupMenu("Connection Menu");
        
        (setName = new JMenuItem("Set name")).addActionListener(setNameListener);
        menu.add(setName);
    }

    ActionListener setNameListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
        LabelWidget name = new LabelWidget (widget.getScene(), "Relation name");
        name.getActions().addAction(ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction()));
        name.setOpaque (true);
        widget.addChild(name);
        widget.setConstraint (name, LayoutFactory.ConnectionWidgetLayoutAlignment.TOP_CENTER, 0.5f);
        widget.getScene().validate();
        }
    };
    
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
    
}
