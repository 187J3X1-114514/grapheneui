package io.homo.grapheneui.nanovg.renderer;

import org.lwjgl.nanovg.NanoVG;

public record TextAlign(int horizontal, int vertical) {
    //horizontal
    public static final int ALIGN_LEFT = NanoVG.NVG_ALIGN_LEFT;
    public static final int ALIGN_CENTER = NanoVG.NVG_ALIGN_CENTER;
    public static final int ALIGN_RIGHT = NanoVG.NVG_ALIGN_RIGHT;
    //vertical
    public static final int ALIGN_TOP = NanoVG.NVG_ALIGN_TOP;
    public static final int ALIGN_MIDDLE = NanoVG.NVG_ALIGN_MIDDLE;
    public static final int ALIGN_BOTTOM = NanoVG.NVG_ALIGN_BOTTOM;

    public static TextAlign of(int horizontal, int vertical) {
        return new TextAlign(horizontal, vertical);
    }
}
