package org.uml.visual.widgets.members;

import java.beans.PropertyChangeEvent;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.openide.util.WeakListeners;
import org.uml.model.members.Constructor;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.popups.MemberBasePopupProvider;

/**
 *
 * @author Jelena
 */
public class ConstructorWidget extends MemberWidgetBase {

    public ConstructorWidget(ClassDiagramScene scene, Constructor constructor) {
        super(scene, constructor);
        this.component.getDeclaringComponent().addPropertyChangeListener(WeakListeners.propertyChange(this, this.component.getDeclaringComponent()));
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
        
        updateVisibilityLabel();
        this.addChild(visibilityLabel);

        nameLabel.setLabel(((Constructor)component).getLabelText());
        this.addChild(nameLabel);

        getActions().addAction(ActionFactory.createPopupMenuAction(new MemberBasePopupProvider(this)));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // from parent
        if ("name".equals(evt.getPropertyName())) {
            String newName = (String) evt.getNewValue();
            nameLabel.setLabel(newName+"()");
        }
        else if ("visibility".equals(evt.getPropertyName())) {
            updateVisibilityLabel();
        }
        notifyTopComponentModified();
        getScene().validate();
    }    
}
