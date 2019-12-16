package org.uml.visual.widgets.popups;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.uml.visual.widgets.components.ComponentWidgetBase;

/**
 *
 * @author Philippe-Henri Gosselin
 */
public abstract class ComponentPopupMenuProvider implements PopupMenuProvider {
    
    private ComponentWidgetBase widget;
    private JMenuItem moveToFront;
    private JMenuItem moveToBack;
    
    ComponentPopupMenuProvider(ComponentWidgetBase widget) {
        this.widget = widget;
    }
    
    void addZMenuItems(JPopupMenu menu) {
        (moveToFront = new JMenuItem("Move to front")).addActionListener(moveToFrontListener);
        menu.add(moveToFront);
        (moveToBack = new JMenuItem("Move to back")).addActionListener(moveToBackListener);
        menu.add(moveToBack);        
    }
   
    ActionListener moveToFrontListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            widget.bringToFront();
            widget.getClassDiagramScene().updateComponentsZOrder();
        }
    };    
    ActionListener moveToBackListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            widget.bringToBack();
            widget.getClassDiagramScene().updateComponentsZOrder();
        }
    };     
}
