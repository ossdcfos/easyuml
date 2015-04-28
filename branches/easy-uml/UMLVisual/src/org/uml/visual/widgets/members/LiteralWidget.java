package org.uml.visual.widgets.members;

import java.beans.PropertyChangeEvent;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.uml.model.members.Literal;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ISignedUMLWidget;
import org.uml.visual.widgets.actions.MemberNameEditor;

/**
 *
 * @author Jelena
 */
public class LiteralWidget extends MemberWidgetBase implements ISignedUMLWidget {

    public LiteralWidget(ClassDiagramScene scene, Literal literal) {
        super(scene, literal);

        updateIcon();
        this.addChild(iconWidget);
        
        nameLabel.setLabel(literal.getName());
        nameLabel.getActions().addAction(ActionFactory.createInplaceEditorAction(new MemberNameEditor(this)));
        this.addChild(nameLabel);
        setChildConstraint(nameLabel, 1);   // any number, as it defines weight by which it will occupy the parent space
    }

    @Override
    public void setSignature(String signature) {
        String oldSignature = member.getSignature();
        if (!signature.equals(oldSignature)) {
            if (member.getDeclaringComponent().signatureExists(signature)) {
                JOptionPane.showMessageDialog(null, "Member \"" + signature + "\" already exists!");
            } else {
                member.setName(signature);
            }
        }
        nameLabel.setLabel(member.getLabelText(getClassDiagramScene().isShowSimpleTypes()));
    }

    @Override
    public String getSignature() {
        return member.getSignature();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        if ("name".equals(propName)) {
            nameLabel.setLabel(member.getLabelText(getClassDiagramScene().isShowSimpleTypes()));
        }
        notifyTopComponentModified();
        getScene().validate();
    }
}
