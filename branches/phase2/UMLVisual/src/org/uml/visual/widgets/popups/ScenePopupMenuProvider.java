package org.uml.visual.widgets.popups;

import java.awt.Dialog;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Utilities;
import org.openide.windows.WindowManager;
import org.uml.jung.JUNGEngine;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.EnumComponent;
import org.uml.model.components.InterfaceComponent;
import org.uml.visual.dialogs.ConnectRelationPanel;
import org.uml.newcode.GenerateCodeDialog;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.actions.ComponentNameEditor;
import org.uml.visual.widgets.components.ClassWidget;
import org.uml.visual.widgets.components.EnumWidget;
import org.uml.visual.widgets.components.InterfaceWidget;
import org.uml.visual.widgets.providers.CloseInplaceEditorOnClickAdapter;

/**
 *
 * @author hrza
 */
public class ScenePopupMenuProvider implements PopupMenuProvider {

    private final JPopupMenu sceneMenu;
    private JMenuItem miCreateClass;
    private JMenuItem miCreateInterface;
    private JMenuItem miCreateEnum;
    private JMenuItem miCreateRelationship;
    private JMenuItem miGenerateCode;
    private JMenuItem miExportAsImage;
    private JMenuItem miApplyJUNGLayout;
    private ClassDiagramScene scene;
    private Point popupPoint;

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

        miCreateClass = new JMenuItem("Add Class");
        miCreateClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // add new class component and widget to scene
                // here we should just add new class component to model, and the scene should be updated elsewhere
                ClassWidget widget = (ClassWidget) scene.addNode(new ClassComponent());
                widget.setPreferredLocation(popupPoint);
                widget.getComponent().setLocation(popupPoint);
                scene.validate();

                // open editor for class name
                WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new ComponentNameEditor(widget));
                ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getNameLabel());
                scene.getView().addMouseListener(new CloseInplaceEditorOnClickAdapter(editorAction));
            }
        });
        miCreateInterface = new JMenuItem("Add Interface");
        miCreateInterface.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                InterfaceWidget widget = (InterfaceWidget) scene.addNode(new InterfaceComponent());
                widget.setPreferredLocation(popupPoint);
                widget.getComponent().setLocation(popupPoint);
                scene.validate();

                WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new ComponentNameEditor(widget));
                ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getNameLabel());
                scene.getView().addMouseListener(new CloseInplaceEditorOnClickAdapter(editorAction));
            }
        });
        miCreateEnum = new JMenuItem("Add Enum");
        miCreateEnum.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EnumWidget widget = (EnumWidget) scene.addNode(new EnumComponent());
                widget.setPreferredLocation(popupPoint);
                widget.getComponent().setLocation(popupPoint);
                scene.validate();

                WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new ComponentNameEditor(widget));
                ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getNameLabel());
                scene.getView().addMouseListener(new CloseInplaceEditorOnClickAdapter(editorAction));
            }
        });

        miCreateRelationship = new JMenuItem("Add Relationship");
        miCreateRelationship.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ConnectRelationPanel panel = new ConnectRelationPanel(scene);
                panel.openRelationDialog();
            }
        });

        miGenerateCode = new JMenuItem("Generate Code");
        miGenerateCode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {        
                GenerateCodeDialog dialog = new GenerateCodeDialog(scene.getClassDiagram());
                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
                dialog.setVisible(true);
            }
        });

        miExportAsImage = new JMenuItem("Export as Image");
        miExportAsImage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BufferedImage img = new BufferedImage(
                        scene.getView().getWidth(),
                        scene.getView().getHeight(),
                        BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D graphics = img.createGraphics();
                scene.paint(graphics);
                graphics.dispose();
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new FileNameExtensionFilter(
                        "Portable Network Graphics (.png)", "png"));
                if (chooser.showSaveDialog(scene.getView()) == JFileChooser.APPROVE_OPTION) {
                    File f = chooser.getSelectedFile();
                    if (!f.getName().toLowerCase().endsWith(".png")) {
                        f = new File(f.getParentFile(), f.getName() + ".png");

                        try {
                            ImageIO.write(img, "png", f);
                        } catch (IOException ex) {
                            //Logger.getLogger(getName()).warning(ex.toString());
                        }
                    }
                }
            }
        });

        miApplyJUNGLayout = new JMenuItem("Arrange diagram");
        miApplyJUNGLayout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JUNGEngine je = new JUNGEngine(scene);
                je.applyJUNGLayout();
            }
        });

        sceneMenu.add(miCreateClass);

        sceneMenu.add(miCreateInterface);

        sceneMenu.add(miCreateEnum);

        sceneMenu.addSeparator();

        sceneMenu.add(miCreateRelationship);

        sceneMenu.addSeparator();

        sceneMenu.add(miGenerateCode);

        sceneMenu.add(miExportAsImage);

        sceneMenu.addSeparator();

        sceneMenu.add(miApplyJUNGLayout);
    }
}
