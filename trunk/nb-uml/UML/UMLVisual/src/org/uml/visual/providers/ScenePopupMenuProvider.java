/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.providers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.dialogs.AddClassDialog;
import org.uml.visual.dialogs.AddRelationshipDialog;
import org.uml.visual.widgets.ClassDiagramScene;

/**
 *
 * @author hrza
 */
public class ScenePopupMenuProvider implements PopupMenuProvider {

    private final JPopupMenu sceneMenu;
    private JMenuItem createClassItem;
    private JMenuItem createRelationshipItem;
    private ClassDiagramScene scene;

    public ScenePopupMenuProvider(ClassDiagramScene scene) {
        this.scene = scene;
        sceneMenu = new JPopupMenu("Diagram menu!");
        generateMenu();
    }

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {

        return sceneMenu;
    }

    public final void generateMenu() {
        createClassItem = new JMenuItem("Add Class");
        createClassItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                (new AddClassDialog(null, scene, true)).setVisible(true);
            }
        });
        createRelationshipItem= new JMenuItem("Add Relationship");
        createRelationshipItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                (new AddRelationshipDialog(null,scene,true)).setVisible(true);
            }
        });
        sceneMenu.add(createClassItem);
        sceneMenu.add(createRelationshipItem);
    }
}
