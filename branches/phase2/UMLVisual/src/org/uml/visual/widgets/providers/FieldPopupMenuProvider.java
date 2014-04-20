/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.providers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JRadioButton;
import org.netbeans.api.visual.action.PopupMenuProvider;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassComponent;
import org.uml.model.Field;
import org.uml.model.Method;
import org.uml.model.Visibility;
import static org.uml.model.Visibility.PACKAGE;
import static org.uml.model.Visibility.PRIVATE;
import static org.uml.model.Visibility.PROTECTED;
import static org.uml.model.Visibility.PUBLIC;
import org.uml.visual.widgets.FieldWidget;

/**
 *
 * @author Jelena
 */
public class FieldPopupMenuProvider implements PopupMenuProvider {

    private FieldWidget fieldWidget;
    private JPopupMenu menu;
    private JMenuItem deleteField;
    private JMenu visibilitySubmenu;
    private JMenu modifiersSubmenu;
    private ButtonGroup visibilityGroup;
    private JRadioButton privateItem;
    private JRadioButton publicItem;
    private JRadioButton protectedItem;
    private JRadioButton packageItem;
    private JCheckBoxMenuItem staticJCBMI;
    private JCheckBoxMenuItem finalJCBMI;
    private JCheckBoxMenuItem synchronizedJCBMI;

    public FieldPopupMenuProvider(FieldWidget fieldWidget) {
        this.fieldWidget = fieldWidget;
        menu = new JPopupMenu("Class Menu");

        visibilityGroup = new ButtonGroup();
        visibilitySubmenu = new JMenu("Visibility");
        visibilitySubmenu.add(publicItem = new JRadioButton("public"));
        publicItem.addActionListener(publicItemListener);
        visibilitySubmenu.add(privateItem = new JRadioButton("private"));
        privateItem.addActionListener(privateItemListener);
        visibilitySubmenu.add(protectedItem = new JRadioButton("protected"));
        protectedItem.addActionListener(protectedItemListener);
        visibilitySubmenu.add(packageItem = new JRadioButton("package"));
        packageItem.addActionListener(packageItemListener);
        visibilityGroup.add(publicItem);
        visibilityGroup.add(privateItem);
        visibilityGroup.add(protectedItem);
        visibilityGroup.add(packageItem);
        menu.add(visibilitySubmenu);

        modifiersSubmenu = new JMenu("Modifiers");
        modifiersSubmenu.add(staticJCBMI = new JCheckBoxMenuItem("static"));
        staticJCBMI.addActionListener(staticJCBMIListener);
        modifiersSubmenu.add(finalJCBMI = new JCheckBoxMenuItem("final"));
        finalJCBMI.addActionListener(finalJCBMIListener);
        modifiersSubmenu.add(synchronizedJCBMI = new JCheckBoxMenuItem("synchronized"));
        synchronizedJCBMI.addActionListener(synchronizedJCBMIListener);
        menu.add(modifiersSubmenu);

        menu.addSeparator();
        (deleteField = new JMenuItem("Delete Field")).addActionListener(removeWidgetListener);
        menu.add(deleteField);

        setSelectedButtons();
    }
    ActionListener publicItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((Field) fieldWidget.getMember()).setVisibility(Visibility.PUBLIC);
            fieldWidget.refreshLabel();
        }
    };
    ActionListener privateItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((Field) fieldWidget.getMember()).setVisibility(Visibility.PRIVATE);
            fieldWidget.refreshLabel();

        }
    };
    ActionListener protectedItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((Field) fieldWidget.getMember()).setVisibility(Visibility.PROTECTED);
            fieldWidget.refreshLabel();
        }
    };
    ActionListener packageItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((Field) fieldWidget.getMember()).setVisibility(Visibility.PACKAGE);
            fieldWidget.refreshLabel();
        }
    };
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            fieldWidget.getMember().getDeclaringClass().removeMember(fieldWidget.getName());
            if (fieldWidget.getMember().getDeclaringClass() instanceof ClassComponent) {
                ((ClassComponent) fieldWidget.getMember().getDeclaringClass()).removeField((Field) fieldWidget.getMember());
            }

            fieldWidget.removeFromParent();
        }
    };
    ActionListener staticJCBMIListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Field field = (Field) fieldWidget.getMember();
            if (staticJCBMI.getState()) {
                if (!field.isIsStatic()) {
                    field.setIsStatic(true);
                }
            } else {
                if (field.isIsStatic()) {
                    field.setIsStatic(false);
                }
            }
            fieldWidget.getFieldNameWidget().setLabel(((Field) fieldWidget.getMember()).getSignatureForLabel());
        }
    };
    ActionListener finalJCBMIListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Field field = (Field) fieldWidget.getMember();
            if (finalJCBMI.getState()) {
                if (!field.isIsFinal()) {
                    field.setIsFinal(true);
                }
            } else {
                if (field.isIsFinal()) {
                    field.setIsFinal(false);
                }
            }
            fieldWidget.getFieldNameWidget().setLabel(((Field) fieldWidget.getMember()).getSignatureForLabel());
        }
    };
    ActionListener synchronizedJCBMIListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Field field = (Field) fieldWidget.getMember();
            if (synchronizedJCBMI.getState()) {
                if (!field.isIsSynchronized()) {
                    field.setIsSynchronized(true);
                }
            } else {
                if (field.isIsSynchronized()) {
                    field.setIsSynchronized(false);
                }
            }
            fieldWidget.getFieldNameWidget().setLabel(((Field) fieldWidget.getMember()).getSignatureForLabel());
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }

    private void setSelectedButtons() {
        Field field = fieldWidget.getFieldComponent();
        publicItem.setSelected(false);
        privateItem.setSelected(false);
        packageItem.setSelected(false);
        protectedItem.setSelected(false);
        staticJCBMI.setSelected(false);
        finalJCBMI.setSelected(false);
        synchronizedJCBMI.setSelected(false);
        if (field != null) {
            switch (field.getVisibility()) {
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
            if (field.isIsStatic()) {
                staticJCBMI.setSelected(true);
            }
            if (field.isIsSynchronized()) {
                finalJCBMI.setSelected(true);
            }
            if (field.isIsSynchronized()) {
                synchronizedJCBMI.setSelected(true);
            }

        }

    }
}
