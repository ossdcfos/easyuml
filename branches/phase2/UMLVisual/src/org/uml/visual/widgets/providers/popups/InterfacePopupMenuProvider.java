package org.uml.visual.widgets.providers.popups;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.LinkedHashSet;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassDiagram;
import org.uml.model.members.Method;
import org.uml.model.members.MethodArgument;
import org.uml.visual.widgets.components.InterfaceWidget;
import org.uml.visual.widgets.members.MethodWidget;
import org.uml.visual.widgets.actions.NameEditor;
import org.uml.visual.widgets.providers.MouseAdapterZaView;

/**
 *
 * @author Jelena
 */
public class InterfacePopupMenuProvider implements PopupMenuProvider {

    private InterfaceWidget interfaceWidget;
    private JPopupMenu menu;
    private JMenuItem addMethod;
    private JMenuItem editPackage;
    private JMenuItem deleteInterface;
    WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new NameEditor(interfaceWidget));
    MouseListener mouseListener = new MouseAdapterZaView(editorAction);

    public InterfacePopupMenuProvider(InterfaceWidget interfaceWidget) {
        this.interfaceWidget = interfaceWidget;
        menu = new JPopupMenu("Interface Menu");

        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);

        menu.addSeparator();

//        (editPackage = new JMenuItem("Edit Package")).addActionListener(editPackageListener);
//        menu.add(editPackage);
//        menu.addSeparator();

        (deleteInterface = new JMenuItem("Delete Interface")).addActionListener(removeWidgetListener);
        menu.add(deleteInterface);
    }
    ActionListener addMethodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            Method m = new Method("untitledMethod", "void");
            try {
                interfaceWidget.getComponent().addMethod(m);

            } catch (RuntimeException ex) {
                JOptionPane.showMessageDialog(menu, "Greska u pravljenju metode interfejsa " + m.getName(), "Greska!", JOptionPane.ERROR_MESSAGE);
            }
            MethodWidget w = new MethodWidget(interfaceWidget.getClassDiagramScene(), m);
            interfaceWidget.addMethodWidget(w);
            interfaceWidget.getScene().validate();

            WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditor(w));
            ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(w.getNameLabel());
            MouseListener mouseListener = new MouseAdapterZaView(nameEditorAction);
            interfaceWidget.getScene().getView().addMouseListener(mouseListener);
        }
    };

//    ActionListener editPackageListener = new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            EditPackageDialog epd = new EditPackageDialog(interfaceWidget.getComponent());
//            epd.setVisible(true);
//////            String pack = "";
////            PackageDialog pd = new PackageDialog(null, true, interfaceWidget.getComponent(), interfaceWidget.getClassDiagramScene().getClassDiagram());
////            pd.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
////            pd.setTitle("Package");
////            pd.setVisible(true);
////
//////            classWidget.getComponent().setPack(pack);
//////            Constructor c = new Constructor(classWidget.getName());
//////            classWidget.getComponent().addConstructor(c);
//////            ConstructorWidget w = new ConstructorWidget(classWidget.getClassDiagramScene(), c);
//////            classWidget.addConstructorWidget(w);
////            interfaceWidget.getScene().validate();
////
//////            w.getActions().addAction(classWidget.getScene().createWidgetHoverAction());
//        }
//    };

    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClassDiagram classDiagram = interfaceWidget.getComponent().getParentDiagram();
            classDiagram.removeComponent(interfaceWidget.getComponent());
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
