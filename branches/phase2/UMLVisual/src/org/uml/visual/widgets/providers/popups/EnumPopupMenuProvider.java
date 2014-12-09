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
    private JMenuItem editPackage;
    WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(enumWidget));
    MouseListener mouseListener = new MouseAdapterZaView(editorAction);

    public EnumPopupMenuProvider(EnumWidget enumWidget) {
        this.enumWidget = enumWidget;
        menu = new JPopupMenu("Enum Menu");

        (addLiteral = new JMenuItem("Add Literal")).addActionListener(addLiteralListener);
        menu.add(addLiteral);
        
        menu.addSeparator();
        
        (editPackage = new JMenuItem("Edit Package")).addActionListener(editPackageListener);
        menu.add(editPackage);     
        
        menu.addSeparator();

        (deleteClass = new JMenuItem("Delete Enum")).addActionListener(removeWidgetListener);
        menu.add(deleteClass);

    }
    
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
    ActionListener editPackageListener = new ActionListener() {
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
            enumWidget.getComponent().getParentDiagram().removeComponent(enumWidget.getComponent().getName());
            enumWidget.removeFromParent();
        }
    };

    @Override
    public JPopupMenu getPopupMenu(Widget widget, Point point) {
        return menu;
    }
}
