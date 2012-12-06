/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.SeparatorWidget;
import org.netbeans.api.visual.widget.Widget;
import org.netbeans.api.visual.widget.general.IconNodeWidget;
import org.openide.util.Utilities;
import org.uml.model.ClassComponent;
import org.uml.model.InterfaceComponent;
import org.uml.visual.widgets.actions.AddFieldAction;
import org.uml.visual.widgets.actions.AddMethodAction;
import org.uml.visual.widgets.actions.EditFieldNameAction;
import org.uml.visual.widgets.actions.PickAttributeModifierAction;
import org.uml.visual.widgets.actions.PickMethodModifierAction;

/**
 *
 * @author hrza
 */
public class InterfaceWidget extends IconNodeWidget {
    
    InterfaceComponent interfaceComponent;
    ClassDiagramScene scene;
    private static final Image MethodDefaultImage = Utilities.loadImage("org/uml/visual/icons/MethodDefault.jpg"); // NOI18N
    private static final Image AtributeDefaultImage = Utilities.loadImage("org/uml/visual/icons/AtributeDefault.jpg"); // NOI18N
    
    
    private static final Border RESIZE_BORDER = 
        org.netbeans.api.visual.border.BorderFactory.createResizeBorder(4, Color.black, true);
    private static final Border DEFAULT_BORDER = 
        org.netbeans.api.visual.border.BorderFactory.createLineBorder();
    
    private LabelWidget interfaceNameWidget;
    private Widget fieldsWidget;
    private Widget methodsWidget;
    
//    private WidgetAction addFieldAction = ActionFactory.createSelectAction(new AddFieldAction(this));
//    private WidgetAction addMethodAction = ActionFactory.createSelectAction(new AddMethodAction(this));
    private WidgetAction selectFieldNameAction = ActionFactory.createSelectAction(new EditFieldNameAction());
    
//    private WidgetAction pickMethodModifier = ActionFactory.createSelectAction(new PickMethodModifierAction(this));
//    private WidgetAction pickAtributeModifier = ActionFactory.createSelectAction(new PickAttributeModifierAction(this));
    
    private static final Border BORDER_4 = BorderFactory.createEmptyBorder(6);
    
    public InterfaceWidget(ClassDiagramScene scene, InterfaceComponent interfaceComponent) {
        super(scene);
        this.interfaceComponent = interfaceComponent;
        this.scene=scene;
        setChildConstraint(getImageWidget(), 1);
        setLayout(LayoutFactory.createVerticalFlowLayout());
        setBorder(org.netbeans.api.visual.border.BorderFactory.createLineBorder());
        setOpaque(true);
        setCheckClipping(true);
        
        Widget interfaceWidget = new Widget(scene);
        interfaceWidget.setLayout(LayoutFactory.createVerticalFlowLayout());
        interfaceWidget.setBorder(BORDER_4);
        
        LabelWidget stereotip = new LabelWidget(scene, "<<interface>>");
        stereotip.setAlignment(LabelWidget.Alignment.CENTER);
        interfaceWidget.addChild(stereotip);
        
        interfaceNameWidget = new LabelWidget(scene);
        interfaceNameWidget.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        interfaceNameWidget.setAlignment(LabelWidget.Alignment.CENTER);
        interfaceWidget.addChild(interfaceNameWidget);
        addChild(interfaceWidget);
        interfaceNameWidget.getActions().addAction(selectFieldNameAction);
        //classNameWidget.getActions().addAction(ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction(this)));
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
        
        
        this.interfaceNameWidget.setLabel(interfaceComponent.getName());
    }
    
}
