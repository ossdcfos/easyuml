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
import org.uml.visual.widgets.MethodWidget;

/**
 *
 * @author "NUGS"
 */
public class MethodPopupMenuProvider implements PopupMenuProvider{
    
        private Widget methodWidget;
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

    public MethodPopupMenuProvider(Widget methodWidget) {
        this.methodWidget = methodWidget;
        menu= new JPopupMenu ("Class Menu");
        
        visibilitySubmenu = new JMenu("Visibility");
        visibilitySubmenu.add(privateItem = new JMenuItem("public"));
        visibilitySubmenu.add(publicItem =new JMenuItem("private"));
        visibilitySubmenu.add(protectedItem =new JMenuItem("protected"));
        visibilitySubmenu.add(packageItem =new JMenuItem("package"));
        menu.add(visibilitySubmenu);
        
        modifiersSubmenu= new JMenu("Modifiers");
        modifiersSubmenu.add(staticJCBMI= new JCheckBoxMenuItem("static"));
        modifiersSubmenu.add(abstractJCBMI=new JCheckBoxMenuItem("abstract"));
        modifiersSubmenu.add(finalJCBMI=new JCheckBoxMenuItem("final"));
        modifiersSubmenu.add(synchronizedJCBMI=new JCheckBoxMenuItem("synchronized"));
        menu.add(modifiersSubmenu);
        
        menu.addSeparator();
        
        (deleteMethod= new JMenuItem ("Delete Method")).addActionListener(removeWidgetListener);
        menu.add(deleteMethod);
    }
        ActionListener removeWidgetListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            //((MethodWidget)methodWidget).getMember().getDeclaringClass()
            methodWidget.removeFromParent();
        }
    };
                
       
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
        
}
