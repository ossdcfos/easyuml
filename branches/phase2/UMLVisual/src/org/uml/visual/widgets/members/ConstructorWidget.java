package org.uml.visual.widgets.members;

import java.beans.PropertyChangeEvent;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.openide.util.WeakListeners;
import org.uml.model.members.Constructor;
import org.uml.visual.parser.MemberParser;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ISignedUMLWidget;
import org.uml.visual.widgets.actions.MemberNameEditor;
import org.uml.visual.widgets.popups.MemberBasePopupProvider;

/**
 *
 * @author Jelena
 */
public class ConstructorWidget extends MemberWidgetBase implements ISignedUMLWidget {

    public ConstructorWidget(ClassDiagramScene scene, Constructor constructor) {
        super(scene, constructor);
        this.component.getDeclaringComponent().addPropertyChangeListener(WeakListeners.propertyChange(this, this.component.getDeclaringComponent()));
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());
        
        updateVisibilityLabel();
        this.addChild(visibilityLabel);

        nameLabel.setLabel(((Constructor)component).getLabelText());
        this.addChild(nameLabel);
        nameLabel.getActions().addAction(ActionFactory.createInplaceEditorAction(new MemberNameEditor(this)));

        getActions().addAction(ActionFactory.createPopupMenuAction(new MemberBasePopupProvider(this)));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // from parent
        String propName = evt.getPropertyName();
        if ("name".equals(propName) || "arguments".equals(propName)) {
            nameLabel.setLabel(((Constructor) component).getLabelText());
        }
        else if ("visibility".equals(propName)) {
            updateVisibilityLabel();
        }
        notifyTopComponentModified();
        getScene().validate();
    }    

    @Override
    public void setSignature(String signature) {
        String oldSignature = component.getSignature();
        if (!signature.equals(oldSignature)) {
            if (component.getDeclaringComponent().signatureExists(signature)) {
                JOptionPane.showMessageDialog(null, "Member \"" + signature + "\" already exists!");
            } else {
                MemberParser.fillConstructorComponents((Constructor) component, signature);
            }
        }
    }

    @Override
    public String getSignature() {
        return component.getSignature();
    }
}
