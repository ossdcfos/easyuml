package org.uml.visual.widgets.members;

import org.uml.visual.parser.MemberParser;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.uml.model.members.Method;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ISignedUMLWidget;
import org.uml.visual.widgets.actions.MemberNameEditor;

/**
 *
 * @author Jelena
 */
public class MethodWidget extends MemberWidgetBase implements ISignedUMLWidget {

    public MethodWidget(ClassDiagramScene scene, Method method) {
        super(scene, method);

        updateVisibilityLabel();
        this.addChild(visibilityLabel);

        nameLabel.setLabel(method.getLabelText());
        nameLabel.getActions().addAction(ActionFactory.createInplaceEditorAction(new MemberNameEditor(this)));
        this.addChild(nameLabel);
        setChildConstraint(nameLabel, 1);   // any number, as it defines weight by which it will occupy the parent space
        nameLabel.setFont(scene.getDefaultFont());
        setStatic(method.isStatic());
    }

    @Override
    public void setSignature(String signature) {
        String oldSignature = member.getSignature();
        if (!signature.equals(oldSignature)) {
            if (member.getDeclaringComponent().signatureExists(signature)) {
                JOptionPane.showMessageDialog(null, "Member \"" + signature + "\" already exists!");
            } else {
                MemberParser.fillMethodComponents((Method) member, signature);
            }
        }
    }

    @Override
    public String getSignature() {
        return member.getSignature();
    }

    private void setStatic(boolean isStatic) {
        Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
        if (isStatic) {
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        } else {
            fontAttributes.put(TextAttribute.UNDERLINE, -1);
        }
        nameLabel.setFont(nameLabel.getFont().deriveFont(fontAttributes));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        if ("isStatic".equals(propName)) {
            setStatic((boolean) evt.getNewValue());
        }
        else if ("isFinal".equals(propName) || "isAbstract".equals(propName) || "isSynchronized".equals(propName) || "name".equals(propName) || "type".equals(propName) || "arguments".equals(propName)) {
            nameLabel.setLabel(((Method) member).getLabelText());
        }
        else if ("visibility".equals(evt.getPropertyName())) {
            updateVisibilityLabel();
        }
        notifyTopComponentModified();
        getScene().validate();
    }
}
