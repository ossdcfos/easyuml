/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.uml.visual.widgets.members;

import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.uml.model.members.Field;
import org.uml.model.members.Member;
import static org.uml.model.members.Visibility.PACKAGE;
import static org.uml.model.members.Visibility.PRIVATE;
import static org.uml.model.members.Visibility.PROTECTED;
import static org.uml.model.members.Visibility.PUBLIC;
import org.uml.visual.parser.WidgetParser;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.popups.FieldPopupMenuProvider;

/**
 *
 * @author Jelena
 */
public class FieldWidget extends MemberWidgetBase {

    Field fieldComponent;
    private WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    LabelWidget visibilityLabel;
    LabelWidget fieldNameWidget;
    WidgetParser wp;

    public FieldWidget(ClassDiagramScene scene, Field field) {
        super(scene);
        this.fieldComponent = field;
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
        visibilityLabel = new LabelWidget(getScene());
        visibilityLabel.setLabel("-");
        this.addChild(visibilityLabel);

        fieldNameWidget = new LabelWidget(getScene());
        fieldNameWidget.setLabel(field.getSignatureForLabel());
        this.addChild(fieldNameWidget);
        fieldNameWidget.getActions().addAction(nameEditorAction);
        fieldNameWidget.getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(this)));
        wp = new WidgetParser();
        refreshLabel();
    }

    @Override
    public LabelWidget getNameLabel() {
        return fieldNameWidget;
    }

    @Override
    public void setName(String newName) {
        if (getName().equals(newName)) {
            return;
        }
        String oldName = fieldComponent.getName();

        if (!fieldComponent.getDeclaringClass().nameExists(newName)) {
            fieldNameWidget.setLabel(newName);
            fieldComponent.setName(newName);
            fieldComponent.getDeclaringClass().notifyMemberNameChanged(fieldComponent, oldName);
        } else {
            //poruka
        }
    }

    @Override
    public String getName() {
        return fieldComponent.getName();
    }

    @Override
    public Member getMember() {
        return fieldComponent;
    }

    public LabelWidget getFieldNameWidget() {
        return fieldNameWidget;
    }

    public Field getFieldComponent() {
        return fieldComponent;
    }

    @Override
    public void setAttributes(String attributes) {
        String oldName = fieldComponent.getName();
        wp.fillFieldComponents(fieldComponent, attributes);
        String newName = fieldComponent.getName();
        if (newName.equals(oldName)) {
            
        } else {
            if (!fieldComponent.getDeclaringClass().nameExists(newName)) {
                fieldComponent.getDeclaringClass().notifyMemberNameChanged(fieldComponent, oldName);
            } else {
                fieldComponent.setName(oldName);
                throw new RuntimeException("Error: name already exists.");
            }
        }


        fieldNameWidget.setLabel(fieldComponent.getSignatureForLabel());

        refreshLabel();

    }

    public void refreshLabel() {
        if (fieldComponent != null && fieldComponent.getVisibility() != null) {
            switch (fieldComponent.getVisibility()) {
                case PUBLIC:
                    visibilityLabel.setLabel("+");
                    break;
                case PRIVATE:
                    visibilityLabel.setLabel("-");
                    break;
                case PROTECTED:
                    visibilityLabel.setLabel("#");
                    break;
                case PACKAGE:
                    visibilityLabel.setLabel("~");
                    break;
            }

        }
    }
}
