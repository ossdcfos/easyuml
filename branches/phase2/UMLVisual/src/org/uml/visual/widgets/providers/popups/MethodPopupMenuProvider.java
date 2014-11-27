package org.uml.visual.widgets.providers.popups;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Modifier;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.components.ClassComponent;
import org.uml.model.components.ComponentBase;
import org.uml.model.members.MemberBase;
import org.uml.model.members.MethodBase;
import org.uml.model.Visibility;
import org.uml.visual.widgets.members.MemberWidgetBase;
import org.uml.visual.widgets.members.MethodWidget;

/**
 *
 * @author "NUGS"
 */
public class MethodPopupMenuProvider extends MemberBasePopupProvider {

    private JMenu visibilitySubmenu;
    private JMenu modifiersSubmenu;
    private ButtonGroup visibilityGroup;
    private JCheckBoxMenuItem privateItem;
    private JCheckBoxMenuItem publicItem;
    private JCheckBoxMenuItem protectedItem;
    private JCheckBoxMenuItem packageItem;
    private JCheckBoxMenuItem staticJCBMI;
    private JCheckBoxMenuItem abstractJCBMI;
    private JCheckBoxMenuItem finalJCBMI;
    private JCheckBoxMenuItem synchronizedJCBMI;

    public MethodPopupMenuProvider(MemberWidgetBase methodWidget) {
        super(methodWidget);

        visibilityGroup = new ButtonGroup();
        visibilitySubmenu = new JMenu("Visibility");
        visibilitySubmenu.add(publicItem = new JCheckBoxMenuItem("public"));
        publicItem.addActionListener(publicItemListener);
        visibilitySubmenu.add(privateItem = new JCheckBoxMenuItem("private"));
        privateItem.addActionListener(privateItemListener);
        visibilitySubmenu.add(protectedItem = new JCheckBoxMenuItem("protected"));
        protectedItem.addActionListener(protectedItemListener);
        visibilitySubmenu.add(packageItem = new JCheckBoxMenuItem("package"));
        packageItem.addActionListener(packageItemListener);
        visibilityGroup.add(publicItem);
        visibilityGroup.add(privateItem);
        visibilityGroup.add(protectedItem);
        visibilityGroup.add(packageItem);
        menu.add(visibilitySubmenu);

        modifiersSubmenu = new JMenu("Modifiers");
        modifiersSubmenu.add(staticJCBMI = new JCheckBoxMenuItem("static"));
        staticJCBMI.addActionListener(staticJCBMIListener);
        modifiersSubmenu.add(abstractJCBMI = new JCheckBoxMenuItem("abstract"));
        abstractJCBMI.addActionListener(abstractJCBMIListener);
        modifiersSubmenu.add(finalJCBMI = new JCheckBoxMenuItem("final"));
        finalJCBMI.addActionListener(finalJCBMIListener);
        modifiersSubmenu.add(synchronizedJCBMI = new JCheckBoxMenuItem("synchronized"));
        synchronizedJCBMI.addActionListener(synchronizedJCBMIListener);
        menu.add(modifiersSubmenu);

        menu.addSeparator();

        addDelete();

        setSelectedButtons();

    }

    ActionListener publicItemListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            ((MethodBase) widget.getMember()).setVisibility(Visibility.PUBLIC);
            ((MethodWidget)widget).refreshLabel();
        }
    };

    ActionListener privateItemListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            ((MethodBase) widget.getMember()).setVisibility(Visibility.PRIVATE);
            ((MethodWidget)widget).refreshLabel();
        }
    };

    ActionListener protectedItemListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            ((MethodBase) widget.getMember()).setVisibility(Visibility.PROTECTED);
            ((MethodWidget)widget).refreshLabel();
        }
    };

    ActionListener packageItemListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            ((MethodBase) widget.getMember()).setVisibility(Visibility.PACKAGE);
            ((MethodWidget)widget).refreshLabel();
        }
    };
    ActionListener staticJCBMIListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            MethodBase m = (MethodBase) widget.getMember();
            if (staticJCBMI.getState()) {
                if (!m.hasConcreteModifier("static")) {
                    m.addModifier(Modifier.STATIC);
                }
            } else {
                if (m.hasConcreteModifier("static")) {
                    m.deleteModifier(Modifier.STATIC);
                }
            }
            ((MethodWidget)widget).getMethodNameWidget().setLabel(((MethodBase) widget.getMember()).getSignatureForLabel());
        }
    };
    ActionListener abstractJCBMIListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            MethodBase m = (MethodBase) widget.getMember();
            if (abstractJCBMI.getState()) {
                if (!m.hasConcreteModifier("abstract")) {
                    m.addModifier(Modifier.ABSTRACT);
                }
            } else {
                if (m.hasConcreteModifier("abstract")) {
                    m.deleteModifier(Modifier.ABSTRACT);
                }
            }
            ((MethodWidget)widget).getMethodNameWidget().setLabel(((MethodBase) widget.getMember()).getSignatureForLabel());
        }
    };
    ActionListener finalJCBMIListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            MethodBase m = (MethodBase) widget.getMember();
            if (finalJCBMI.getState()) {
                if (!m.hasConcreteModifier("final")) {
                    m.addModifier(Modifier.FINAL);
                }
            } else {
                if (m.hasConcreteModifier("final")) {
                    m.deleteModifier(Modifier.FINAL);
                }
            }
            ((MethodWidget)widget).getMethodNameWidget().setLabel(((MethodBase) widget.getMember()).getSignatureForLabel());
        }
    };
    ActionListener synchronizedJCBMIListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            MethodBase m = (MethodBase) widget.getMember();
            if (synchronizedJCBMI.getState()) {
                if (!m.hasConcreteModifier("synchronized")) {
                    m.addModifier(Modifier.SYNCHRONIZED);
                }
            } else {
                if (m.hasConcreteModifier("synchronized")) {
                    m.deleteModifier(Modifier.SYNCHRONIZED);
                }
            }
            ((MethodWidget)widget).getMethodNameWidget().setLabel(((MethodBase) widget.getMember()).getSignatureForLabel());
        }
    };

    private void setSelectedButtons() {
        MethodBase method = (MethodBase) widget.getMember();
        publicItem.setSelected(false);
        privateItem.setSelected(false);
        packageItem.setSelected(false);
        protectedItem.setSelected(false);
        staticJCBMI.setSelected(false);
        finalJCBMI.setSelected(false);
        abstractJCBMI.setSelected(false);
        synchronizedJCBMI.setSelected(false);
        switch (method.getVisibility()) {
            case PUBLIC:
                publicItem.setSelected(true);
                break;
            case PRIVATE:
                privateItem.setSelected(true);
                break;
            case PACKAGE:
                packageItem.setSelected(true);
                break;
            case PROTECTED:
                protectedItem.setSelected(true);
                break;
        }
        if (method.hasConcreteModifier("static")) {
            staticJCBMI.setSelected(true);
        }

        if (method.hasConcreteModifier("final")) {
            finalJCBMI.setSelected(true);
        }

        if (method.hasConcreteModifier("abstract")) {
            abstractJCBMI.setSelected(true);
        }

        if (method.hasConcreteModifier("synchronized")) {
            synchronizedJCBMI.setSelected(true);
        }

    }

}
