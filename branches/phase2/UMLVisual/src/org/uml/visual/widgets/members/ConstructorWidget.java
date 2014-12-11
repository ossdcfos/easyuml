package org.uml.visual.widgets.members;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.uml.model.members.Constructor;
import org.uml.model.members.MemberBase;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.providers.popups.MemberBasePopupProvider;

/**
 *
 * @author Jelena
 */
public class ConstructorWidget extends MemberWidgetBase {

    LabelWidget visibilityLabel;

    public ConstructorWidget(ClassDiagramScene scene, Constructor constructor) {
        super(scene, constructor);
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
        visibilityLabel = new LabelWidget(getScene());
        visibilityLabel.setLabel("+");
        this.addChild(visibilityLabel);

        nameWidget = new LabelWidget(getScene());
        nameWidget.setLabel(((Constructor)component).getSignatureForLabel());
        this.addChild(nameWidget);
        //construktorNameWidget.getActions().addAction(nameEditorAction);

        getActions().addAction(ActionFactory.createPopupMenuAction(new MemberBasePopupProvider(this)));
    }

    @Override
    public LabelWidget getNameLabel() {
        return nameWidget;
    }

//    @Override
//    public void setName(String newName) {
//        nameLabel.setLabel(newName);
//    }
//
//    @Override
//    public String getName() {
//        return constructorComponent.getName();
//    }

    @Override
    public MemberBase getMember() {
        return component;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ("name".equals(evt.getPropertyName())) {
            String newName = evt.getNewValue().toString();
            component.setName(newName);
            nameWidget.setLabel(newName+"()");
        }
        if ("visibility".equals(evt.getPropertyName())) {
            refreshVisibilityLabel();
        }
        changedNotify();
        getScene().validate();
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
    public void setSignature(String signature) {
        // TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getSignature() {
        // TODO Implement this.
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
