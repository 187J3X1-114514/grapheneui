package io.homo.grapheneui.opengl.texture;

import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.lwjgl.opengl.GL30.*;

public enum TextureFormat {
    RGBA8(GL_RGBA8),
    RGBA16F(GL_RGBA16F),
    RGB8(GL_RGB8),
    RG16F(GL_RG16F),
    R32F(GL_R32F),
    R32UI(GL_R32UI),
    DEPTH32F(GL_DEPTH_COMPONENT32F),
    DEPTH24_STENCIL8(GL_DEPTH24_STENCIL8),
    DEPTH24(GL_DEPTH_COMPONENT24);


    private static final Map<Integer, TextureFormat> GL_TO_FORMAT;

    static {
        Map<Integer, TextureFormat> glMap = new HashMap<>();

        for (TextureFormat format : values()) {
            glMap.put(format.glFormat, format);
        }

        GL_TO_FORMAT = Collections.unmodifiableMap(glMap);
    }

    private final int glFormat;

    TextureFormat(int glFormat) {
        this.glFormat = glFormat;
    }

    public static @NotNull TextureFormat fromGl(int format) {
        TextureFormat result = GL_TO_FORMAT.get(format);
        if (result == null) {
            throw new IllegalArgumentException("Unsupported OpenGL format: 0x" +
                    Integer.toHexString(format).toUpperCase());
        }
        return result;
    }

    public boolean isStencil() {
        return this == DEPTH24_STENCIL8;
    }

    public int gl() {
        return glFormat;
    }
}