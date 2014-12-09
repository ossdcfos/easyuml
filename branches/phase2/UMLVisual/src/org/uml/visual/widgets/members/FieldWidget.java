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
import org.uml.model.members.Field;
import org.uml.model.members.MemberBase;
import static org.uml.model.Visibility.PACKAGE;
import static org.uml.model.Visibility.PRIVATE;
import static org.uml.model.Visibility.PROTECTED;
import static org.uml.model.Visibility.PUBLIC;
import org.uml.visual.parser.WidgetParser;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.providers.popups.MemberBasePopupProvider;

/**
 *
 * @author Jelena
 */
public class FieldWidget extends MemberWidgetBase implements PropertyChangeListener {

    LabelWidget visibilityLabel;
    LabelWidget nameWidget;
    WidgetParser wp;

    public FieldWidget(ClassDiagramScene scene, Field field) {
        super(scene, field);
        this.component = field;
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());

        visibilityLabel = new LabelWidget(getScene());
        updateVisibilityLabel();
        this.addChild(visibilityLabel);

        nameWidget = new LabelWidget(getScene());
        nameWidget.setLabel(field.getSignatureForLabel());
        nameWidget.setFont(scene.getDefaultFont());
        setStatic(field.isStatic());
        nameWidget.getActions().addAction(nameEditorAction);
        this.addChild(nameWidget);

        getActions().addAction(ActionFactory.createPopupMenuAction(new MemberBasePopupProvider(this)));
        wp = new WidgetParser();
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
                wp.fillFieldComponents((Field) component, signature);
                component.getDeclaringComponent().notifyMemberSignatureChanged(component, oldSignature);
            }
        }
        nameWidget.setLabel(((Field) component).getSignatureForLabel());
    }

    @Override
    public String getSignature() {
        return ((Field) component).getSignatureWithoutModifiers();
    }

    public final void updateVisibilityLabel() {
//        if (component != null && component.getVisibility() != null) {
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
//        }
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
        if ("isFinal".equals(propName) || "isTransient".equals(propName) || "isVolatile".equals(propName) || "name".equals(propName) || "type".equals(propName)) {
            nameWidget.setLabel(((Field) component).getSignatureForLabel());
        }
        if ("visibility".equals(evt.getPropertyName())) {
            updateVisibilityLabel();
        }
        getClassDiagramScene().getUmlTopComponent().modify();
        getScene().validate();
    }

}
