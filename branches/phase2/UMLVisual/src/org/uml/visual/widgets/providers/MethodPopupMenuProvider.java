package org.uml.visual.widgets.providers;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Modifier;
import java.util.HashMap;
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
import org.uml.visual.widgets.MethodWidget;

/**
 *
 * @author "NUGS"
 */
public class MethodPopupMenuProvider implements PopupMenuProvider{
    
        private MethodWidget methodWidget;
        private JPopupMenu menu;
        private JMenuItem deleteMethod;
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

    public MethodPopupMenuProvider(MethodWidget methodWidget) {
        this.methodWidget = methodWidget;
        menu= new JPopupMenu ("Class Menu");
        
        visibilityGroup = new ButtonGroup();
        visibilitySubmenu = new JMenu("Visibility");
        visibilitySubmenu.add(publicItem = new JRadioButton("public"));
        publicItem.addActionListener(publicItemListener);
        visibilitySubmenu.add(privateItem =new JRadioButton("private"));
        privateItem.addActionListener(privateItemListener);
        visibilitySubmenu.add(protectedItem =new JRadioButton("protected"));
        protectedItem.addActionListener(protectedItemListener);
        visibilitySubmenu.add(packageItem =new JRadioButton("package"));
        packageItem.addActionListener(packageItemListener);
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
        
        (deleteMethod= new JMenuItem ("Delete Method")).addActionListener(removeWidgetListener);
        menu.add(deleteMethod);
    }
    
        ActionListener publicItemListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            ((Method)methodWidget.getMember()).setVisibility(Visibility.PUBLIC);
            methodWidget.refreshLabel();
        }
    };
        
          ActionListener privateItemListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            ((Method)methodWidget.getMember()).setVisibility(Visibility.PRIVATE);
            methodWidget.refreshLabel();
        }
    };
    
        ActionListener protectedItemListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            ((Method)methodWidget.getMember()).setVisibility(Visibility.PROTECTED);
            methodWidget.refreshLabel();
        }
    };
    
        ActionListener packageItemListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            ((Method)methodWidget.getMember()).setVisibility(Visibility.PACKAGE);
            methodWidget.refreshLabel();
        }
    };
        ActionListener staticJCBMIListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Method m = (Method)methodWidget.getMember();
            if(staticJCBMI.getState()){
                if(!m.hasConcreteModifier("static")) {
                    m.addModifier(Modifier.STATIC);
                }
            }else {
                if(m.hasConcreteModifier("static")) {
                    m.deleteModifier(Modifier.STATIC);
                }
            }
            methodWidget.getMethodNameWidget().setLabel(((Method) methodWidget.getMember()).getSignatureForLabel());
        }
    };
        ActionListener abstractJCBMIListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Method m = (Method)methodWidget.getMember();
            if(abstractJCBMI.getState()){
                if(!m.hasConcreteModifier("abstract")) {
                    m.addModifier(Modifier.ABSTRACT);
                }
            }else {
                if(m.hasConcreteModifier("abstract")) {
                    m.deleteModifier(Modifier.ABSTRACT);
                }
            }
            methodWidget.getMethodNameWidget().setLabel(((Method) methodWidget.getMember()).getSignatureForLabel());
        }
    };
        ActionListener finalJCBMIListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Method m = (Method)methodWidget.getMember();
            if(finalJCBMI.getState()){
                if(!m.hasConcreteModifier("final")) {
                    m.addModifier(Modifier.FINAL);
                }
            }else {
                if(m.hasConcreteModifier("final")) {
                    m.deleteModifier(Modifier.FINAL);
                }
            }
            methodWidget.getMethodNameWidget().setLabel(((Method) methodWidget.getMember()).getSignatureForLabel());
        }
    };
        ActionListener synchronizedJCBMIListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            Method m = (Method)methodWidget.getMember();
            if(synchronizedJCBMI.getState()){
                if(!m.hasConcreteModifier("synchronized")) {
                    m.addModifier(Modifier.SYNCHRONIZED);
                }
            }else {
                if(m.hasConcreteModifier("synchronized")) {
                    m.deleteModifier(Modifier.SYNCHRONIZED);
                }
            }
            methodWidget.getMethodNameWidget().setLabel(((Method) methodWidget.getMember()).getSignatureForLabel());
        }
    };
        ActionListener removeWidgetListener = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
           methodWidget.getMember().getDeclaringClass().removeMember(methodWidget.getName());
           if(methodWidget.getMember().getDeclaringClass() instanceof ClassComponent) {
               ((ClassComponent) methodWidget.getMember().getDeclaringClass()).removeMethod((Method) methodWidget.getMember());
           }
           methodWidget.removeFromParent();
        }
    };
                
       
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
        
}
