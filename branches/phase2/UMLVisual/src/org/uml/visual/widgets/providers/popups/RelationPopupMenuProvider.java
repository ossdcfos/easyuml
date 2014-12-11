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
import org.uml.model.relations.RelationBase;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author Uros
 */
public class RelationPopupMenuProvider implements PopupMenuProvider {

    private ConnectionWidget widget;
    ClassDiagramScene cdScene;
    RelationBase relationComponent;
    LabelWidget name;
    private JPopupMenu menu;
    private JMenuItem setName;
    private JMenuItem removeRelation;

    public RelationPopupMenuProvider(ConnectionWidget widget, RelationBase relationComponent) {
        this.widget = widget;
        this.cdScene = (ClassDiagramScene)widget.getScene();
       
        this.relationComponent = relationComponent;
        menu = new JPopupMenu("Connection Menu");

//        (setName = new JMenuItem("Change relation type")).addActionListener(changeRelationTypeListener);
//        menu.add(setName);
        (removeRelation = new JMenuItem("Remove relation")).addActionListener(removeRelationListener);
        menu.add(removeRelation);
    }

//    ActionListener changeRelationTypeListener = new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            ChangeRelationTypeDialog dialog = new ChangeRelationTypeDialog(null, relationComponent, cdScene.getClassDiagram(), widget, true);
//            dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
//            dialog.setTitle("Change relation type");
//            dialog.setVisible(true);
//        }
//    };

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
