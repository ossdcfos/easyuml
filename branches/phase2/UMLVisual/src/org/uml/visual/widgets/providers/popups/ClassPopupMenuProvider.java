package org.uml.visual.widgets.providers.popups;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.widget.*;
import org.uml.model.ClassDiagram;
import org.uml.visual.widgets.components.ClassWidget;
import org.uml.visual.widgets.actions.NameEditor;
import org.uml.visual.widgets.providers.MouseAdapterZaView;

/**
 *
 * @author hrza
 */
public class ClassPopupMenuProvider implements PopupMenuProvider {

    private ClassWidget classWidget;
    private JPopupMenu menu;
    private JMenuItem deleteClass;
    private JMenuItem addField;
    private JMenuItem addMethod;
    private JMenuItem addConstructor;
    private JMenuItem editPackage;
    WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new NameEditor(classWidget));
    MouseListener mouseListener = new MouseAdapterZaView(editorAction);

    public ClassPopupMenuProvider(ClassWidget classWidget) {
        this.classWidget = classWidget;
        menu = new JPopupMenu("Class Menu");

        (addConstructor = new JMenuItem("Add Constructor")).addActionListener(addConstructorListener);
        menu.add(addConstructor);
        (addField = new JMenuItem("Add Field")).addActionListener(addFieldListener);
        menu.add(addField);
        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);

        menu.addSeparator();

//        (editPackage = new JMenuItem("Edit Package")).addActionListener(editPackageListener);
//        menu.add(editPackage);
//
//        menu.addSeparator();
        
        (deleteClass = new JMenuItem("Delete Class")).addActionListener(removeWidgetListener);
        menu.add(deleteClass);
    }
    
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClassDiagram classDiagram = classWidget.getComponent().getParentDiagram();
            classDiagram.removeComponent(classWidget.getComponent());
        }
    };
    
    ActionListener addFieldListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.addFieldWidget();
        }
    };
    
    ActionListener addMethodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.addMethodWidget();
        }
    };
    ActionListener addConstructorListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.addConstructorWidget();
        }
    };
    
//    ActionListener editPackageListener = new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            EditPackageDialog epd = new EditPackageDialog(classWidget.getComponent());
//            epd.setVisible(true);
//////            String pack = "";
////            PackageDialog pd = new PackageDialog(null, true, classWidget.getComponent(), classWidget.getClassDiagramScene().getClassDiagram());
////            pd.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
////            pd.setTitle("Package");
////            pd.setVisible(true);
////
//////            classWidget.getComponent().setPack(pack);
//////            Constructor c = new Constructor(classWidget.getName());
//////            classWidget.getComponent().addConstructor(c);
//////            ConstructorWidget w = new ConstructorWidget(classWidget.getClassDiagramScene(), c);
//////            classWidget.addConstructorWidget(w);
////            classWidget.getScene().validate();
////
//////            w.getActions().addAction(classWidget.getScene().createWidgetHoverAction());
//        }
//    };
    
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
    
}
