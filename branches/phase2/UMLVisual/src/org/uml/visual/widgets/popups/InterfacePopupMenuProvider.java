package org.uml.visual.widgets.popups;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassDiagram;
import org.uml.model.members.Method;
import org.uml.visual.widgets.components.InterfaceWidget;
import org.uml.visual.widgets.members.MethodWidget;
import org.uml.visual.widgets.actions.MemberNameEditor;
import org.uml.visual.widgets.providers.CloseInplaceEditorOnClickAdapter;

/**
 *
 * @author Jelena
 */
public class InterfacePopupMenuProvider implements PopupMenuProvider {

    private InterfaceWidget interfaceWidget;
    private JPopupMenu menu;
    private JMenuItem addMethod;
    private JMenuItem deleteInterface;

    public InterfacePopupMenuProvider(InterfaceWidget interfaceWidget) {
        this.interfaceWidget = interfaceWidget;
        menu = new JPopupMenu("Interface Menu");

        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);

        menu.addSeparator();

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

            WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new MemberNameEditor(w));
            ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(w.getNameLabel());
            MouseListener mouseListener = new CloseInplaceEditorOnClickAdapter(nameEditorAction);
            interfaceWidget.getScene().getView().addMouseListener(mouseListener);
        }
    };

    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClassDiagram classDiagram = interfaceWidget.getComponent().getParentDiagram();
            classDiagram.removePartFromContainer(interfaceWidget.getComponent());
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
