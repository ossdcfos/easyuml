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
import org.uml.model.members.Method;
import org.uml.model.members.MethodBase;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ISignedUMLWidget;
import org.uml.visual.widgets.actions.MemberNameEditor;
import org.uml.visual.widgets.popups.MemberBasePopupProvider;

/**
 *
 * @author Jelena
 */
public class MethodWidget extends MemberWidgetBase implements ISignedUMLWidget {

    public MethodWidget(ClassDiagramScene scene, Method method) {
        super(scene, method);
        this.component = method;
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());

        updateVisibilityLabel();
        this.addChild(visibilityLabel);

        nameLabel.setLabel(method.getLabelText());
        nameLabel.setFont(scene.getDefaultFont());
        setStatic(method.isStatic());
        this.addChild(nameLabel);
        nameLabel.getActions().addAction(ActionFactory.createInplaceEditorAction(new MemberNameEditor(this)));

        getActions().addAction(ActionFactory.createPopupMenuAction(new MemberBasePopupProvider(this)));
        updateVisibilityLabel();
    }

    @Override
    public void setSignature(String signature) {
        String oldSignature = component.getSignature();
        if (!signature.equals(oldSignature)) {
            if (component.getDeclaringComponent().signatureExists(signature)) {
                JOptionPane.showMessageDialog(null, "Member \"" + signature + "\" already exists!");
            } else {
                MemberParser.fillMethodComponents((MethodBase) component, signature);
            }
        }
    }

    @Override
    public String getSignature() {
        return component.getSignature();
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
        else if ("isFinal".equals(propName) || "isAbstract".equals(propName) || "isSynchronized".equals(propName) || "name".equals(propName) || "type".equals(propName)) {
            nameLabel.setLabel(((Method) component).getLabelText());
        }
        else if ("visibility".equals(evt.getPropertyName())) {
            updateVisibilityLabel();
        }
        getClassDiagramScene().getUmlTopComponent().modify();
        getScene().validate();
    }
}
