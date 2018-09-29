package org.uml.visual.widgets.popups;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.export.SceneExporter;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Exceptions;
import org.openide.windows.WindowManager;
import org.uml.jung.JUNGEngine;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.components.EnumComponent;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.components.PackageComponent;
import org.uml.visual.dialogs.ConnectRelationPanel;
import org.uml.visual.dialogs.CustomSizePanel;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.actions.ComponentNameEditor;
import org.uml.visual.widgets.components.ClassWidget;
import org.uml.visual.widgets.components.EnumWidget;
import org.uml.visual.widgets.components.InterfaceWidget;
import org.uml.visual.widgets.components.PackageWidget;
import org.uml.visual.widgets.providers.CloseInplaceEditorOnClickAdapter;

/**
 *
 * @author hrza
 */
public class ScenePopupMenuProvider implements PopupMenuProvider {

    private final ClassDiagramScene scene;
    private final JPopupMenu sceneMenu;
    private JMenuItem miCreateClass;
    private JMenuItem miCreateInterface;
    private JMenuItem miCreateEnum;
    private JMenuItem miCreatePackage;
    private JMenuItem miCreateRelationship;
//    private JMenuItem miGenerateCode;
    private JMenu mExportAsImage;
    private JMenuItem miApplyJUNGLayout;
    private JMenu mVisualOptions;
    private JCheckBoxMenuItem miShowIcons;
    private JCheckBoxMenuItem miShowAddMember;
    private JCheckBoxMenuItem miShowMembers;
    private JCheckBoxMenuItem miShowSimpleTypeNames;
    private final JRadioButtonMenuItem miBlueGray = new JRadioButtonMenuItem("Blue-gray");
    private final JRadioButtonMenuItem miSand = new JRadioButtonMenuItem("Sand-red");
    private JMenu mColorTheme;
    private Point popupPoint;

    public ScenePopupMenuProvider(ClassDiagramScene scene) {
        this.scene = scene;
        sceneMenu = new JPopupMenu("Diagram menu!");
        generateMenu();
    }

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        popupPoint = point;
        updateMenu();
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

                // Temp renamer
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

                // Temp renamer
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

                // Temp renamer
                WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new ComponentNameEditor(widget));
                ActionFactory.getInplaceEditorController(editorAction).openEditor(widget.getNameLabel());
                scene.getView().addMouseListener(new CloseInplaceEditorOnClickAdapter(editorAction));
            }
        });
        miCreatePackage = new JMenuItem("Add Package");
        miCreatePackage.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                PackageWidget widget = (PackageWidget) scene.addNode(new PackageComponent());
                widget.bringToBack();
                widget.setPreferredLocation(popupPoint);
                widget.getComponent().setLocation(popupPoint);
                scene.validate();

                // Temp renamer
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

//        miGenerateCode = new JMenuItem("Generate code");
//        miGenerateCode.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                GenerateCodeDialog dialog = new GenerateCodeDialog(scene.getClassDiagram());
//                dialog.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
//                dialog.setVisible(true);
//            }
//        });

        mExportAsImage = new JMenu("Export as image");
        JMenuItem miExportAsImageCurrent = new JMenuItem("Current view");
        miExportAsImageCurrent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportAsImage(true,false);
            }
        });
        mExportAsImage.add(miExportAsImageCurrent);
        JMenuItem miExportAsImageWhole = new JMenuItem("Whole diagram");
        miExportAsImageWhole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportAsImage(false,false);
            }
        });
        mExportAsImage.add(miExportAsImageWhole);
        miExportAsImageWhole = new JMenuItem("Whole diagram (custom size)");
        miExportAsImageWhole.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exportAsImage(false,true);
            }
        });
        mExportAsImage.add(miExportAsImageWhole);
        miApplyJUNGLayout = new JMenuItem("Arrange diagram (experimental)");
        miApplyJUNGLayout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JUNGEngine je = new JUNGEngine(scene);
                je.applyJUNGLayout();
            }
        });

        mVisualOptions = new JMenu("Visual options");

        miShowIcons = new JCheckBoxMenuItem("Show icons");
        miShowIcons.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scene.setShowIcons(!scene.isShowIcons());
                WindowManager.getDefault().findTopComponent("properties").repaint();
            }
        });
        mVisualOptions.add(miShowIcons);

        miShowAddMember = new JCheckBoxMenuItem("Show add member");
        miShowAddMember.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClassDiagram classDiagram = scene.getClassDiagram();
                classDiagram.setShowAddMember(!classDiagram.isShowAddMember());
                WindowManager.getDefault().findTopComponent("properties").repaint();
            }
        });
        mVisualOptions.add(miShowAddMember);
        
        miShowMembers = new JCheckBoxMenuItem("Show members");
        miShowMembers.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ClassDiagram classDiagram = scene.getClassDiagram();
                classDiagram.setShowMembers(!classDiagram.isShowMembers());
                WindowManager.getDefault().findTopComponent("properties").repaint();
            }
        });
        mVisualOptions.add(miShowMembers);

        miShowSimpleTypeNames = new JCheckBoxMenuItem("Show simple class names");
        miShowSimpleTypeNames.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                scene.setShowSimpleTypes(!scene.isShowSimpleTypes());
                WindowManager.getDefault().findTopComponent("properties").repaint();
            }
        });
        mVisualOptions.add(miShowSimpleTypeNames);

        ButtonGroup themeButtonGroup = new ButtonGroup();
        mColorTheme = new JMenu("Color theme");
        miBlueGray.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                scene.setColorTheme("blue-gray");
                miBlueGray.setSelected(true);
            }
        });
        themeButtonGroup.add(miBlueGray);
        mColorTheme.add(miBlueGray);
        miSand.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                scene.setColorTheme("sand-red");
                miSand.setSelected(true);
            }
        });
        themeButtonGroup.add(miSand);
        mColorTheme.add(miSand);

        sceneMenu.add(miCreateClass);

        sceneMenu.add(miCreateInterface);

        sceneMenu.add(miCreateEnum);

//        sceneMenu.add(miCreatePackage);
        
        sceneMenu.addSeparator();

        sceneMenu.add(miCreateRelationship);

        sceneMenu.addSeparator();

//        sceneMenu.add(miGenerateCode);

        sceneMenu.add(mExportAsImage);

        sceneMenu.addSeparator();

        sceneMenu.add(miApplyJUNGLayout);

        sceneMenu.add(mVisualOptions);

        sceneMenu.add(mColorTheme);
    }

    private void updateMenu() {
        miShowIcons.setSelected(scene.isShowIcons());
        miShowMembers.setSelected(scene.getClassDiagram().isShowMembers());
        miShowAddMember.setSelected(scene.getClassDiagram().isShowAddMember());
        miShowSimpleTypeNames.setSelected(scene.isShowSimpleTypes());
        switch(scene.getColorTheme().getName()){
            case "blue-gray":
                miBlueGray.setSelected(true);
                break;
            case "sand-red":
                miSand.setSelected(true);
                break;
        }
    }

    private static File currentSaveDirectory;
    private void exportAsImage(boolean onlyVisiblePart,boolean customSize) {    
        int customWidth = 4*scene.getPreferredBounds().width;
        int customHeight = 4*scene.getPreferredBounds().height;
        if (customSize) {
            CustomSizePanel customSizePanel = new CustomSizePanel(customWidth,customHeight);
            if (!customSizePanel.openDialog())
                return;
            customWidth = customSizePanel.getWidth();
            customHeight = customSizePanel.getHeight();            
        }
        JFileChooser chooser = new JFileChooser() {
            @Override
            public void approveSelection() {
                File f = getSelectedFile();
                if (!f.getName().toLowerCase().endsWith(".png")) {
                    f = new File(f.getParentFile(), f.getName() + ".png");
                }
                if (f.exists() && getDialogType() == SAVE_DIALOG) {
                    int result = JOptionPane.showConfirmDialog(this, "The file exists, overwrite?", "Existing file", JOptionPane.YES_NO_OPTION);
                    switch (result) {
                        case JOptionPane.YES_OPTION:
                            super.approveSelection();
                            return;
                        case JOptionPane.NO_OPTION:
                            return;
                        case JOptionPane.CLOSED_OPTION:
                            return;
                    }
                }
                super.approveSelection();
            }
        };
        if (currentSaveDirectory != null) {
            chooser.setCurrentDirectory(currentSaveDirectory);
        }
        chooser.setFileFilter(new FileNameExtensionFilter("Portable Network Graphics (.png)", "png"));
        if (chooser.showSaveDialog(scene.getView()) == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            if (f != null && f.getParentFile() != null) {
                currentSaveDirectory = f;
            }
            if (!f.getName().toLowerCase().endsWith(".png")) {
                f = new File(f.getParentFile(), f.getName() + ".png");
            }
            try {
                // Zoom type is overriden if onlyVisiblePart is set
                SceneExporter.createImage(scene, f, SceneExporter.ImageType.PNG, customSize?SceneExporter.ZoomType.CUSTOM_SIZE:SceneExporter.ZoomType.ACTUAL_SIZE, onlyVisiblePart, false, 0, customWidth, customHeight);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Cannot export scene as image!", "Input-output error!", JOptionPane.ERROR_MESSAGE);
                Exceptions.printStackTrace(ex);
            }
        }
    }
}
