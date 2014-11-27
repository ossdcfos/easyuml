package org.uml.visual.widgets.providers.popups;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JPopupMenu;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.members.Field;
import org.uml.model.members.MemberBase;
import org.uml.model.Visibility;
import static org.uml.model.Visibility.PACKAGE;
import static org.uml.model.Visibility.PRIVATE;
import static org.uml.model.Visibility.PROTECTED;
import static org.uml.model.Visibility.PUBLIC;
import org.uml.visual.widgets.members.FieldWidget;
import org.uml.visual.widgets.members.MemberWidgetBase;

/**
 *
 * @author Jelena
 */
public class FieldPopupMenuProvider extends MemberBasePopupProvider {

    private JMenu visibilitySubmenu;
    private JMenu modifiersSubmenu;
    private ButtonGroup visibilityGroup;
    private JCheckBoxMenuItem privateItem;
    private JCheckBoxMenuItem publicItem;
    private JCheckBoxMenuItem protectedItem;
    private JCheckBoxMenuItem packageItem;
    private JCheckBoxMenuItem staticJCBMI;
    private JCheckBoxMenuItem finalJCBMI;
    private JCheckBoxMenuItem synchronizedJCBMI;

    public FieldPopupMenuProvider(MemberWidgetBase widget) {
        super(widget);

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
            ((Field) widget.getMember()).setVisibility(Visibility.PUBLIC);
            ((FieldWidget)widget).refreshVisibilityLabel();
        }
    };
    ActionListener privateItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((Field) widget.getMember()).setVisibility(Visibility.PRIVATE);
            ((FieldWidget)widget).refreshVisibilityLabel();

        }
    };
    ActionListener protectedItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((Field) widget.getMember()).setVisibility(Visibility.PROTECTED);
            ((FieldWidget)widget).refreshVisibilityLabel();
        }
    };
    ActionListener packageItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ((Field) widget.getMember()).setVisibility(Visibility.PACKAGE);
            ((FieldWidget)widget).refreshVisibilityLabel();
        }
    };
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            MemberBase member = widget.getMember();
            member.getDeclaringClass().removeMember(widget.getName());
            member.getDeclaringClass().removeMemberFromContainer(member);
            widget.getClassDiagramScene().removeObject(member);
            widget.removeFromParent();
        }
    };
    ActionListener staticJCBMIListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Field field = (Field) widget.getMember();
            if (staticJCBMI.getState()) {
                if (!field.isStatic()) {
                    field.setStatic(true);
                }
            } else {
                if (field.isStatic()) {
                    field.setStatic(false);
                }
            }
            ((FieldWidget)widget).getFieldNameWidget().setLabel(((Field) widget.getMember()).getSignatureForLabel());
        }
    };
    ActionListener finalJCBMIListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Field field = (Field) widget.getMember();
            if (finalJCBMI.getState()) {
                if (!field.isFinal()) {
                    field.setFinal(true);
                }
            } else {
                if (field.isFinal()) {
                    field.setFinal(false);
                }
            }
            ((FieldWidget)widget).getFieldNameWidget().setLabel(((Field) widget.getMember()).getSignatureForLabel());
        }
    };
    ActionListener synchronizedJCBMIListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Field field = (Field) widget.getMember();
            if (synchronizedJCBMI.getState()) {
                if (!field.isSynchronized()) {
                    field.setSynchronized(true);
                }
            } else {
                if (field.isSynchronized()) {
                    field.setSynchronized(false);
                }
            }
            ((FieldWidget)widget).getFieldNameWidget().setLabel(((Field) widget.getMember()).getSignatureForLabel());
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }

    private void setSelectedButtons() {
        Field field = (Field) widget.getMember();
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
            if (field.isStatic()) {
                staticJCBMI.setSelected(true);
            }
            if (field.isSynchronized()) {
                finalJCBMI.setSelected(true);
            }
            if (field.isSynchronized()) {
                synchronizedJCBMI.setSelected(true);
            }

        }

    }
}
