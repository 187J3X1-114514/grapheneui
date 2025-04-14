package io.homo.grapheneui.nanovg.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.GrapheneUIMod;
import io.homo.grapheneui.nanovg.NanoVGContext;
import io.homo.grapheneui.nanovg.NanoVGRendererBase;
import io.homo.grapheneui.opengl.GlState;
import io.homo.grapheneui.opengl.texture.GlTexture;
import io.homo.grapheneui.opengl.texture.TextureFormat;
import io.homo.grapheneui.utils.AsyncTimer;
import org.lwjgl.nanovg.NSVGImage;
import org.lwjgl.system.MemoryUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.lwjgl.nanovg.NanoSVG.*;
import static org.lwjgl.nanovg.NanoVGGL3.nvglCreateImageFromHandle;
import static org.lwjgl.opengl.GL43.*;

public class NanoVGSvgRenderer extends NanoVGRendererBase {
    public static NanoVGSvgRenderer INSTANCE;
    private final long rastPtr;
    private final Map<String, NanoVGSvg> cache = new HashMap<>();

    public NanoVGSvgRenderer(NanoVGContext context) {
        this.rastPtr = context.rastPtr;
        INSTANCE = this;
    }

    public NanoVGSvg getOrCreate(String svg) {
        if (cache.containsKey(svg)) {
            return cache.get(svg);
        }
        NSVGImage image = nsvgParse(svg, "px", 96f);
        if (image == null) {
            GrapheneUIMod.LOGGER.error("SVG解析失败: {}", svg);
            throw new RuntimeException("无法解析SVG");
        }
        try {
            int iconSize = GrapheneUI.CONST.ICON_SIZE;
            int bufferSize = iconSize * iconSize * 4;
            long buffer = MemoryUtil.nmemCalloc(1, bufferSize);
            nnsvgRasterize(
                    rastPtr,
                    image.address(),
                    0,
                    0,
                    1.0f,
                    buffer,
                    iconSize,
                    iconSize,
                    iconSize * 4
            );
            GlTexture texture;
            try (GlState ignored = new GlState()) {
                texture = GlTexture.create(iconSize, iconSize, TextureFormat.RGBA8);
                glBindTexture(GL_TEXTURE_2D, texture.id);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
                nglTexSubImage2D(
                        GL_TEXTURE_2D,
                        0,
                        0,
                        0,
                        iconSize,
                        iconSize,
                        GL_RGBA,
                        GL_UNSIGNED_BYTE,
                        buffer
                );
                new AsyncTimer().schedule(
                        () -> RenderSystem.recordRenderCall(() -> MemoryUtil.nmemFree(buffer)),
                        10,
                        TimeUnit.SECONDS
                );
            }

            int nvgTex = nvglCreateImageFromHandle(
                    contextPtr,
                    texture.id,
                    iconSize,
                    iconSize,
                    0
            );
            NanoVGSvg result = new NanoVGSvg(svg, texture, nvgTex);
            cache.put(svg, result);
            return result;
        } finally {
            nsvgDelete(image);
        }
    }
}
