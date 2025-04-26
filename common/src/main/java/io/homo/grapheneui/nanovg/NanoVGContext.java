package io.homo.grapheneui.nanovg;

import io.homo.grapheneui.core.Transform;
import io.homo.grapheneui.impl.Vec2;
import io.homo.grapheneui.opengl.GlStates;
import io.homo.grapheneui.opengl.framebuffer.BindPoint;
import io.homo.grapheneui.opengl.framebuffer.GlFrameBuffer;
import io.homo.grapheneui.opengl.texture.TextureFormat;
import io.homo.grapheneui.utils.Color;
import io.homo.grapheneui.utils.MinecraftUtil;
import net.minecraft.client.Minecraft;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoSVG;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.nanovg.NanoVGGL3;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.lwjgl.opengl.GL30.*;

public class NanoVGContext {

    private static Transform S_currentTransform;
    private static Transform S_globalTransform;
    public GlFrameBuffer frameBuffer;
    public long contextPtr = -1;
    public long rastPtr = -1;
    /// 状态
    private Color S_fillColor = Color.black();
    private Color S_strokeColor = Color.black();
    private float S_strokeWidth = 1f;
    private float S_alpha = 1f;

    ///

    public NanoVGContext(int nvgFlags) {
        contextPtr = NanoVGGL3.nvgCreate(nvgFlags);
        rastPtr = NanoSVG.nsvgCreateRasterizer();
        frameBuffer = GlFrameBuffer.create(
                TextureFormat.RGBA8,
                TextureFormat.DEPTH24_STENCIL8,
                (int) MinecraftUtil.getScreenSize().x,
                (int) MinecraftUtil.getScreenSize().y
        );
        frameBuffer.setClearColor(0, 0, 0, 1);
    }

    public void begin(boolean copyMinecraftFbo) {
        Vec2 screenSize = MinecraftUtil.getScreenSize();
        GlStates.save("nanovg-frame");
        if (
                frameBuffer.getWidth() != ((int) screenSize.x) ||
                        frameBuffer.getHeight() != ((int) screenSize.y)
        ) {
            frameBuffer.resizeFrameBuffer((int) screenSize.x, (int) screenSize.y);
        }
        frameBuffer.bind(BindPoint.ALL);
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
        if (copyMinecraftFbo) {
            glBindFramebuffer(GL_READ_FRAMEBUFFER, Minecraft.getInstance().getMainRenderTarget().frameBufferId);
            glBindFramebuffer(GL_DRAW_FRAMEBUFFER, frameBuffer.getFrameBufferId());
            glBlitFramebuffer(
                    0,
                    0,
                    Minecraft.getInstance().getMainRenderTarget().width,
                    Minecraft.getInstance().getMainRenderTarget().height,
                    0,
                    0,
                    frameBuffer.getWidth(),
                    frameBuffer.getHeight(),
                    GL_COLOR_BUFFER_BIT,
                    GL_LINEAR
            );
        }

        NanoVG.nvgBeginFrame(
                contextPtr,
                screenSize.x,
                screenSize.y,
                1.0f
        );
    }

    public void end() {
        NanoVG.nvgEndFrame(contextPtr);
        glBindFramebuffer(GL_READ_FRAMEBUFFER, frameBuffer.getFrameBufferId());
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, Minecraft.getInstance().getMainRenderTarget().frameBufferId);
        glBlitFramebuffer(
                0,
                0,
                frameBuffer.getWidth(),
                frameBuffer.getHeight(),
                0,
                0,
                Minecraft.getInstance().getMainRenderTarget().width,
                Minecraft.getInstance().getMainRenderTarget().height,
                GL_COLOR_BUFFER_BIT,
                GL_LINEAR
        );
        GlStates.pop("nanovg-frame").restore();

    }

    public void save() {
        NanoVG.nvgSave(contextPtr);
    }

    public void restore() {
        NanoVG.nvgRestore(contextPtr);
    }

    public void line(
            float x1, float y1,
            float x2, float y2,
            float lineWidth
    ) {
        NanoVG.nvgMoveTo(contextPtr, x1, y1);
        NanoVG.nvgLineTo(contextPtr, x2, y2);
        NanoVG.nvgStrokeWidth(contextPtr, lineWidth);
    }

    public void rect(
            float x, float y,
            float width, float height
    ) {
        NanoVG.nvgRect(contextPtr, x, y, width, height);
    }

    public void roundedRect(
            float x, float y,
            float width, float height,
            float radius
    ) {
        NanoVG.nvgRoundedRect(contextPtr, x, y, width, height, radius);
    }

    public void beginPath() {
        NanoVG.nvgBeginPath(contextPtr);
    }

    public void endPath(boolean fill) {
        if (fill) {
            NanoVG.nvgFill(contextPtr);
        } else {
            NanoVG.nvgStroke(contextPtr);
        }
    }

    public void endPath() {
        NanoVG.nvgFill(contextPtr);
    }

    public void drawLine(
            float x1, float y1,
            float x2, float y2,
            Color color,
            float lineWidth
    ) {
        beginPath();
        fillColor(color);
        line(x1, y1, x2, y2, lineWidth);
        endPath();
    }

    public void drawRect(
            float x, float y,
            float width, float height,
            Color color,
            boolean fill
    ) {
        beginPath();
        drawColor(fill, color);
        rect(x, y, width, height);
        endPath(fill);
    }

    public void drawRoundedRect(
            float x, float y,
            float width, float height,
            float radius,
            Color color,
            boolean fill
    ) {
        beginPath();
        drawColor(fill, color);
        roundedRect(x, y, width, height, radius);
        endPath(fill);
    }

    public void imagePattern(
            float ox,
            float oy,
            float ex,
            float ey,
            float width,
            float height,
            float angle,
            float alpha,
            int image
    ) {
        NVGPaint imgPaint = nvgImagePattern(
                contextPtr,
                ox,
                oy,
                ex,
                ey,
                angle,
                image,
                alpha,
                NVGPaint.calloc()
        );
        nvgFillPaint(contextPtr, imgPaint);
    }

    public float globalAlpha() {
        return S_alpha;
    }

    public void globalAlpha(float alpha) {
        S_alpha = alpha;
    }

    public void resetScissor() {
        NanoVG.nvgResetScissor(contextPtr);
    }

    public void scissor(
            float x,
            float y,
            float width,
            float height
    ) {
        NanoVG.nvgScissor(
                contextPtr,
                x,
                y,
                width,
                height
        );
    }

    public void transform(Transform transform) {
        resetTransform();

        if (S_globalTransform != null) {
            float[] globalMat = S_globalTransform.transformMatrix();
            nvgTransform(contextPtr, globalMat[0], globalMat[1], globalMat[2],
                    globalMat[3], globalMat[4], globalMat[5]);
        }

        if (transform != null) {
            float[] mat = transform.transformMatrix();
            nvgTransform(contextPtr, mat[0], mat[1], mat[2], mat[3], mat[4], mat[5]);
            S_currentTransform = transform;
        }
    }

    public void globalTransform(Transform globalTransform) {
        if (globalTransform == null) {
            S_globalTransform = null;
            if (S_currentTransform != null) {
                transform(S_currentTransform);
            }
            return;
        }
        S_globalTransform = globalTransform;
        transform(null);
    }

    public void resetTransform() {
        nvgResetTransform(contextPtr);
        S_currentTransform = null;
    }

    public void resetGlobalTransform() {
        nvgResetTransform(contextPtr);
        S_globalTransform = null;
    }


    public NVGPaint linearGradient(
            float startX,
            float startY,
            float endX,
            float endY,
            Color from,
            Color to
    ) {
        return linearGradient(
                startX,
                startY,
                endX,
                endY,
                from,
                to,
                NVGPaint.calloc()
        );
    }

    public NVGPaint linearGradient(
            float startX,
            float startY,
            float endX,
            float endY,
            Color from,
            Color to,
            NVGPaint srcPaint
    ) {
        from = from.copy().alpha((int) (globalAlpha() * from.alpha()));
        to = to.copy().alpha((int) (globalAlpha() * to.alpha()));

        NVGPaint paint = NanoVG.nvgLinearGradient(
                contextPtr,
                startX,
                startY,
                endX,
                endY,
                from.nvg(),
                to.nvg(),
                srcPaint
        );
        nvgFillPaint(contextPtr, paint);
        return paint;
    }

    public void strokeWidth(float width) {
        S_strokeWidth = width;
        NanoVG.nvgStrokeWidth(contextPtr, width);
    }

    public void strokeColor(Color color) {
        color = color.copy().alpha((int) (globalAlpha() * color.alpha()));
        S_strokeColor = Color.rgba(color.integer());
        NanoVG.nvgStrokeColor(contextPtr, color.nvg());
    }

    public void fillColor(Color color) {
        color = color.copy().alpha((int) (globalAlpha() * color.alpha()));
        S_fillColor = Color.rgba(color.integer());
        NanoVG.nvgFillColor(contextPtr, color.nvg());
    }

    public void drawColor(boolean fill, Color color) {
        if (fill) {
            fillColor(color);
        } else {
            strokeColor(color);
        }
    }

    public Color fillColor() {
        return S_fillColor;
    }

    public Color strokeColor() {
        return S_strokeColor;
    }

    public float strokeWidth() {
        return S_strokeWidth;
    }
}