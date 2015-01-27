package org.uml.visual.widgets.members;

import java.awt.Color;
import java.beans.PropertyChangeListener;
import org.netbeans.api.visual.action.ActionFactory;
import org.netbeans.api.visual.border.BorderFactory;
import org.netbeans.api.visual.layout.LayoutFactory;
import org.netbeans.api.visual.model.ObjectState;
import org.netbeans.api.visual.widget.LabelWidget;
import org.openide.util.WeakListeners;
import org.uml.model.members.MemberBase;
import org.uml.visual.colorthemes.ColorTheme;
import org.uml.visual.widgets.ClassDiagramScene;
import org.uml.visual.widgets.components.ComponentWidgetBase;
import static org.uml.visual.widgets.members.MemberParentAlignWithMoveStrategyProvider.ALIGN_WITH_MOVE_DECORATOR_DEFAULT;
import org.uml.visual.widgets.popups.MemberBasePopupProvider;

/**
 *
 * @author Jelena
 */
public abstract class MemberWidgetBase extends LabelWidget implements PropertyChangeListener {

    protected MemberBase member;
    protected LabelWidget visibilityLabel = new LabelWidget(getScene());
    protected LabelWidget nameLabel = new LabelWidget(getScene());

    public MemberWidgetBase(ClassDiagramScene scene, MemberBase member) {
        super(scene);
        this.member = member;
        scene.addObject(member, this);
        setOpaque(true);
        setBackground(getColorTheme().getMemberDefaultColor());
        setBorder(getColorTheme().getMemberDefaultBorder());

        this.member.addPropertyChangeListener(WeakListeners.propertyChange(this, this.member));
        this.setLayout(LayoutFactory.createHorizontalFlowLayout());

        // To support hovering and selecting (in notifyStateChanged), otherwise a Provider is needed
        getActions().addAction(scene.createSelectAction());
        getActions().addAction(ActionFactory.createPopupMenuAction(new MemberBasePopupProvider(this)));

        MemberParentAlignWithMoveStrategyProvider mpsp = new MemberParentAlignWithMoveStrategyProvider(new SingleLayerAlignWithWidgetCollector(scene.getMainLayer(), false), scene.getInterractionLayer(), ALIGN_WITH_MOVE_DECORATOR_DEFAULT, false);
        getActions().addAction(ActionFactory.createMoveAction(mpsp, mpsp));

        getActions().addAction(scene.createWidgetHoverAction());
    }

    public final MemberBase getMember() {
        return member;
    }

    public final ClassDiagramScene getClassDiagramScene() {
        return (ClassDiagramScene) getScene();
    }

    public static final ColorTheme getColorTheme() {
        return ClassDiagramScene.colorTheme;
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
}
