package org.uml.visual.colorthemes;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Boris
 */
public class ColorThemesStore {
    private static final Map<String, ColorTheme> colorThemes = new HashMap<>();
    static {
        colorThemes.put("blue-gray", new BlueGrayColorTheme());
        colorThemes.put("sand", new SandColorTheme());
    }
    public static final ColorTheme DEFAULT_COLOR_THEME = new BlueGrayColorTheme();    
    
    public static ColorTheme getColorTheme(String name){
        if(colorThemes.containsKey(name)) return colorThemes.get(name);
        else return DEFAULT_COLOR_THEME;
    }
}
