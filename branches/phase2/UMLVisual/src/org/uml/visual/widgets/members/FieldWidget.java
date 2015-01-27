package org.uml.visual.widgets.members;

import org.uml.visual.parser.MemberParser;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.uml.model.members.Field;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ISignedUMLWidget;
import org.uml.visual.widgets.actions.MemberNameEditor;

/**
 *
 * @author Jelena
 */
public class FieldWidget extends MemberWidgetBase implements ISignedUMLWidget {

    public FieldWidget(ClassDiagramScene scene, Field field) {
        super(scene, field);

        updateVisibilityLabel();
        this.addChild(visibilityLabel);

        nameLabel.setLabel(field.getLabelText());
        nameLabel.getActions().addAction(ActionFactory.createInplaceEditorAction(new MemberNameEditor(this)));
        this.addChild(nameLabel);
        setChildConstraint(nameLabel, 1);   // any number, as it defines weight by which it will occupy the parent space
        nameLabel.setFont(scene.getDefaultFont()); // this has to be invoked because of setStatic changes to font. Otherwise there is a NPE
        setStatic(field.isStatic());
    }

    @Override
    public String getSignature() {
        return member.getSignature();
    }

    @Override
    public void setSignature(String signature) {
        String oldSignature = member.getSignature();
        if (!signature.equals(oldSignature)) {
            if (member.getDeclaringComponent().signatureExists(signature)) {
                JOptionPane.showMessageDialog(null, "Member \"" + signature + "\" already exists!");
            } else {
                MemberParser.fillFieldComponents((Field) member, signature);
            }
        }
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
        } else if ("isFinal".equals(propName) || "isTransient".equals(propName) || "isVolatile".equals(propName) || "name".equals(propName) || "type".equals(propName)) {
            nameLabel.setLabel(((Field) member).getLabelText());
        } else if ("visibility".equals(evt.getPropertyName())) {
            updateVisibilityLabel();
        }
        notifyTopComponentModified();
        getScene().validate();
    }
}
