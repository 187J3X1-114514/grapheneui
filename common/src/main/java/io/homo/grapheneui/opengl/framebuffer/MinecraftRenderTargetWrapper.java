package io.homo.grapheneui.opengl.framebuffer;

import com.mojang.blaze3d.pipeline.RenderTarget;
import io.homo.grapheneui.opengl.texture.GlTexture;
import io.homo.grapheneui.opengl.texture.TextureFormat;
import io.homo.grapheneui.utils.ColorUtil;
import net.minecraft.client.Minecraft;

import static org.lwjgl.opengl.GL30.glBindFramebuffer;

public class MinecraftRenderTargetWrapper extends GlFrameBuffer {
    public RenderTarget renderTarget;
    private int clearColor = ColorUtil.color(255, 0, 0, 0);

    MinecraftRenderTargetWrapper(RenderTarget renderTarget) {
        this.renderTarget = renderTarget;
    }

    public static MinecraftRenderTargetWrapper of(RenderTarget renderTarget) {
        if (renderTarget == null) return null;
        return new MinecraftRenderTargetWrapper(renderTarget);
    }

    public void clearFrameBuffer() {
        #if MC_VER < MC_1_21_4
        this.renderTarget.clear(Minecraft.ON_OSX);
        #elif MC_VER > MC_1_21_4
        com.mojang.blaze3d.systems.RenderSystem.getDevice().createCommandEncoder().clearColorAndDepthTextures(
                java.util.Objects.requireNonNull(renderTarget.getColorTexture()),
                clearColor,
                java.util.Objects.requireNonNull(renderTarget.getDepthTexture()),
                0.0f
        );
        #else
        this.renderTarget.clear();
        #endif
    }

    public void resizeFrameBuffer(int width, int height) {
        #if MC_VER < MC_1_21_4
        this.renderTarget.resize(width, height, Minecraft.ON_OSX);
        #else
        this.renderTarget.resize(width, height);
        #endif
    }

    @Override
    public int getWidth() {
        return renderTarget.width;
    }

    @Override
    public int getHeight() {
        return renderTarget.height;
    }

    @Override
    public void destroy() {
        renderTarget.destroyBuffers();
    }

    public void bind(BindPoint bindPoint, boolean setViewport) {
        #if MC_VER > MC_1_21_4
        glBindFramebuffer(GlFrameBuffer.resolveBindTarget(bindPoint), MinecraftRenderTargetUtil.getFboId(renderTarget));
        #else
        if (bindPoint == BindPoint.READ) {
            renderTarget.bindRead();
        } else {
            renderTarget.bindWrite(setViewport);
        }
        #endif

    }

    public void bind(BindPoint bindPoint) {
        bind(bindPoint, true);
    }

    public void unbind(BindPoint bindPoint) {
        glBindFramebuffer(GlFrameBuffer.resolveBindTarget(bindPoint), 0);
    }

    @Override
    public int getTextureId(io.homo.grapheneui.opengl.framebuffer.FrameBufferAttachmentType attachmentType) {
        #if MC_VER > MC_1_21_4
        return switch (attachmentType) {
            case COLOR -> MinecraftRenderTargetUtil.getColorTexId(renderTarget);
            case DEPTH, DEPTH_STENCIL -> MinecraftRenderTargetUtil.getDepthTexId(renderTarget);
        };
        #else
        return switch (attachmentType) {
            case COLOR -> renderTarget.getColorTextureId();
            case DEPTH, DEPTH_STENCIL -> renderTarget.getDepthTextureId();
        };
        #endif

    }

    @Override
    public GlTexture getTexture(FrameBufferAttachmentType attachmentType) {
        return null;
    }

    @Override
    public int getFrameBufferId() {
        #if MC_VER > MC_1_21_4
        return MinecraftRenderTargetUtil.getFboId(renderTarget);
        #else
        return renderTarget.frameBufferId;
        #endif
    }

    @Override
    public void setClearColor(float red, float green, float blue, float alpha) {
        #if MC_VER > MC_1_21_4
        clearColor = ColorUtil.color((int) (alpha * 255), (int) (red * 255), (int) (green * 255), (int) (blue * 255));
        #else
        renderTarget.setClearColor(red, green, blue, alpha);
        #endif

    }

    @Override
    public TextureFormat getColorTextureFormat() {
        return TextureFormat.RGBA8;
    }

    @Override
    public TextureFormat getDepthTextureFormat() {
        return TextureFormat.DEPTH24;
    }
}
