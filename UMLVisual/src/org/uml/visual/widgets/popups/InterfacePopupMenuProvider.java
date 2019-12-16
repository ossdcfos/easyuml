package org.uml.visual.widgets.popups;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.Exceptions;
import org.uml.model.ClassDiagram;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.InterfaceComponent;
import org.uml.model.members.Method;
import org.uml.visual.widgets.components.InterfaceWidget;

/**
 *
 * @author Jelena
 */
public class InterfacePopupMenuProvider extends ComponentPopupMenuProvider {

    private InterfaceWidget interfaceWidget;
    private JPopupMenu menu;
    private JMenuItem addMethod,copyMethods,pasteMethods;
    private JMenuItem deleteInterface;

    public InterfacePopupMenuProvider(InterfaceWidget interfaceWidget) {
        super(interfaceWidget);
        this.interfaceWidget = interfaceWidget;
        menu = new JPopupMenu("Interface Menu");

        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);

        (pasteMethods = new JMenuItem("Paste methods from clipboard")).addActionListener(pasteMethodsListener);
        menu.add(pasteMethods);

        menu.addSeparator();

        (copyMethods = new JMenuItem("Copy methods to clipboard")).addActionListener(copyMethodsListener);
        menu.add(copyMethods);
        
        menu.addSeparator();

        addZMenuItems(menu);
        
        menu.addSeparator();

        (deleteInterface = new JMenuItem("Delete Interface")).addActionListener(removeWidgetListener);
        menu.add(deleteInterface);
    }
    ActionListener addMethodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            interfaceWidget.addMethodWidget();
        }
    };
    ActionListener pasteMethodsListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                String clipboard = (String)Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
                interfaceWidget.addMethods(clipboard);
            } catch (Exception ex) {
            }
        }
    };    
    ActionListener copyMethodsListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            InterfaceComponent interfaceComponent = interfaceWidget.getComponent();
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream ps = new PrintStream(baos, true, "utf-8");
                for (Method method : interfaceComponent.getMethods()) {
                    ps.println(method.getSignature());
                }
                StringSelection selection = new StringSelection(baos.toString());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection,selection);
            } catch (UnsupportedEncodingException ex) {
            }
        }
    };   
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClassDiagram classDiagram = interfaceWidget.getComponent().getParentDiagram();
            classDiagram.removeComponentFromContainer(interfaceWidget.getComponent());
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
