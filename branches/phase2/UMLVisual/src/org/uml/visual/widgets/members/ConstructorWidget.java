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
public class ConstructorWidget extends MemberWidgetBase implements PropertyChangeListener{

    Constructor constructorComponent;
    LabelWidget visibilityLabel;
    LabelWidget nameLabel;
    
    public ConstructorWidget(ClassDiagramScene scene, Constructor constructor) {
        super(scene, constructor);
        this.constructorComponent = constructor;
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
        visibilityLabel = new LabelWidget(getScene());
        visibilityLabel.setLabel("+");
        this.addChild(visibilityLabel);

        nameLabel = new LabelWidget(getScene());
        nameLabel.setLabel(constructorComponent.getSignatureForLabel());
        this.addChild(nameLabel);
        //construktorNameWidget.getActions().addAction(nameEditorAction);
        
        getActions().addAction(ActionFactory.createPopupMenuAction(new MemberBasePopupProvider(this)));
    }
    
    @Override
    public LabelWidget getNameLabel() {
        return nameLabel;
    }

    @Override
    public void setName(String newName) {
        nameLabel.setLabel(newName);
    }

    @Override
    public String getName() {
        return constructorComponent.getName();
    }

    @Override
    public MemberBase getMember() {
        return constructorComponent;
    }

    @Override
    public void setAttributes(String attributes) {
        
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String newName = evt.getNewValue().toString();
        constructorComponent.setName(newName);
        setName(newName + "()");
    }

    @Override
    protected void setSelected(boolean isSelected) {
        if(isSelected){
            visibilityLabel.setForeground(SELECT_FONT_COLOR);
            nameLabel.setForeground(SELECT_FONT_COLOR);
        } else {
            visibilityLabel.setForeground(DEFAULT_FONT_COLOR);
            nameLabel.setForeground(DEFAULT_FONT_COLOR);
        }
    }
}
