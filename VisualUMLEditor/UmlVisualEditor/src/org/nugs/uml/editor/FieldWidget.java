package org.nugs.uml.editor;

import org.netbeans.api.visual.widget.Scene;
import org.netbeans.api.visual.widget.Widget;

/**
 *
 * @author zoran
 */
public class FieldWidget extends Widget {

    public FieldWidget(String fieldName, Scene scene) {
        super(scene);
       /* 
        Widget widget = new Widget(scene);
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());

        LabelWidget labelMinus = new LabelWidget(scene);
        labelMinus.setFont(scene.getDefaultFont().deriveFont(Font.BOLD));
        labelMinus.setLabel("-");
        labelMinus.getActions().addAction(deleteFieldAction);
        this.addChild(labelMinus);

        this.addChild(createAtributeModifierPicker(scene));

        LabelWidget staticKeyword = new LabelWidget(scene);
        staticKeyword.setLabel(" _");
        staticKeyword.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
        this.addChild(staticKeyword);
        staticKeyword.getActions().addAction(pickStaticKeywordForAtribute);

        LabelWidget finalKeyword = new LabelWidget(scene);
        finalKeyword.setLabel(" _ ");
        finalKeyword.setFont(scene.getDefaultFont().deriveFont(Font.ITALIC));
        this.addChild(finalKeyword);
        finalKeyword.getActions().addAction(pickFinalKeywordForAtribute);

        LabelWidget labelWidget = new LabelWidget(scene);
        labelWidget.setLabel(fieldName);
        this.addChild(labelWidget);
        labelWidget.getActions().addAction(editorAction);
        */
    }
    
}
