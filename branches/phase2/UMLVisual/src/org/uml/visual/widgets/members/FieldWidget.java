package org.uml.visual.widgets.members;

import org.uml.memberparser.MemberParser;
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

        this.addChild(iconWidget);

        this.addChild(visibilityLabel);

        nameLabel.setLabel(field.getLabelText(getClassDiagramScene().isShowSimpleTypes()));
        nameLabel.getActions().addAction(ActionFactory.createInplaceEditorAction(new MemberNameEditor(this)));
        nameLabel.setFont(scene.getFont()); // this has to be invoked because of setStatic changes to font. Otherwise there is a NPE
        setStatic(field.isStatic());
        this.addChild(nameLabel);
        setChildConstraint(nameLabel, 1);   // any number, as it defines weight by which it will occupy the parent space
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
                try {
                    MemberParser.fillFieldComponents((Field) member, signature);
                } catch (IllegalArgumentException e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Illegal format error", JOptionPane.ERROR_MESSAGE);
                }
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
            updateIcon();
            setStatic((boolean) evt.getNewValue());
        } else if ("isFinal".equals(propName) || "isTransient".equals(propName) || "isVolatile".equals(propName) || "name".equals(propName) || "type".equals(propName)) {
            nameLabel.setLabel(((Field) member).getLabelText(getClassDiagramScene().isShowSimpleTypes()));
        } else if ("visibility".equals(evt.getPropertyName())) {
            updateIcon();
            updateVisibilityLabel();
        }
        notifyTopComponentModified();
        getScene().validate();
    }
}
