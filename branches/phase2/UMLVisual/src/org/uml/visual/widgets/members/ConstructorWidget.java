package org.uml.visual.widgets.members;

import java.beans.PropertyChangeEvent;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.openide.util.WeakListeners;
import org.uml.model.members.Constructor;
import org.uml.visual.parser.MemberParser;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ISignedUMLWidget;
import org.uml.visual.widgets.actions.MemberNameEditor;

/**
 *
 * @author Jelena
 */
public class ConstructorWidget extends MemberWidgetBase implements ISignedUMLWidget {

    public ConstructorWidget(ClassDiagramScene scene, Constructor constructor) {
        super(scene, constructor);
        this.member.getDeclaringComponent().addPropertyChangeListener(WeakListeners.propertyChange(this, this.member.getDeclaringComponent()));
        
        updateVisibilityLabel();
        this.addChild(visibilityLabel);

        nameLabel.setLabel(((Constructor)member).getLabelText());
        nameLabel.getActions().addAction(ActionFactory.createInplaceEditorAction(new MemberNameEditor(this)));
        this.addChild(nameLabel);
        setChildConstraint(nameLabel, 1);   // any number, as it defines weight by which it will occupy the parent space
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        // from parent
        String propName = evt.getPropertyName();
        if ("name".equals(propName) || "arguments".equals(propName)) {
            nameLabel.setLabel(((Constructor) member).getLabelText());
        }
        else if ("visibility".equals(propName)) {
            updateVisibilityLabel();
        }
        notifyTopComponentModified();
        getScene().validate();
    }    

    @Override
    public void setSignature(String signature) {
        String oldSignature = member.getSignature();
        if (!signature.equals(oldSignature)) {
            if (member.getDeclaringComponent().signatureExists(signature)) {
                JOptionPane.showMessageDialog(null, "Member \"" + signature + "\" already exists!");
            } else {
                MemberParser.fillConstructorComponents((Constructor) member, signature);
            }
        }
    }

    @Override
    public String getSignature() {
        return member.getSignature();
    }
}
