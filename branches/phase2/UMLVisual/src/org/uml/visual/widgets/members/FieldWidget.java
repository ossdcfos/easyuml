package org.uml.visual.widgets.members;

import org.uml.visual.parser.MemberParser;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.openide.util.WeakListeners;
import org.uml.model.members.Field;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ISignedUMLWidget;
import org.uml.visual.widgets.actions.MemberNameEditor;
import org.uml.visual.widgets.popups.MemberBasePopupProvider;

/**
 *
 * @author Jelena
 */
public class FieldWidget extends MemberWidgetBase implements ISignedUMLWidget {

    public FieldWidget(ClassDiagramScene scene, Field field) {
        super(scene, field);
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());

        updateVisibilityLabel();
        this.addChild(visibilityLabel);

        nameLabel.setLabel(field.getLabelText());
        // this has to be invoked because of setStatic changes to font. Otherwise there is a NPE
        nameLabel.setFont(scene.getDefaultFont());
        setStatic(field.isStatic());
        nameLabel.getActions().addAction(ActionFactory.createInplaceEditorAction(new MemberNameEditor(this)));
        this.addChild(nameLabel);

        getActions().addAction(ActionFactory.createPopupMenuAction(new MemberBasePopupProvider(this)));
    }

    @Override
    public String getSignature() {
        return component.getSignature();
    }

    @Override
    public void setSignature(String signature) {
        String oldSignature = component.getSignature();
        if (!signature.equals(oldSignature)) {
            if (component.getDeclaringComponent().signatureExists(signature)) {
                JOptionPane.showMessageDialog(null, "Member \"" + signature + "\" already exists!");
            } else {
                MemberParser.fillFieldComponents((Field) component, signature);
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
            nameLabel.setLabel(((Field) component).getLabelText());
        } else if ("visibility".equals(evt.getPropertyName())) {
            updateVisibilityLabel();
        }
        notifyTopComponentModified();
        getScene().validate();
    }
}
