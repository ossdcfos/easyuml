/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Random;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.openide.windows.WindowManager;
import org.uml.model.Method;
import org.uml.model.MethodArgument;
import org.uml.visual.dialogs.PackageDialog;
import org.uml.visual.widgets.providers.MouseAdapterZaView;
import org.uml.visual.widgets.InterfaceWidget;
import org.uml.visual.widgets.MethodWidget;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;
import org.uml.visual.widgets.actions.NameEditorAction;

/**
 *
 * @author Jelena
 */
public class InterfacePopupMenuProvider implements PopupMenuProvider {

    private InterfaceWidget interfaceWidget;
    private JPopupMenu menu;
    private JMenuItem deleteInterface;
    private JMenuItem addPackage;
    private JMenuItem addMethod;
    WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction());
    MouseListener mouseListener = new MouseAdapterZaView(editorAction);

    public InterfacePopupMenuProvider(InterfaceWidget interfaceWidget) {
        this.interfaceWidget = interfaceWidget;
        menu = new JPopupMenu("Interface Menu");

        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);
        menu.addSeparator();
        (deleteInterface = new JMenuItem("Delete Interface")).addActionListener(removeWidgetListener);
        menu.add(deleteInterface);
        (addPackage = new JMenuItem("Set Package")).addActionListener(addPackageListener);
        menu.add(addPackage);
    }
    ActionListener addMethodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            Method m = new Method("untitledMethod", null, new HashMap<String, MethodArgument>());
            try {
                interfaceWidget.getComponent().addMethod(m);

            } catch (RuntimeException ex) {
                Random r = new Random();
                int i = r.nextInt(10000);
                m.setName(m.getName() + i);
                interfaceWidget.getComponent().addMethod(m);
            }
            MethodWidget w = new MethodWidget(interfaceWidget.getClassDiagramScene(), m);
            interfaceWidget.addMethodWidget(w);
            interfaceWidget.getScene().validate();

            WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(w));
            ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(w.getNameLabel());
            MouseListener mouseListener = new MouseAdapterZaView(nameEditorAction);
            interfaceWidget.getScene().getView().addMouseListener(mouseListener);
        }
    };
    ActionListener addPackageListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            String pack = "";
            PackageDialog pd = new PackageDialog(null, true, interfaceWidget.getComponent(), interfaceWidget.getClassDiagramScene().getUmlClassDiagram());
            pd.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            pd.setTitle("Package");
            pd.setVisible(true);

//            classWidget.getComponent().setPack(pack);
//            Constructor c = new Constructor(classWidget.getName());
//            classWidget.getComponent().addConstructor(c);
//            ConstructorWidget w = new ConstructorWidget(classWidget.getClassDiagramScene(), c);
//            classWidget.addConstructorWidget(w);
            interfaceWidget.getScene().validate();

//            w.getActions().addAction(classWidget.getScene().createWidgetHoverAction());
        }
    };
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            interfaceWidget.getComponent().getParentDiagram().removeComponent(interfaceWidget.getName());
            interfaceWidget.removeFromParent();
            interfaceWidget.getScene().validate();
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
