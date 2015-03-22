package org.uml.visual.themes;

import java.awt.Color;
import org.netbeans.api.visual.border.Border;

/**
 *
 * @author Boris Perović
 */
public interface Theme {
    
    public static final Color TRANSPARENT = new Color(0, 0, 0, 1);
    
    public Color getDefaultColor();
    public Color getHoverColor();
    public Color getSelectColor();
    public Color getHoverSelectColor();
    
    public Color getDefaultBorderColor();
    public Color getHoverBorderColor();
    public Color getSelectBorderColor();
    public Border getDefaultBorder();
    public Border getHoverBorder();
    public Border getSelectBorder();
    
    public Color getMemberDefaultColor();
    public Color getMemberHoverColor();
    public Color getMemberSelectColor();
    
    public Border getMemberDefaultBorder();
    public Border getMemberHoverBorder();
    public Border getMemberSelectBorder();
    
    public Color getAddMemberDefaultColor();
    public Color getAddMemberHoverColor();
    
    public Border getAddMemberDefaultBorder();
    public Border getAddMemberHoverBorder();
    
    public Color getDefaultFontColor();
    public Color getHoverFontColor();
    public Color getSelectFontColor();

    public String getName();
}
