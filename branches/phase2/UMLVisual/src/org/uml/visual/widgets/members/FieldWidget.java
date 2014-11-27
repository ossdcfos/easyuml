package org.uml.visual.widgets.members;

import java.awt.Color;
import java.awt.Font;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.action.WidgetAction;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.openide.util.WeakListeners;
import org.uml.model.members.Field;
import org.uml.model.members.MemberBase;
import static org.uml.model.Visibility.PACKAGE;
import static org.uml.model.Visibility.PRIVATE;
import static org.uml.model.Visibility.PROTECTED;
import static org.uml.model.Visibility.PUBLIC;
import org.uml.visual.parser.WidgetParser;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.actions.NameEditorAction;
import org.uml.visual.widgets.providers.popups.FieldPopupMenuProvider;

/**
 *
 * @author Jelena
 */
public class FieldWidget extends MemberWidgetBase implements PropertyChangeListener {

    private WidgetAction nameEditorAction = ActionFactory.createInplaceEditorAction(new NameEditorAction(this));
    LabelWidget visibilityLabel;
    LabelWidget nameWidget;
    WidgetParser wp;

    public FieldWidget(ClassDiagramScene scene, Field field) {
        super(scene, field);
        this.component = field;
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
        //scene.addObject(field, this);
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());

        visibilityLabel = new LabelWidget(getScene());
        visibilityLabel.setLabel("-");
        visibilityLabel.setBackground(Color.BLUE);
        this.addChild(visibilityLabel);

//        getActions().addAction(scene.createSelectAction());
//        getActions().addAction(scene.createObjectHoverAction());
        nameWidget = new LabelWidget(getScene());
        nameWidget.setLabel(field.getSignatureForLabel());
        nameWidget.setBackground(Color.MAGENTA);
        this.addChild(nameWidget);
        nameWidget.getActions().addAction(nameEditorAction);

        getActions().addAction(ActionFactory.createPopupMenuAction(new FieldPopupMenuProvider(this)));
        wp = new WidgetParser();
        refreshVisibilityLabel();
    }

    @Override
    public LabelWidget getNameLabel() {
        return nameWidget;
    }

    @Override
    public void setName(String newName) {
        if (getName().equals(newName)) {
            return;
        }
        String oldName = component.getName();

        if (!component.getDeclaringClass().nameExists(newName)) {
            nameWidget.setLabel(newName);
            component.setName(newName);
            component.getDeclaringClass().notifyMemberNameChanged(component, oldName);
        } else {
            //poruka
        }
    }

    @Override
    public String getName() {
        return component.getName();
    }

    @Override
    public MemberBase getMember() {
        return component;
    }

    public LabelWidget getFieldNameWidget() {
        return nameWidget;
    }

    @Override
    public void setAttributes(String attributes) {
        String oldName = component.getName();
        wp.fillFieldComponents((Field)component, attributes);
        String newName = component.getName();
        if (newName.equals(oldName)) {

        } else {
            if (!component.getDeclaringClass().nameExists(newName)) {
                component.getDeclaringClass().notifyMemberNameChanged(component, oldName);
            } else {
                component.setName(oldName);
                throw new RuntimeException("Error: name already exists.");
            }
        }

        nameWidget.setLabel(((Field)component).getSignatureForLabel());

        refreshVisibilityLabel();
    }

    public final void refreshVisibilityLabel() {
        if (component != null && component.getVisibility() != null) {
            switch (component.getVisibility()) {
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

    @Override
    protected void setSelected(boolean isSelected) {
        if (isSelected) {
            visibilityLabel.setForeground(SELECT_FONT_COLOR);
            nameWidget.setForeground(SELECT_FONT_COLOR);
        } else {
            visibilityLabel.setForeground(DEFAULT_FONT_COLOR);
            nameWidget.setForeground(DEFAULT_FONT_COLOR);
        }
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("isStatic".equals(evt.getPropertyName()) || "isFinal".equals(evt.getPropertyName()) || "isSynchronized".equals(evt.getPropertyName()) || "name".equals(evt.getPropertyName())) {
            nameWidget.setLabel(((Field)component).getSignatureForLabel());
        }
        if ("visibility".equals(evt.getPropertyName())) {
            refreshVisibilityLabel();
        }
        getScene().validate();
    }

}
