package org.uml.visual.widgets.providers.popups;

import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import javax.swing.*;
import org.netbeans.api.visual.action.*;
import org.netbeans.api.visual.widget.*;
import org.openide.windows.WindowManager;
import org.uml.model.components.ClassComponent;
import org.uml.model.ClassDiagram;
import org.uml.model.members.*;
import org.uml.model.Visibility;
import static org.uml.model.Visibility.*;
import org.uml.visual.dialogs.PackageDialog;
import org.uml.visual.widgets.components.ClassWidget;
import org.uml.visual.widgets.members.*;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.MouseAdapterZaView;

/**
 *
 * @author hrza
 */
public class ClassPopupMenuProvider implements PopupMenuProvider {

    private ClassWidget classWidget;
    private JPopupMenu menu;
    private JMenuItem deleteClass;
    private JMenuItem addField;
    private JMenuItem addMethod;
    private JMenuItem addConstructor;
    private JMenuItem addPackage;
    private JMenu visibilitySubmenu;
    private ButtonGroup visibilityGroup;
    private JCheckBoxMenuItem privateItem;
    private JCheckBoxMenuItem publicItem;
    private JCheckBoxMenuItem protectedItem;
    private JCheckBoxMenuItem packageItem;
    private JCheckBoxMenuItem abstractJCBMI;
    WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(classWidget));
    MouseListener mouseListener = new MouseAdapterZaView(editorAction);

    public ClassPopupMenuProvider(ClassWidget classWidget) {
        this.classWidget = classWidget;
        menu = new JPopupMenu("Class Menu");

        (addConstructor = new JMenuItem("Add Constructor")).addActionListener(addConstructorListener);
        menu.add(addConstructor);

        (addField = new JMenuItem("Add Field")).addActionListener(addAtributeListener);
        menu.add(addField);
        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
        menu.add(addMethod);

        (addPackage = new JMenuItem("Set Package")).addActionListener(addPackageListener);
        menu.add(addPackage);

        menu.addSeparator();
        (deleteClass = new JMenuItem("Delete Class")).addActionListener(removeWidgetListener);
        menu.add(deleteClass);

        menu.addSeparator();

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

        menu.add(abstractJCBMI = new JCheckBoxMenuItem("abstract"));
        abstractJCBMI.addActionListener(abstractJCBMIListener);
        setSelectedButtons();
    }
    
    /**
     * Remove Widget Listener
     *
     *
     */
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ClassDiagram classDiagram = classWidget.getComponent().getParentDiagram();
            classDiagram.removeComponent(classWidget.getName());
//            classWidget.removeFromParent();
//            
//            for (Map.Entry<String, RelationComponent> entry : classDiagram.getRelations().entrySet()) {
//                RelationComponent relation = entry.getValue();
//                if (relation.getSource().getName().equals(classWidget.getName()) || relation.getTarget().getName().equals(classWidget.getName())) {
//                    classDiagram.removeRelation(relation.getName());
//                    classWidget.getClassDiagramScene().removeEdge(relation);
//                }
//            }
//            classWidget.getClassDiagramScene().removeNode(classWidget.getComponent());
        }
    };
    
    ActionListener addAtributeListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Field f = new Field("untitledField", null, Visibility.PRIVATE);
            addField(f);
            FieldWidget w = new FieldWidget(classWidget.getClassDiagramScene(), f);
            classWidget.addFieldWidget(w);
            classWidget.getScene().validate();

            WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(w));
            ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(w.getNameLabel());
            MouseListener mouseListener = new MouseAdapterZaView(nameEditorAction);
            classWidget.getScene().getView().addMouseListener(mouseListener);
        }
    };
    
    ActionListener addMethodListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Method m = new Method("untitledMethod", null, new HashMap<String, MethodArgument>());
            addMethod(m);
            MethodWidget w = new MethodWidget(classWidget.getClassDiagramScene(), m);
            classWidget.addMethodWidget(w);
            classWidget.getScene().validate();

            WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(w));
            ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(w.getNameLabel());
            MouseListener mouseListener = new MouseAdapterZaView(nameEditorAction);
            classWidget.getScene().getView().addMouseListener(mouseListener);

        }
    };
    ActionListener addConstructorListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Constructor c = new Constructor(classWidget.getName());
            classWidget.getComponent().addConstructor(c);
            ConstructorWidget w = new ConstructorWidget(classWidget.getClassDiagramScene(), c);
            classWidget.addConstructorWidget(w);
            classWidget.getScene().validate();
        }
    };
    
    ActionListener addPackageListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            String pack = "";
            PackageDialog pd = new PackageDialog(null, true, classWidget.getComponent(), classWidget.getClassDiagramScene().getClassDiagram());
            pd.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            pd.setTitle("Package");
            pd.setVisible(true);

//            classWidget.getComponent().setPack(pack);
//            Constructor c = new Constructor(classWidget.getName());
//            classWidget.getComponent().addConstructor(c);
//            ConstructorWidget w = new ConstructorWidget(classWidget.getClassDiagramScene(), c);
//            classWidget.addConstructorWidget(w);
            classWidget.getScene().validate();

//            w.getActions().addAction(classWidget.getScene().createWidgetHoverAction());
        }
    };
    
    ActionListener publicItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.getComponent().setVisibility(Visibility.PUBLIC);

        }
    };
    
    ActionListener privateItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.getComponent().setVisibility(Visibility.PRIVATE);
        }
    };
    
    ActionListener protectedItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.getComponent().setVisibility(Visibility.PROTECTED);
        }
    };
    
    ActionListener packageItemListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            classWidget.getComponent().setVisibility(Visibility.PACKAGE);
        }
    };
    
    ActionListener abstractJCBMIListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Widget classNameWidget = classWidget.getChildren().get(0);
            ClassComponent classComponent = classWidget.getComponent();
            if (classComponent.isAbstract() == false) {
                classComponent.setAbstract(true);

                LabelWidget abstractLabel = new LabelWidget(classWidget.getScene(), "<<abstract>>");
                abstractLabel.setFont(classWidget.getScene().getDefaultFont().deriveFont(Font.ITALIC));
                abstractLabel.setAlignment(LabelWidget.Alignment.CENTER);

                classNameWidget.addChild(0, abstractLabel);

            } else {
                classComponent.setAbstract(false);
                classNameWidget.removeChild(classNameWidget.getChildren().get(0));
            }

        }
    };

    // TODO Dodati jos listenera za ClassWidgetMeni
    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }

    private void setSelectedButtons() {
        ClassComponent classComponent = classWidget.getComponent();
        publicItem.setSelected(false);
        privateItem.setSelected(false);
        packageItem.setSelected(false);
        protectedItem.setSelected(false);
        abstractJCBMI.setSelected(false);
        switch (classComponent.getVisibility()) {
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
        if (classComponent.isAbstract()) {
            abstractJCBMI.setSelected(true);
        }
    }

    private int getCounter(MemberBase member) {
        int brojac = 1;
        String name = member.getName();
        String broj = name.substring(name.length() - 1);
        if (broj.matches("[0-9]")) {
            name = name.substring(0, name.length() - 1);
            member.setName(name);
            brojac = Integer.parseInt(broj) + 1;
        }
        return brojac;
    }

    private void addField(Field f) {
        try {
            classWidget.getComponent().addField(f);
        } catch (RuntimeException ex) {
            int counter = getCounter(f);
            f.setName(f.getName() + counter);
            addField(f);
        }
    }

    private void addMethod(Method method) {
        try {
            classWidget.getComponent().addMethod(method);
        }catch (RuntimeException ex) {
            int counter = getCounter(method);
            method.setName(method.getName() + counter);
            addMethod(method);
        }
    }
    
}
