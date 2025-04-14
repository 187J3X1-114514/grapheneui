package io.homo.grapheneui.opengl.texture;

import com.mojang.blaze3d.systems.RenderSystem;
import io.homo.grapheneui.opengl.GlState;

import static io.homo.grapheneui.opengl.GlUtils.glSafeObjectLabel;
import static org.lwjgl.opengl.GL43.*;

public class GlTexture {
    public int id;
    public int format;
    public int width;
    public int height;
    private boolean mipmap = false;

    public GlTexture(int width, int height, int format, boolean mipmap) {
        this.id = glGenTextures();
        this.format = format;
        this.width = width;
        this.height = height;
        this.mipmap = mipmap;
        initializeTexture();
    }

    public static GlTexture create(int width, int height, TextureFormat format) {
        return create(width, height, format, false);
    }

    public static GlTexture create(int width, int height, TextureFormat format, boolean mipmap) {
        return new GlTexture(width, height, format.gl(), mipmap);
    }

    private void validateTextureParameters() {
        if (width <= 0 || height <= 0) {
            throw new IllegalArgumentException("Invalid texture dimensions: "
                    + width + "x" + height);
        }
        int[] maxSize = new int[1];
        glGetIntegerv(GL_MAX_TEXTURE_SIZE, maxSize);
        if (width > maxSize[0] || height > maxSize[0]) {
            throw new IllegalArgumentException("Texture size exceeds maximum supported size: "
                    + maxSize[0] + " (requested " + width + "x" + height + ")");
        }
    }

    private void initializeTexture() {
        validateTextureParameters();
        glBindTexture(GL_TEXTURE_2D, this.id);
        int minFilter = mipmap ? GL_LINEAR_MIPMAP_LINEAR : GL_NEAREST;
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, minFilter);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_BASE_LEVEL, 0);
        int maxLevel = mipmap ? 8 : 0;
        if (mipmap) glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, maxLevel);
        glTexStorage2D(GL_TEXTURE_2D, maxLevel + 1, this.format, this.width, this.height);
        glBindTexture(GL_TEXTURE_2D, 0);
        updateDebugLabel(getDebugLabel());
    }

    public void destroy() {
        RenderSystem.assertOnRenderThread();
        glDeleteTextures(this.id);
    }

    public void resize(int width, int height) {
        RenderSystem.assertOnRenderThread();
        glDeleteTextures(this.id);
        this.id = glGenTextures();
        if (width < 1 || height < 1) throw new RuntimeException();
        this.width = width;
        this.height = height;
        initializeTexture();
    }

    public void copyFromFBO(int srcFbo) {
        RenderSystem.assertOnRenderThread();
        glBindFramebuffer(GL_FRAMEBUFFER, srcFbo);
        glBindTexture(GL_TEXTURE_2D, this.id);
        glCopyTexSubImage2D(GL_TEXTURE_2D, 0, 0, 0, 0, 0, width, height);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    public void copyFromTex(int srcTex) {
        glCopyImageSubData(srcTex, GL_TEXTURE_2D, 0, 0, 0, 0,
                this.id, GL_TEXTURE_2D, 0, 0, 0, 0,
                width, height, 1);
    }


    public int getTextureId() {
        return id;
    }


    public TextureFormat getTextureFormat() {
        return TextureFormat.fromGl(format);
    }


    public int getWidth() {
        return width;
    }


    public int getHeight() {
        return height;
    }


    public String getDebugLabel() {
        return "Texture{" +
                "id=" + getTextureId() +
                "format=" + getTextureFormat() +
                "width=" + getWidth() +
                "height=" + getHeight() +
                '}';
    }


    public void updateDebugLabel(String newLabel) {
        glSafeObjectLabel(GL_TEXTURE, getTextureId(), getDebugLabel());
    }

    public void generateMipmap() {
        try (GlState ignored = new GlState()) {
            glBindTexture(GL_TEXTURE_2D, this.id);
            glGenerateMipmap(GL_TEXTURE_2D);
        }
    }
}