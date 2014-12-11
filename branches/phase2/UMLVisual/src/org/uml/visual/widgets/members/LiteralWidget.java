package org.uml.visual.widgets.members;

import java.beans.PropertyChangeEvent;
import javax.swing.JOptionPane;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.widget.LabelWidget;
import org.openide.util.WeakListeners;
import org.uml.model.members.Field;
import org.uml.model.members.Literal;
import org.uml.model.members.MemberBase;
import org.uml.model.members.Method;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.providers.popups.MemberBasePopupProvider;

/**
 *
 * @author Jelena
 */
public class LiteralWidget extends MemberWidgetBase {

    public LiteralWidget(ClassDiagramScene scene, Literal literal) {
        super(scene, literal);
        this.component.addPropertyChangeListener(WeakListeners.propertyChange(this, this.component));
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());

        nameWidget = new LabelWidget(getScene());
        nameWidget.setLabel(literal.getName());
        this.addChild(nameWidget);
        nameWidget.getActions().addAction(nameEditorAction);

        getActions().addAction(ActionFactory.createPopupMenuAction(new MemberBasePopupProvider(this)));
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
    protected void setSelected(boolean isSelected) {
        if (isSelected) {
            nameWidget.setForeground(SELECT_FONT_COLOR);
        } else {
            nameWidget.setForeground(DEFAULT_FONT_COLOR);
        }
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
        nameWidget.setLabel(((Literal) component).getSignatureForLabel());
    }

    @Override
    public String getSignature() {
        return component.getSignature();
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String propName = evt.getPropertyName();
        if ("name".equals(propName)) {
            nameWidget.setLabel(((Literal) component).getSignatureForLabel());
        }
        changedNotify();
        getScene().validate();
    }

}
