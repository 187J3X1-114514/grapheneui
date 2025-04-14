package io.homo.grapheneui.nanovg;

import io.homo.grapheneui.GrapheneUIMod;

public class NanoVGFontLoader {
    public static NanoVGFont[] FONTS = new NanoVGFont[]{

    };

    public static NanoVGFont FONT;

    public static void initAndLoad() {
        FONT = new NanoVGFont("HarmonyOSFont Medium", "/fonts/HarmonyOS_Sans_SC_Medium.ttf");
        FONT.load();
        GrapheneUIMod.LOGGER.info("加载字体 {} 路径 {}", FONT.name, FONT.path);
        for (NanoVGFont font : FONTS) {
            font.load();
            GrapheneUIMod.LOGGER.info("加载字体 {} 路径 {}", font.name, font.path);
        }
    }
}
