package org.uml.visual.widgets.popups;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.components.PackageWidget;

/**
 *
 * @author Jelena
 */
public class PackagePopupMenuProvider extends ComponentPopupMenuProvider {

    private PackageWidget packageWidget;
    private JPopupMenu menu;
    private JMenuItem deleteClass;

    public PackagePopupMenuProvider(PackageWidget packageWidget) {
        super(packageWidget);
        this.packageWidget = packageWidget;
        menu = new JPopupMenu("Package Menu");

        menu.addSeparator();

        addZMenuItems(menu);

        menu.addSeparator();

        (deleteClass = new JMenuItem("Delete Package")).addActionListener(removeWidgetListener);
        menu.add(deleteClass);

    }
    
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            packageWidget.getComponent().getParentDiagram().removeComponentFromContainer(packageWidget.getComponent());
            packageWidget.removeFromParent();
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
