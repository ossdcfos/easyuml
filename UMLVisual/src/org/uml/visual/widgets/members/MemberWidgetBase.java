package org.uml.visual.widgets.members;

import java.awt.Dimension;
import org.uml.visual.widgets.providers.MemberParentAlignWithMoveStrategyProvider;
import java.awt.Font;
import java.awt.Image;
import java.beans.PropertyChangeListener;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.ImageWidget;
import org.netbeans.api.visual.widget.LabelWidget;
import org.netbeans.api.visual.widget.Widget;
import org.openide.util.ImageUtilities;
import org.openide.util.WeakListeners;
import org.uml.model.members.Constructor;
import org.uml.model.members.Field;
import org.uml.model.members.Literal;
import org.uml.model.members.MemberBase;
import org.uml.model.members.Method;
import org.uml.visual.themes.Theme;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.ResampledImageWidget;
import org.uml.visual.widgets.components.ComponentWidgetBase;
import org.uml.visual.widgets.popups.MemberBasePopupProvider;

/**
 *
 * @author Jelena
 */
public abstract class MemberWidgetBase extends Widget implements PropertyChangeListener {

    protected MemberBase member;
    protected LabelWidget visibilityLabel = new LabelWidget(getScene());
    protected LabelWidget nameLabel = new LabelWidget(getScene());

    protected static String iconFolderPath = "org/uml/visual/widgets/icons/";
    protected ResampledImageWidget iconWidget = new ResampledImageWidget(getScene());

    public MemberWidgetBase(ClassDiagramScene scene, MemberBase member) {
        super(scene);
        this.member = member;
        scene.addObject(member, this);
        setOpaque(true);
        setBackground(getColorTheme().getMemberDefaultColor());
        setBorder(getColorTheme().getMemberDefaultBorder());
        setLayout(LayoutFactory.createHorizontalFlowLayout(LayoutFactory.SerialAlignment.CENTER, 0));

        iconWidget.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 3));
        updateIcon();
        updateIconDisplay(scene.isShowIcons());
        
        visibilityLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 1));
        visibilityLabel.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 10));
        updateVisibilityLabel();

        this.member.addPropertyChangeListener(WeakListeners.propertyChange(this, this.member));

        // To support hovering and selecting (in notifyStateChanged), otherwise a Provider is needed
        getActions().addAction(scene.createSelectAction());
        getActions().addAction(ActionFactory.createPopupMenuAction(new MemberBasePopupProvider(this)));

        MemberParentAlignWithMoveStrategyProvider mpsp = new MemberParentAlignWithMoveStrategyProvider(scene, false);
        getActions().addAction(ActionFactory.createMoveAction(mpsp, mpsp));

        getActions().addAction(scene.createWidgetHoverAction());
    }

    public final MemberBase getMember() {
        return member;
    }

    public final ClassDiagramScene getClassDiagramScene() {
        return (ClassDiagramScene) getScene();
    }

    public final Theme getColorTheme() {
        return getClassDiagramScene().getColorTheme();
    }

    // used for InplaceEditorAction
    public final LabelWidget getNameLabel() {
        return nameLabel;
    }

    protected void notifyTopComponentModified() {
        getClassDiagramScene().getUmlTopComponent().notifyModified();
    }

    @Override
    public void notifyStateChanged(ObjectState previousState, ObjectState state) {
        // Reaction to hover, focus and selection goes here
        super.notifyStateChanged(previousState, state);

        // in case it has not yet been initialized return (adding to the scene calls notifyStateChanged, before the full object has been initialized)
        if (getParentWidget() == null) return;

        ComponentWidgetBase componentWidget = (ComponentWidgetBase) getParentWidget().getParentWidget();

        boolean focused = state.isFocused();
        boolean hovered = state.isHovered();

        if (focused) {
            setSelectedLook(state);
            setBorder(getColorTheme().getMemberSelectBorder());
            setBackground(getColorTheme().getMemberSelectColor());
            if (hovered) componentWidget.setState(componentWidget.getState().deriveWidgetHovered(true));
        } else if (hovered) {
            setSelectedLook(state);
            setBorder(getColorTheme().getMemberHoverBorder());
            setBackground(getColorTheme().getMemberHoverColor());
            componentWidget.setState(componentWidget.getState().deriveWidgetHovered(true));
        } else {
            setSelectedLook(state);
            setBorder(getColorTheme().getMemberDefaultBorder());
            setBackground(getColorTheme().getMemberDefaultColor());
            componentWidget.setState(componentWidget.getState().deriveWidgetHovered(false));
        }
    }

    public final void updateVisibilityLabel() {
        switch (member.getVisibility()) {
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

    public final void updateIcon() {
        String lowercaseVisibility = member.getVisibility().toString().toLowerCase();
        String suffix = lowercaseVisibility.substring(0, 1).toUpperCase() + lowercaseVisibility.substring(1);
        if (member instanceof Field) {
            Field field = (Field) member;
            if (field.isStatic()) suffix = "Static" + suffix;
            iconWidget.setImage(ImageUtilities.loadImage(iconFolderPath + "fields/field" + suffix + ".png"));
        } else if (member instanceof Method) {
            Method method = (Method) member;
            if (method.isStatic()) suffix = "Static" + suffix;
            iconWidget.setImage(ImageUtilities.loadImage(iconFolderPath + "methods/method" + suffix + ".png"));
        } else if (member instanceof Constructor) {
            iconWidget.setImage(ImageUtilities.loadImage(iconFolderPath + "constructors/constructor" + suffix + ".png"));
        } else if (member instanceof Literal) {
            iconWidget.setImage(ImageUtilities.loadImage(iconFolderPath + "other/literal.png"));
        }
        iconWidget.setMaximumSize(new Dimension(12,12));
    }

    // used for setting the font etc.
    protected void setSelectedLook(ObjectState state) {
        boolean focused = state.isFocused();
        boolean hovered = state.isHovered();

        if (focused) {
            visibilityLabel.setForeground(getColorTheme().getSelectFontColor());
            nameLabel.setForeground(getColorTheme().getSelectFontColor());
        } else if (hovered) {
            visibilityLabel.setForeground(getColorTheme().getHoverFontColor());
            nameLabel.setForeground(getColorTheme().getHoverFontColor());
        } else {
            visibilityLabel.setForeground(getColorTheme().getDefaultFontColor());
            nameLabel.setForeground(getColorTheme().getDefaultFontColor());
        }
    }

    public void updateColor() {
        notifyStateChanged(getState(), getState());
    }

    public final void updateIconDisplay(boolean iconDisplayEnabled) {
        if (iconDisplayEnabled) iconWidget.setVisible(true);
        else iconWidget.setVisible(false);
    }

    public void updateTypeNamesDisplay(boolean simpleTypeNamesDisplayEnabled) {
        nameLabel.setLabel(member.getLabelText(simpleTypeNamesDisplayEnabled));
    }
}
