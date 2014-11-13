/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers.popups;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassComponent;
import org.uml.model.members.Constructor;
import org.uml.visual.widgets.members.ConstructorWidget;

/**
 *
 * @author Jelena
 */
public class ConstructorPopupMenuProvider implements PopupMenuProvider {

    private ConstructorWidget constructorWidget;
    private JPopupMenu menu;
    private JMenuItem deleteMethod;
    private JMenu visibilitySubmenu;
    private JMenu modifiersSubmenu;
    private JMenuItem privateItem;
    private JMenuItem publicItem;
    private JMenuItem protectedItem;
    private JMenuItem packageItem;
    private JCheckBoxMenuItem staticJCBMI;
    private JCheckBoxMenuItem abstractJCBMI;
    private JCheckBoxMenuItem finalJCBMI;
    private JCheckBoxMenuItem synchronizedJCBMI;

    public ConstructorPopupMenuProvider(ConstructorWidget constructorWidget) {
        this.constructorWidget = constructorWidget;
        menu = new JPopupMenu("Constructor Menu");

        visibilitySubmenu = new JMenu("Visibility");
        visibilitySubmenu.add(privateItem = new JMenuItem("public"));
        visibilitySubmenu.add(publicItem = new JMenuItem("private"));
        visibilitySubmenu.add(protectedItem = new JMenuItem("protected"));
        visibilitySubmenu.add(packageItem = new JMenuItem("package"));
        menu.add(visibilitySubmenu);


        menu.addSeparator();

        (deleteMethod = new JMenuItem("Delete Constructor")).addActionListener(removeWidgetListener);
        menu.add(deleteMethod);
    }
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            constructorWidget.getMember().getDeclaringClass().removeMember(constructorWidget.getName());
            if(constructorWidget.getMember().getDeclaringClass() instanceof ClassComponent) {
               ((ClassComponent) constructorWidget.getMember().getDeclaringClass()).removeConstructor((Constructor) constructorWidget.getMember());
           }
            constructorWidget.getClassDiagramScene().removeObject(constructorWidget.getMember());
            constructorWidget.removeFromParent();
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
