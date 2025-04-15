package io.homo.grapheneui.nanovg;

import io.homo.grapheneui.GrapheneUIMod;

import java.util.HashMap;
import java.util.Map;

public class NanoVGFontLoader {
    public static final String MEDIUM_VARIATION = "medium";
    public static final String BLACK_VARIATION = "black";
    public static final String BOLD_VARIATION = "bold";
    public static final String LIGHT_VARIATION = "light";
    public static final String THIN_VARIATION = "thin";
    public static final String REGULAR_VARIATION = "regular";
    public static Map<String, NanoVGFont> FONT_MAP = new HashMap<>();

    public static void initAndLoad() {
        FONT_MAP.put(MEDIUM_VARIATION, new NanoVGFont("HarmonyOSFont Medium", "/fonts/HarmonyOS_Sans_SC_Medium.ttf"));
        FONT_MAP.put(BLACK_VARIATION, new NanoVGFont("HarmonyOSFont Black", "/fonts/HarmonyOS_Sans_SC_Black.ttf"));
        FONT_MAP.put(BOLD_VARIATION, new NanoVGFont("HarmonyOSFont Bold", "/fonts/HarmonyOS_Sans_SC_Bold.ttf"));
        FONT_MAP.put(LIGHT_VARIATION, new NanoVGFont("HarmonyOSFont Light", "/fonts/HarmonyOS_Sans_SC_Light.ttf"));
        FONT_MAP.put(REGULAR_VARIATION, new NanoVGFont("HarmonyOSFont Regular", "/fonts/HarmonyOS_Sans_SC_Regular.ttf"));
        FONT_MAP.put(THIN_VARIATION, new NanoVGFont("HarmonyOSFont Thin", "/fonts/HarmonyOS_Sans_SC_Thin.ttf"));

        for (NanoVGFont font : FONT_MAP.values()) {
            font.load();
            GrapheneUIMod.LOGGER.info("加载字体 {} 路径 {}", font.name, font.path);
        }
    }
}
