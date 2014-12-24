package org.uml.visual.widgets.members;

import java.beans.PropertyChangeEvent;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.openide.util.WeakListeners;
import org.uml.model.members.Literal;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ISignedUMLWidget;
import org.uml.visual.widgets.actions.MemberNameEditor;
import org.uml.visual.widgets.popups.MemberBasePopupProvider;

/**
 *
 * @author Jelena
 */
public class LiteralWidget extends MemberWidgetBase implements ISignedUMLWidget {

    public LiteralWidget(ClassDiagramScene scene, Literal literal) {
        super(scene, literal);
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());

        nameLabel.setLabel(literal.getName());
        this.addChild(nameLabel);
        nameLabel.getActions().addAction(ActionFactory.createInplaceEditorAction(new MemberNameEditor(this)));

        getActions().addAction(ActionFactory.createPopupMenuAction(new MemberBasePopupProvider(this)));
    }

    @Override
    public void setSignature(String signature) {
        String oldSignature = component.getSignature();
        if (!signature.equals(oldSignature)) {
            if (component.getDeclaringComponent().signatureExists(signature)) {
                JOptionPane.showMessageDialog(null, "Member \"" + signature + "\" already exists!");
            } else {
                component.setName(signature);
            }
        }
        nameLabel.setLabel(((Literal) component).getLabelText());
    }

    @Override
    public String getSignature() {
        return component.getSignature();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        if ("name".equals(propName)) {
            nameLabel.setLabel(((Literal) component).getLabelText());
        }
        notifyTopComponentModified();
        getScene().validate();
    }
}
