/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers.popups;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.windows.WindowManager;
import org.uml.model.ClassDiagram;
import org.uml.model.relations.RelationComponent;
import org.uml.visual.dialogs.ChangeRelationTypeDialog;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author Uros
 */
public class ConnectionPopupMenuProvider implements PopupMenuProvider {

    private ConnectionWidget widget;
    ClassDiagramScene cdScene;
    RelationComponent relationComponent;
    LabelWidget name;
    private JPopupMenu menu;
    private JMenuItem setName;
    private JMenuItem removeRelation;

    public ConnectionPopupMenuProvider(ConnectionWidget widget, RelationComponent relationComponent) {
        this.widget = widget;
        this.cdScene = (ClassDiagramScene)widget.getScene();
       
        this.relationComponent = relationComponent;
        menu = new JPopupMenu("Connection Menu");

        (setName = new JMenuItem("Change relation type")).addActionListener(changeRelationTypeListener);
        menu.add(setName);
        (removeRelation = new JMenuItem("Remove relation")).addActionListener(removeRelationListener);
        menu.add(removeRelation);
    }

    ActionListener changeRelationTypeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ChangeRelationTypeDialog dialog = new ChangeRelationTypeDialog(null, relationComponent, cdScene.getClassDiagram(), widget, true);
            dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            dialog.setTitle("Change relation type");
            dialog.setVisible(true);
        }
    };

    ActionListener removeRelationListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            cdScene.removeEdge(relationComponent);
            cdScene.getClassDiagram().getRelations().remove(relationComponent.getName());
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }

}
