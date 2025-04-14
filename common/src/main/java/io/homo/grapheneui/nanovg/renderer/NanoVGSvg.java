package io.homo.grapheneui.nanovg.renderer;

import io.homo.grapheneui.opengl.texture.GlTexture;

public class NanoVGSvg {
    public String svg;
    public GlTexture glTex;
    public int nvgImg;

    public NanoVGSvg(String svg, GlTexture glTex, int nvgImg) {
        this.svg = svg;
        this.glTex = glTex;
        this.nvgImg = nvgImg;
    }
}
