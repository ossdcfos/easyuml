package org.uml.visual.widgets.actions;

import org.netbeans.api.visual.action.TextFieldInplaceEditor;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.uml.model.ClassDiagram;
import org.uml.model.relations.RelationBase;

/**
 *
 * @author Uros
 */
public class RelationLabelTextFieldEditorAction implements TextFieldInplaceEditor{

    RelationBase relationComponent;
    ClassDiagram classDiagram;
    
    public RelationLabelTextFieldEditorAction(RelationBase relationComponent, ClassDiagram classDiagram) {
        this.relationComponent = relationComponent;
        this.classDiagram = classDiagram;
    }

    
    
    @Override
    public boolean isEnabled(Widget widget) {
        return true;
    }

    @Override
    public String getText(Widget widget) {
        return ((LabelWidget) widget).getLabel();
    }

    @Override
    public void setText(Widget widget, String string) {
        ((LabelWidget) widget).setLabel(string);
        classDiagram.removeRelation(relationComponent);
        relationComponent.setName(string);
        classDiagram.addRelation(relationComponent);
    }
    
}
