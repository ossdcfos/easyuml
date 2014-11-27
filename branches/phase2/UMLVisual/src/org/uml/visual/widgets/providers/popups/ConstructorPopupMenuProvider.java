package org.uml.visual.widgets.providers.popups;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import org.uml.visual.widgets.members.MemberWidgetBase;

/**
 *
 * @author Jelena
 */
public class ConstructorPopupMenuProvider extends MemberBasePopupProvider {

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

    public ConstructorPopupMenuProvider(MemberWidgetBase constructorWidget) {
        super(constructorWidget);
        
        visibilitySubmenu = new JMenu("Visibility");
        visibilitySubmenu.add(privateItem = new JMenuItem("public"));
        visibilitySubmenu.add(publicItem = new JMenuItem("private"));
        visibilitySubmenu.add(protectedItem = new JMenuItem("protected"));
        visibilitySubmenu.add(packageItem = new JMenuItem("package"));
        menu.add(visibilitySubmenu);

        menu.addSeparator();
        
        addDelete();
    }
}
