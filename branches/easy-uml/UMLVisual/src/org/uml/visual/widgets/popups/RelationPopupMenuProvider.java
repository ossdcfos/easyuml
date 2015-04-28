package org.uml.visual.widgets.popups;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassDiagram;
import org.uml.visual.widgets.relations.RelationBaseWidget;

/**
 *
 * @author Uros
 */
public class RelationPopupMenuProvider implements PopupMenuProvider {

    private RelationBaseWidget relationWidget;
    private JPopupMenu menu;
    private JMenuItem removeRelation;

    public RelationPopupMenuProvider(RelationBaseWidget relationWidget) {
        this.relationWidget = relationWidget;

        menu = new JPopupMenu("Connection Menu");

        (removeRelation = new JMenuItem("Remove relation")).addActionListener(removeRelationListener);
        menu.add(removeRelation);
    }

    ActionListener removeRelationListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClassDiagram classDiagram = relationWidget.getRelation().getSource().getParentDiagram();
            classDiagram.removeRelation(relationWidget.getRelation());
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
