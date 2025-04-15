package io.homo.grapheneui;

import io.homo.grapheneui.nanovg.NanoVGFont;
import io.homo.grapheneui.nanovg.NanoVGFontLoader;

public class GrapheneUI {
    public static GrapheneTheme THEME = new GrapheneTheme().dark();
    public static GrapheneConst CONST = new GrapheneConst();

    public static NanoVGFont mediumFont() {
        return NanoVGFontLoader.FONT_MAP.get(NanoVGFontLoader.MEDIUM_VARIATION);
    }

    public static NanoVGFont blackFont() {
        return NanoVGFontLoader.FONT_MAP.get(NanoVGFontLoader.BLACK_VARIATION);

    }

    public static NanoVGFont boldFont() {
        return NanoVGFontLoader.FONT_MAP.get(NanoVGFontLoader.BOLD_VARIATION);

    }

    public static NanoVGFont lightFont() {
        return NanoVGFontLoader.FONT_MAP.get(NanoVGFontLoader.LIGHT_VARIATION);

    }

    public static NanoVGFont thinFont() {
        return NanoVGFontLoader.FONT_MAP.get(NanoVGFontLoader.THIN_VARIATION);

    }

    public static NanoVGFont regularFont() {
        return NanoVGFontLoader.FONT_MAP.get(NanoVGFontLoader.REGULAR_VARIATION);
    }

}
