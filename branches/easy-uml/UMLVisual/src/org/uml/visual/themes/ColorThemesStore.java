package org.uml.visual.themes;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Boris Perović
 */
public class ColorThemesStore {

    private static final Map<String, Theme> colorThemes = new HashMap<>();

    static {
        Theme ct;
        ct = new BlueGrayTheme();
        colorThemes.put(ct.getName(), ct);
        ct = new SandRedTheme();
        colorThemes.put(ct.getName(), ct);
    }
    public static final Theme DEFAULT_COLOR_THEME = new BlueGrayTheme();

    public static Theme getColorTheme(String name) {
        if (colorThemes.containsKey(name)) return colorThemes.get(name);
        else return DEFAULT_COLOR_THEME;
    }
}
