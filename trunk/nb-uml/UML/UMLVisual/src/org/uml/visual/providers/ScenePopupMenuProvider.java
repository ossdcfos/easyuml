/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.providers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.InplaceEditorProvider;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassComponent;
import org.uml.model.EnumComponent;
import org.uml.model.InterfaceComponent;
import org.uml.visual.dialogs.AddRelationshipDialog;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.EnumWidget;
import org.uml.visual.widgets.InterfaceWidget;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;

/**
 *
 * @author hrza
 */
public class ScenePopupMenuProvider implements PopupMenuProvider {

    private final JPopupMenu sceneMenu;
    private JMenuItem createClassItem;
    private JMenuItem createInterfaceItem;
    private JMenuItem createEnumItem;
    private JMenuItem createRelationshipItem;
    private ClassDiagramScene scene;
    WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
    
    MouseListener mouseListener = new MouseAdapterZaView(editorAction);
    
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
                ClassWidget widget = new ClassWidget(scene, new ClassComponent());
                scene.addWidget(widget);               
                //ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getChildren().get(2).getChildren().get(0));
               ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getNameLabel());
                // (new AddClassDialog(null, scene, true)).setVisible(true);
                scene.getView().addMouseListener(mouseListener);
                
            }
        });
        createInterfaceItem = new JMenuItem("Add Interface");
        createInterfaceItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterfaceWidget widget = new InterfaceWidget(scene, new InterfaceComponent());
                scene.addWidget(widget);    
                //ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getChildren().get(2).getChildren().get(1));
                ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getNameLabel());
                scene.getView().addMouseListener(mouseListener);
            }
        });
        createEnumItem = new JMenuItem("Add Enum");
        createEnumItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EnumWidget widget = new EnumWidget(scene, new EnumComponent());
                scene.addWidget(widget);                
                //ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getChildren().get(2).getChildren().get(1));
                ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getNameLabel());
                scene.getView().addMouseListener(mouseListener);
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
        sceneMenu.add(createInterfaceItem);
        sceneMenu.add(createEnumItem);
        sceneMenu.add(createRelationshipItem);
    }
}
