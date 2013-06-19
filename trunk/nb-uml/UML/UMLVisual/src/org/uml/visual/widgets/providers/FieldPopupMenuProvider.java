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
import org.uml.model.Field;
import org.uml.model.Visibility;
import org.uml.visual.widgets.FieldWidget;

/**
 *
 * @author Jelena
 */
public class FieldPopupMenuProvider implements PopupMenuProvider{

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
    private JCheckBoxMenuItem abstractJCBMI;
    private JCheckBoxMenuItem finalJCBMI;
    private JCheckBoxMenuItem synchronizedJCBMI;
    
    public FieldPopupMenuProvider(FieldWidget fieldWidget) {
        this.fieldWidget = fieldWidget;
        menu = new JPopupMenu("Class Menu");
        
        visibilityGroup = new ButtonGroup();       
        visibilitySubmenu = new JMenu("Visibility");
        visibilitySubmenu.add(publicItem = new JRadioButton("public"));
        publicItem.addActionListener(publicItemListener);
        visibilitySubmenu.add(privateItem =new JRadioButton("private"));
        privateItem.addActionListener(privateItemListener);
        visibilitySubmenu.add(protectedItem =new JRadioButton("protected"));
        protectedItem.addActionListener(protectedItemListener);
        visibilitySubmenu.add(packageItem =new JRadioButton("package"));
        visibilityGroup.add(publicItem);
        visibilityGroup.add(privateItem);
        visibilityGroup.add(protectedItem);
        visibilityGroup.add(packageItem);
        menu.add(visibilitySubmenu);
        
        modifiersSubmenu= new JMenu("Modifiers");
        modifiersSubmenu.add(staticJCBMI= new JCheckBoxMenuItem("static"));
        staticJCBMI.addActionListener(staticJCBMIListener);
        modifiersSubmenu.add(abstractJCBMI=new JCheckBoxMenuItem("abstract"));
        abstractJCBMI.addActionListener(abstractJCBMIListener);
        modifiersSubmenu.add(finalJCBMI=new JCheckBoxMenuItem("final"));
        finalJCBMI.addActionListener(finalJCBMIListener);
        modifiersSubmenu.add(synchronizedJCBMI=new JCheckBoxMenuItem("synchronized"));
        synchronizedJCBMI.addActionListener(synchronizedJCBMIListener);
        menu.add(modifiersSubmenu);
        
        menu.addSeparator();
        (deleteField = new JMenuItem("Delete Field")).addActionListener(removeWidgetListener);
        menu.add(deleteField);
    }
    
    ActionListener publicItemListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            ((Field)fieldWidget.getMember()).setVisibility(Visibility.PUBLIC);
                     
        }
    };
    
    ActionListener privateItemListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            ((Field)fieldWidget.getMember()).setVisibility(Visibility.PRIVATE);
        }
    };
    
    ActionListener protectedItemListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            ((Field)fieldWidget.getMember()).setVisibility(Visibility.PROTECTED);
        }
    };
    
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
           fieldWidget.getMember().getDeclaringClass().removeMember(fieldWidget.getName());
           fieldWidget.removeFromParent();
        }
    };
    
    ActionListener staticJCBMIListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Field field = (Field)fieldWidget.getMember();
            if (field.isIsStatic()==false) {
                field.setIsStatic(true);
            }
            else {
                field.setIsStatic(false);
            }
        }
    };
    
    ActionListener abstractJCBMIListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Field field = (Field)fieldWidget.getMember();
            if(field.isIsAbstract()==false) {
                field.setIsAbstract(true);
            }
            else {
                field.setIsAbstract(false);
            }
        }
    };
    
    ActionListener finalJCBMIListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Field field = (Field)fieldWidget.getMember();
            if(field.isIsFinal()==false) {
                field.setIsFinal(true);
            }
            else {
                field.setIsFinal(false);
            }
        }
        
    };
    
    ActionListener synchronizedJCBMIListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Field field = (Field)fieldWidget.getMember();
            if(field.isIsSynchronised()==false) {
                field.setIsSynchronised(true);
            }
            else {
                field.setIsSynchronised(false);
            }
        }
        
    };
    
    
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }

    }
