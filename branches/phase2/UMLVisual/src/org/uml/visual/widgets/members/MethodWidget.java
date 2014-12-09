package org.uml.visual.widgets.members;

import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.openide.util.WeakListeners;
import org.uml.model.members.MemberBase;
import org.uml.model.members.Method;
import org.uml.model.members.MethodBase;
import org.uml.visual.parser.WidgetParser;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.providers.popups.MemberBasePopupProvider;

/**
 *
 * @author Jelena
 */
public class MethodWidget extends MemberWidgetBase implements PropertyChangeListener {

    LabelWidget visibilityLabel;
    LabelWidget nameWidget;
    WidgetParser wp;

    public MethodWidget(ClassDiagramScene scene, Method method) {
        super(scene, method);
        this.component = method;
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());

        visibilityLabel = new LabelWidget(getScene());
        updateVisibilityLabel();
        this.addChild(visibilityLabel);

        nameWidget = new LabelWidget(getScene());
        nameWidget.setLabel(method.getSignatureForLabel());
        nameWidget.setFont(scene.getDefaultFont());
        setStatic(method.isStatic());
        this.addChild(nameWidget);
        nameWidget.getActions().addAction(nameEditorAction);

        getActions().addAction(ActionFactory.createPopupMenuAction(new MemberBasePopupProvider(this)));
        wp = new WidgetParser();
        updateVisibilityLabel();
    }

    @Override
    public LabelWidget getNameLabel() {
        return nameWidget;
    }

    @Override
    public MemberBase getMember() {
        return component;
    }
    
    @Override
    public void setSignature(String signature) {
        String oldSignature = component.getSignatureWithoutModifiers();
        if (!signature.equals(oldSignature)) {
            if (component.getDeclaringComponent().signatureExists(signature)) {
                JOptionPane.showMessageDialog(null, "Member \"" + signature + "\" already exists!");
            } else {
                wp.fillMethodComponents((MethodBase) component, signature);
                component.getDeclaringComponent().notifyMemberSignatureChanged(component, oldSignature);
            }
        }
        nameWidget.setLabel(((MethodBase) component).getSignatureForLabel());
    }

    @Override
    public String getSignature() {
        return component.getSignatureWithoutModifiers();
    }

    public final void updateVisibilityLabel() {
        switch (component.getVisibility()) {
            case PUBLIC:
                visibilityLabel.setLabel("+");
                break;
            case PRIVATE:
                visibilityLabel.setLabel("-");
                break;
            case PROTECTED:
                visibilityLabel.setLabel("#");
                break;
            case PACKAGE:
                visibilityLabel.setLabel("~");
                break;
        }
    }

    @Override
    protected void setSelected(boolean isSelected) {
        if (isSelected) {
            visibilityLabel.setForeground(SELECT_FONT_COLOR);
            nameWidget.setForeground(SELECT_FONT_COLOR);
        } else {
            visibilityLabel.setForeground(DEFAULT_FONT_COLOR);
            nameWidget.setForeground(DEFAULT_FONT_COLOR);
        }
    }

    private void setStatic(boolean isStatic) {
        Map<TextAttribute, Integer> fontAttributes = new HashMap<>();
        if (isStatic) {
            fontAttributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        } else {
            fontAttributes.put(TextAttribute.UNDERLINE, -1);
        }
        nameWidget.setFont(nameWidget.getFont().deriveFont(fontAttributes));
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        if ("isStatic".equals(propName)) {
            setStatic((boolean) evt.getNewValue());
        }
        if ("isFinal".equals(propName) || "isAbstract".equals(propName) || "isSynchronized".equals(propName) || "name".equals(propName) || "type".equals(propName)) {
            nameWidget.setLabel(((Method) component).getSignatureForLabel());
        }
        if ("visibility".equals(evt.getPropertyName())) {
            updateVisibilityLabel();
        }
        getClassDiagramScene().getUmlTopComponent().modify();
        getScene().validate();
    }

}
