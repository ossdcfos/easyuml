package org.uml.visual.widgets.popups;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.ConnectionWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.relations.RelationBase;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author Uros
 */
public class RelationPopupMenuProvider implements PopupMenuProvider {

    ClassDiagramScene cdScene;
    RelationBase relationComponent;
    private JPopupMenu menu;
    private JMenuItem removeRelation;

    public RelationPopupMenuProvider(ConnectionWidget widget, RelationBase relationComponent) {
        cdScene = (ClassDiagramScene)widget.getScene();
       
        this.relationComponent = relationComponent;
        menu = new JPopupMenu("Connection Menu");

        (removeRelation = new JMenuItem("Remove relation")).addActionListener(removeRelationListener);
        menu.add(removeRelation);
    }

    ActionListener removeRelationListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            cdScene.removeEdge(relationComponent);
            cdScene.getClassDiagram().removeRelation(relationComponent);
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
