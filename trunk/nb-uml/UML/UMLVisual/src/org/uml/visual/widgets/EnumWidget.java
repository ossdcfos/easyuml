/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.Font;
import java.awt.event.KeyEvent;
import javax.swing.ActionMap;
import javax.swing.InputMap;
import javax.swing.KeyStroke;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.uml.model.ClassDiagramComponent;
import org.uml.model.EnumComponent;
import org.uml.visual.providers.ClassConnectProvider;
import org.uml.visual.widgets.actions.DeleteClassAction;
import org.uml.visual.widgets.actions.EditFieldNameAction;

/**
 *
 * @author "NUGS"
 */
public class EnumWidget extends UMLWidget {

    ClassDiagramScene scene;
    EnumComponent enumComponent;
    private LabelWidget enumNameWidget;
    private Widget fieldsWidget;
    private Widget methodsWidget;
    private WidgetAction selectFieldNameAction = ActionFactory.createSelectAction(new EditFieldNameAction());
    private static final Border BORDER_4 = BorderFactory.createEmptyBorder(6);

    public EnumWidget(ClassDiagramScene scene, EnumComponent enumComponent) {
        super(scene);
        this.scene = scene;
        this.enumComponent = enumComponent;

        setChildConstraint(getImageWidget(), 1);
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setBorder(org.netbeans.api.visual.border.BorderFactory.createLineBorder());
        setOpaque(true);
        setCheckClipping(true);

        Widget enumWidget = new Widget(scene);
        enumWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        enumWidget.setBorder(BORDER_4);

        LabelWidget stereotip = new LabelWidget(scene, "<<enumeration>>");
        stereotip.setAlignment(LabelWidget.Alignment.CENTER);
        enumWidget.addChild(stereotip);

        enumNameWidget = new LabelWidget(scene);
        enumNameWidget.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        enumNameWidget.setAlignment(LabelWidget.Alignment.CENTER);
        enumWidget.addChild(enumNameWidget);
        addChild(enumWidget);
        enumNameWidget.getActions().addAction(selectFieldNameAction);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        fieldsWidget = new Widget(scene);
        fieldsWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        fieldsWidget.setOpaque(false);
        fieldsWidget.setBorder(BORDER_4);
        LabelWidget memberName = new LabelWidget(scene);
        fieldsWidget.addChild(memberName);
        addChild(fieldsWidget);

        addChild(new SeparatorWidget(scene, SeparatorWidget.Orientation.HORIZONTAL));

        methodsWidget = new Widget(scene);
        methodsWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        methodsWidget.setOpaque(false);
        methodsWidget.setBorder(BORDER_4);
        LabelWidget operationName = new LabelWidget(scene);
        methodsWidget.addChild(operationName);
        addChild(methodsWidget);
             

        this.enumNameWidget.setLabel(enumComponent.getName());
    }

    public ClassDiagramScene getClassDiagramScene() {
        return scene;
    }
        @Override
        public EnumComponent getComponent () {
            return enumComponent;
        }

    @Override
    public String toString() {
        return "ime enuma, dodati label na enum";
    }
        
   
}
