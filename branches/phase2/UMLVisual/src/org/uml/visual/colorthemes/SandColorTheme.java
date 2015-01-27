package org.uml.visual.colorthemes;

import java.awt.Color;
import java.awt.color.ColorSpace;
import org.netbeans.api.visual.border.Border;
import org.netbeans.api.visual.border.BorderFactory;
import org.uml.visual.widgets.TranslucentCompositeBorder;
import static org.uml.visual.widgets.components.ComponentWidgetBase.SELECT_BORDER_SIZE;

/**
 *
 * @author Boris
 */
public class SandColorTheme implements ColorTheme {

    // Component - colors
    private static final float BASIC_ALPHA = 0.20f;
    protected static final Color BASIC_COLOR = new Color(0xFFE0D67D, true);
    protected static final Color DEFAULT_COLOR = new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB), BASIC_COLOR.getComponents(null), BASIC_ALPHA);
    protected static final Color HOVER_COLOR = new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB), BASIC_COLOR.getComponents(null), BASIC_ALPHA + 0.15f);
    protected static final Color SELECT_COLOR = new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB), BASIC_COLOR.getComponents(null), BASIC_ALPHA + 0.20f);
    protected static final Color HOVER_SELECT_COLOR = new Color(ColorSpace.getInstance(ColorSpace.CS_sRGB), BASIC_COLOR.getComponents(null), BASIC_ALPHA + 0.30f);

    // Component - borders
    protected static final Color DEFAULT_BORDER_COLOR = Color.BLACK;
    private static final Color HOVER_BORDER_COLOR = new Color(0xFFB6212D, true);
    private static final Color SELECT_BORDER_COLOR = HOVER_BORDER_COLOR;
    protected static Border DEFAULT_BORDER = new TranslucentCompositeBorder(BorderFactory.createEmptyBorder(SELECT_BORDER_SIZE), BorderFactory.createLineBorder(1, DEFAULT_BORDER_COLOR));
    protected static Border HOVER_BORDER = new TranslucentCompositeBorder(BorderFactory.createEmptyBorder(SELECT_BORDER_SIZE), BorderFactory.createLineBorder(1, HOVER_BORDER_COLOR));
    protected static Border SELECT_BORDER = new TranslucentCompositeBorder(BorderFactory.createResizeBorder(SELECT_BORDER_SIZE, SELECT_BORDER_COLOR, false), BorderFactory.createEmptyBorder(1));

    // Member - colors
    protected static final Color DEFAULT_MEMBER_COLOR = TRANSPARENT;
    protected static final Color HOVER_MEMBER_COLOR = new Color(0x3FB6212D, true);
    protected static final Color SELECT_MEMBER_COLOR = new Color(0x7FB6212D, true);
    // Member - borders
    protected static Border DEFAULT_MEMBER_BORDER = BorderFactory.createEmptyBorder(1);
    protected static Border HOVER_MEMBER_BORDER = BorderFactory.createLineBorder(1, HOVER_BORDER_COLOR);
    protected static Border SELECT_MEMBER_BORDER = BorderFactory.createLineBorder(1, SELECT_BORDER_COLOR); //0x0096FF33 0x0000A1

    // Add - colors
    protected static final Color DEFAULT_ADD_MEMBER_COLOR = TRANSPARENT;
    protected static final Color HOVER_ADD_MEMBER_COLOR = new Color(0x3FB6212D, true);
    // Add - border
    protected static Border DEFAULT_ADD_MEMBER_BORDER = BorderFactory.createEmptyBorder(1);
    protected static Border HOVER_ADD_MEMBER_BORDER = BorderFactory.createLineBorder(1, HOVER_BORDER_COLOR);

    // Member - font colors
    protected static final Color DEFAULT_FONT_COLOR = Color.BLACK;
    protected static final Color HOVER_FONT_COLOR = DEFAULT_FONT_COLOR;
    protected static final Color SELECT_FONT_COLOR = Color.WHITE;

    @Override
    public Color getDefaultColor() {
        return DEFAULT_COLOR;
    }

    @Override
    public Color getHoverColor() {
        return HOVER_COLOR;
    }

    @Override
    public Color getSelectColor() {
        return SELECT_COLOR;
    }

    @Override
    public Color getHoverSelectColor() {
        return HOVER_SELECT_COLOR;
    }

    @Override
    public Color getDefaultBorderColor() {
        return DEFAULT_BORDER_COLOR;
    }

    @Override
    public Color getHoverBorderColor() {
        return HOVER_BORDER_COLOR;
    }

    @Override
    public Color getSelectBorderColor() {
        return SELECT_BORDER_COLOR;
    }

    @Override
    public Border getDefaultBorder() {
        return DEFAULT_BORDER;
    }

    @Override
    public Border getHoverBorder() {
        return HOVER_BORDER;
    }

    @Override
    public Border getSelectBorder() {
        return SELECT_BORDER;
    }

    @Override
    public Color getMemberDefaultColor() {
        return DEFAULT_MEMBER_COLOR;
    }

    @Override
    public Color getMemberHoverColor() {
        return HOVER_MEMBER_COLOR;
    }

    @Override
    public Color getMemberSelectColor() {
        return SELECT_MEMBER_COLOR;
    }

    @Override
    public Border getMemberDefaultBorder() {
        return DEFAULT_MEMBER_BORDER;
    }

    @Override
    public Border getMemberHoverBorder() {
        return HOVER_MEMBER_BORDER;
    }

    @Override
    public Border getMemberSelectBorder() {
        return SELECT_MEMBER_BORDER;
    }

    @Override
    public Color getAddMemberDefaultColor() {
        return DEFAULT_ADD_MEMBER_COLOR;
    }

    @Override
    public Color getAddMemberHoverColor() {
        return HOVER_ADD_MEMBER_COLOR;
    }

    @Override
    public Border getAddMemberDefaultBorder() {
        return DEFAULT_ADD_MEMBER_BORDER;
    }

    @Override
    public Border getAddMemberHoverBorder() {
        return HOVER_ADD_MEMBER_BORDER;
    }

    @Override
    public Color getDefaultFontColor() {
        return DEFAULT_FONT_COLOR;
    }

    @Override
    public Color getHoverFontColor() {
        return HOVER_FONT_COLOR;
    }

    @Override
    public Color getSelectFontColor() {
        return SELECT_FONT_COLOR;
    }
}
