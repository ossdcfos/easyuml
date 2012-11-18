/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.builder;

import java.awt.Color;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.UmlClassElement;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ClassWidget;
import org.uml.visual.widgets.actions.AddFieldAction;
import org.uml.visual.widgets.actions.AddMethodAction;
import org.uml.visual.widgets.actions.DeleteFieldAction;
import org.uml.visual.widgets.actions.DeleteMethodAction;
import org.uml.visual.widgets.actions.LabelTextFieldEditorAction;
import org.uml.visual.widgets.actions.PickAttributeModifierAction;
import org.uml.visual.widgets.actions.PickFinalKeywordForAttributeAction;
import org.uml.visual.widgets.actions.PickFinalKeywordForMethodAction;
import org.uml.visual.widgets.actions.PickMethodModifierAction;
import org.uml.visual.widgets.actions.PickStaticKeywordForAttributeAction;
import org.uml.visual.widgets.actions.PickStaticKeywordForMethodAction;

/**
 *
 * Combination of Adapter Pattern and Builder Pattern. Builder Pattern provides
 * us default build methods, and Adapter "Hides" implementation of basic
 * functionality leaving us to implement 2-3 methods! Similar to TableModel and
 * DefaultTableModel(Adapter)
 */
public abstract class ClassWidgetBuilderAdapter implements ClassWidgetBuilder {
    
    protected ClassWidget classWidget;
    protected UmlClassElement element;
    protected ClassDiagramScene scene;
    protected WidgetAction addFieldAction = ActionFactory.createSelectAction(new AddFieldAction(classWidget));
    protected WidgetAction addMethodAction = ActionFactory.createSelectAction(new AddMethodAction(classWidget));
    protected WidgetAction deleteFieldAction = ActionFactory.createSelectAction(new DeleteFieldAction(classWidget));
    protected WidgetAction deleteMethodAction = ActionFactory.createSelectAction(new DeleteMethodAction(classWidget));
    protected WidgetAction editorAction = ActionFactory.createInplaceEditorAction(new LabelTextFieldEditorAction(classWidget));
    protected WidgetAction pickMethodModifier = ActionFactory.createSelectAction(new PickMethodModifierAction(classWidget));
    protected WidgetAction pickAtributeModifier = ActionFactory.createSelectAction(new PickAttributeModifierAction(classWidget));
    protected WidgetAction pickStaticKeywordForAtribute = ActionFactory.createSelectAction(new PickStaticKeywordForAttributeAction());
    protected WidgetAction pickFinalKeywordForAtribute = ActionFactory.createSelectAction(new PickFinalKeywordForAttributeAction());
    protected WidgetAction pickStaticKeywordForMethod = ActionFactory.createSelectAction(new PickStaticKeywordForMethodAction());
    protected WidgetAction pickFinalKeywordForMethod = ActionFactory.createSelectAction(new PickFinalKeywordForMethodAction());
    
    public ClassWidgetBuilderAdapter(ClassDiagramScene scene, UmlClassElement element) {
        this.scene = scene;
        this.element = element;
        classWidget = new ClassWidget(scene, element);
    }
    
    @Override
    public abstract void buildActions();
    
    @Override
    public abstract void buildFields();
    
    @Override
    public abstract void buildLabels();
    
    @Override
    public void buildBorder() {
        classWidget.setBorder(BorderFactory.createEmptyBorder(6));
    }
    
    @Override
    public void buildMethods() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public Widget createAddFieldActionWidget() {
        Widget widget = new Widget(scene);
        widget.setLayout(LayoutFactory.createAbsoluteLayout());
        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel("+ add field");
        labelWidget.setForeground(Color.GRAY);
        labelWidget.setAlignment(LabelWidget.Alignment.CENTER);
        labelWidget.getActions().addAction(ActionFactory.createSelectAction(new AddFieldAction(classWidget)));
        widget.addChild(labelWidget);
        return widget;
    }
    
    private Widget createMethodModifierPicker(Scene scene) {
        Widget w = new Widget(scene);
        w.setLayout(LayoutFactory.createVerticalFlowLayout());
        ImageWidget DefaultImage = new ImageWidget(scene);
        DefaultImage.setImage(MethodDefaultImage);
        DefaultImage.getActions().addAction(pickMethodModifier);
        w.addChild(DefaultImage);
        return w;
    } 
    
    
}
