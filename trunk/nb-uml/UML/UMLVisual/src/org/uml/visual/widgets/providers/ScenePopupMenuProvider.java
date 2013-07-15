/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

import java.awt.AWTException;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;
import org.uml.model.ClassComponent;
import org.uml.model.EnumComponent;
import org.uml.model.InterfaceComponent;
import org.uml.visual.dialogs.AddRelationDialog;
import org.uml.visual.dialogs.GenerateCodeDialog;
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
    private JMenuItem generateCode;
    private ClassDiagramScene scene;
    private Point popupPoint;
    WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
    
    MouseListener mouseListener = new MouseAdapterZaView(editorAction);
    
    public ScenePopupMenuProvider(ClassDiagramScene scene) {
        this.scene = scene;
        sceneMenu = new JPopupMenu("Diagram menu!");
        generateMenu();
    }

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        popupPoint = point;
        return sceneMenu;
    }

    public final void generateMenu() {

        createClassItem = new JMenuItem("Add Class");
        createClassItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // add neww class component and widget to scene
                // here we should just add new class component to model, and the scene should be updated elsewhere
                ClassWidget widget = (ClassWidget)scene.addNode(new ClassComponent());
                widget.setPreferredLocation(popupPoint);
                scene.validate();
                
                // open editor for class name
                ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getNameLabel());
                scene.getView().addMouseListener(mouseListener);
                
            }
        });
        createInterfaceItem = new JMenuItem("Add Interface");
        createInterfaceItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterfaceWidget widget = (InterfaceWidget)scene.addNode(new InterfaceComponent());
                widget.setPreferredLocation(popupPoint);
                scene.validate();

                ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getNameLabel());
                scene.getView().addMouseListener(mouseListener);
            }
        });
        createEnumItem = new JMenuItem("Add Enum");
        createEnumItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EnumWidget widget = (EnumWidget)scene.addNode(new EnumComponent());
                widget.setPreferredLocation(popupPoint);
                scene.validate();               
                
                ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getNameLabel());
                scene.getView().addMouseListener(mouseListener);
            }
        });
        
        createRelationshipItem= new JMenuItem("Add Relationship");
        createRelationshipItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                AddRelationDialog dialog = new AddRelationDialog(null,scene,true);
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                dialog.setTitle("Add relation");
                dialog.setVisible(true);
            }
        });
        
        generateCode = new JMenuItem("Generate code");
        generateCode.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                GenerateCodeDialog dialog = new GenerateCodeDialog(null, true, scene.getUmlClassDiagram());
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                dialog.setTitle("Generate code");
                dialog.setVisible(true);
            }
        });
        
        sceneMenu.add(generateCode);
        sceneMenu.add(createClassItem);
        sceneMenu.add(createInterfaceItem);
        sceneMenu.add(createEnumItem);
        sceneMenu.add(createRelationshipItem);
    }
}
