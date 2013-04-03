/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

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
import org.openide.windows.WindowManager;
import org.uml.model.ClassDiagram;
import org.uml.model.RelationComponent;
import org.uml.visual.dialogs.ChangeRelationshipTypeDialog;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;

/**
 *
 * @author Uros
 */
public class ConnectionPopupMenuProvider implements PopupMenuProvider{
    
    private ConnectionWidget widget;
    ClassDiagram classDiagram;
    RelationComponent relationComponent;
    LabelWidget name;
    private JPopupMenu menu;
    private JMenuItem setName;
    private JMenuItem removeRelation;
    
    public ConnectionPopupMenuProvider(ConnectionWidget widget,ClassDiagram classDiagram,RelationComponent relationComponent,LabelWidget name) {
        this.widget = widget;
        this.classDiagram= classDiagram;
        this.relationComponent=relationComponent;
        this.name = name;
        menu=new JPopupMenu("Connection Menu");
        
        (setName = new JMenuItem("Change relation type")).addActionListener(changeRelationTypeListener);
        menu.add(setName);
        (removeRelation = new JMenuItem("Remove relation")).addActionListener(removeRelationListener);
        menu.add(removeRelation);
    }

    ActionListener changeRelationTypeListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
        ChangeRelationshipTypeDialog dialog = new ChangeRelationshipTypeDialog(null, relationComponent, classDiagram, true);
        dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
        dialog.setVisible(true);
        }
    };
    
    ActionListener removeRelationListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            widget.removeFromParent();
            classDiagram.getRelations().remove(relationComponent.getName());
        }
    };
    
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
    
}
