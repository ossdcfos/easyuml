package org.uml.visual.widgets.popups;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.members.Literal;
import org.uml.visual.widgets.components.EnumWidget;
import org.uml.visual.widgets.members.LiteralWidget;
import org.uml.visual.widgets.actions.MemberNameEditor;
import org.uml.visual.widgets.providers.CloseInplaceEditorOnClickAdapter;

/**
 *
 * @author Jelena
 */
public class EnumPopupMenuProvider implements PopupMenuProvider {

    private EnumWidget enumWidget;
    private JPopupMenu menu;
    private JMenuItem deleteClass;
    private JMenuItem addLiteral;

    public EnumPopupMenuProvider(EnumWidget enumWidget) {
        this.enumWidget = enumWidget;
        menu = new JPopupMenu("Enum Menu");

        (addLiteral = new JMenuItem("Add Literal")).addActionListener(addLiteralListener);
        menu.add(addLiteral);
        
        menu.addSeparator();

        (deleteClass = new JMenuItem("Delete Enum")).addActionListener(removeWidgetListener);
        menu.add(deleteClass);

    }
    
    ActionListener addLiteralListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            enumWidget.addLiteralWidget();
        }
    };
    
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            enumWidget.getComponent().getParentDiagram().removePartFromContainer(enumWidget.getComponent());
            enumWidget.removeFromParent();
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
