package org.uml.visual.widgets.providers.popups;

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
import org.openide.windows.WindowManager;
import org.uml.model.members.Literal;
import org.uml.visual.dialogs.PackageDialog;
import org.uml.visual.widgets.components.EnumWidget;
import org.uml.visual.widgets.members.LiteralWidget;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.MouseAdapterZaView;

/**
 *
 * @author Jelena
 */
public class EnumPopupMenuProvider implements PopupMenuProvider {

    private EnumWidget enumWidget;
    private JPopupMenu menu;
    private JMenuItem deleteClass;
    private JMenuItem addLiteral;
    private JMenuItem addField;
    private JMenuItem addMethod;
    private JMenuItem addConstructor;
    private JMenuItem addPackage;
    WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(enumWidget));
    MouseListener mouseListener = new MouseAdapterZaView(editorAction);

    public EnumPopupMenuProvider(EnumWidget enumWidget) {
        this.enumWidget = enumWidget;
        menu = new JPopupMenu("Enum Menu");

        (addLiteral = new JMenuItem("Add Literal")).addActionListener(addLiteralListener);
        menu.add(addLiteral);
//        (addConstructor = new JMenuItem("Add Constructor")).addActionListener(addConstructorListener);
//        menu.add(addConstructor);
//        (addField = new JMenuItem("Add Field")).addActionListener(addAtributeListener);
//        menu.add(addField);
//        (addMethod = new JMenuItem("Add Method")).addActionListener(addMethodListener);
//        menu.add(addMethod);
        (addPackage = new JMenuItem("Set Package")).addActionListener(addPackageListener);
        menu.add(addPackage);
        (deleteClass = new JMenuItem("Delete Enum")).addActionListener(removeWidgetListener);
        menu.add(deleteClass);

    }
//    ActionListener addConstructorListener = new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            Constructor c = new Constructor(enumWidget.getName());
//            enumWidget.getComponent().addConstructor(c);
//            ConstructorWidget w = new ConstructorWidget(enumWidget.getClassDiagramScene(), c);
//            enumWidget.addConstructorWidget(w);
//            enumWidget.getScene().validate();
//        }
//    };
    ActionListener addLiteralListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            Literal l = new Literal("LITERAL");
            LiteralWidget w = new LiteralWidget(enumWidget.getClassDiagramScene(), l);
            enumWidget.getComponent().addLiteral(l);
            enumWidget.addLiteralWidget(w);
            enumWidget.getScene().validate();

            WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(w));
            ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(w.getNameLabel());
            MouseListener mouseListener = new MouseAdapterZaView(nameEditorAction);
            enumWidget.getScene().getView().addMouseListener(mouseListener);
        }
    };
    ActionListener addPackageListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
//            String pack = "";
            PackageDialog pd = new PackageDialog(null, true, enumWidget.getComponent(), enumWidget.getClassDiagramScene().getClassDiagram());
            pd.setLocationRelativeTo(WindowManager.getDefault().getMainWindow());
            pd.setTitle("Package");
            pd.setVisible(true);

//            classWidget.getComponent().setPack(pack);
//            Constructor c = new Constructor(classWidget.getName());
//            classWidget.getComponent().addConstructor(c);
//            ConstructorWidget w = new ConstructorWidget(classWidget.getClassDiagramScene(), c);
//            classWidget.addConstructorWidget(w);
            enumWidget.getScene().validate();

//            w.getActions().addAction(classWidget.getScene().createWidgetHoverAction());
        }
    };
    ActionListener removeWidgetListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            enumWidget.getComponent().getParentDiagram().removeComponent(enumWidget.getName());
            enumWidget.removeFromParent();
        }
    };
//    ActionListener addAtributeListener = new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//
//            Field f = new Field("untitledField", null, Visibility.PRIVATE);
//            try {
//                enumWidget.getComponent().addField(f);
//            } catch (RuntimeException ex) {
//                Random r = new Random();
//                int i = r.nextInt(10000);
//                f.setName(f.getName() + i);
//                enumWidget.getComponent().addField(f);
//            }
//            FieldWidget w = new FieldWidget(enumWidget.getClassDiagramScene(), f);
//            enumWidget.addFieldWidget(w);
//            enumWidget.getScene().validate();
//
//            WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(w));
//            ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(w.getNameLabel());
//            MouseListener mouseListener = new MouseAdapterZaView(nameEditorAction);
//            enumWidget.getScene().getView().addMouseListener(mouseListener);
//        }
//    };
//    ActionListener addMethodListener = new ActionListener() {
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            Method m = new Method("untitledMethod", null, new HashMap<String, MethodArgument>());
//            try {
//                enumWidget.getComponent().addMethod(m);
//
//            } catch (RuntimeException ex) {
//                Random r = new Random();
//                int i = r.nextInt(10000);
//                m.setName(m.getName() + i);
//                enumWidget.getComponent().addMethod(m);
//            }
//            MethodWidget w = new MethodWidget(enumWidget.getClassDiagramScene(), m);
//            enumWidget.addMethodWidget(w);
//            enumWidget.getScene().validate();
//
//            WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(w));
//            ActionFactory.getInplaceEditorController(nameEditorAction).openEditor(w.getNameLabel());
//            MouseListener mouseListener = new MouseAdapterZaView(nameEditorAction);
//            enumWidget.getScene().getView().addMouseListener(mouseListener);
//        }
//    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
