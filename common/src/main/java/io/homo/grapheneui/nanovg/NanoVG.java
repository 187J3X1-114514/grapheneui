package io.homo.grapheneui.nanovg;

import io.homo.grapheneui.core.Transform;
import io.homo.grapheneui.nanovg.renderer.NanoVGSvgRenderer;
import io.homo.grapheneui.nanovg.renderer.NanoVGTextRenderer;
import io.homo.grapheneui.utils.MinecraftUtil;
import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVGGL3.NVG_ANTIALIAS;
import static org.lwjgl.nanovg.NanoVGGL3.NVG_STENCIL_STROKES;

public class NanoVG {
    public static NanoVGRenderers RENDERER;
    public static NanoVGContext context;
    private static Transform currentTransform;

    public static NanoVGContext getContext() {
        return context;
    }

    public static void init() {
        context = new NanoVGContext(NVG_ANTIALIAS | NVG_STENCIL_STROKES);
        RENDERER = new NanoVGRenderers();
        NanoVGFontLoader.initAndLoad();
    }

    public static float getScreenWidth() {
        return MinecraftUtil.getScreenSize().x;
    }

    public static float getScreenHeight() {
        return MinecraftUtil.getScreenSize().y;
    }

    public static NVGColor colorRGB(int r, int g, int b) {
        return colorRGB(r / 255f, g / 255f, b / 255f);
    }

    public static NVGColor colorRGB(float r, float g, float b) {
        NVGColor color = NVGColor.calloc();
        return org.lwjgl.nanovg.NanoVG.nvgRGBf(r, g, b, color);
    }

    public static NVGColor colorRGBA(int r, int g, int b, int a) {
        return colorRGBA(r / 255f, g / 255f, b / 255f, a / 255f);
    }

    public static NVGColor colorRGBA(float r, float g, float b, float a) {
        NVGColor color = NVGColor.calloc();
        return org.lwjgl.nanovg.NanoVG.nvgRGBAf(r, g, b, a, color);
    }

    public static class NanoVGRenderers {
        public NanoVGTextRenderer TEXT = new NanoVGTextRenderer(context);
        public NanoVGSvgRenderer SVG = new NanoVGSvgRenderer(context);
    }


}
