/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.visual.widgets.FieldWidget;
import org.uml.visual.widgets.LiteralWidget;

/**
 *
 * @author Jelena
 */
public class LiteralPopupProvider implements PopupMenuProvider{

    private LiteralWidget literalWidget;
    private JPopupMenu menu;
    private JMenuItem deleteField;
    //private JMenu visibilitySubmenu;
    //private JMenu modifiersSubmenu;
    private JMenuItem privateItem;
    private JMenuItem publicItem;
    private JMenuItem protectedItem;
    private JMenuItem packageItem;
    private JCheckBoxMenuItem staticJCBMI;
    private JCheckBoxMenuItem abstractJCBMI;
    private JCheckBoxMenuItem finalJCBMI;
    private JCheckBoxMenuItem synchronizedJCBMI;
    
    public LiteralPopupProvider(LiteralWidget literalWidget) {
        this.literalWidget = literalWidget;
        menu = new JPopupMenu("Literal Menu");
        
        (deleteField = new JMenuItem("Delete Literal")).addActionListener(removeWidgetListener);
        menu.add(deleteField);
    }
    
    
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           literalWidget.getMember().getDeclaringClass().removeMember(literalWidget.getName());
           literalWidget.removeFromParent();
        }
    };
    
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
    
}
