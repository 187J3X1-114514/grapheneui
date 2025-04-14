package io.homo.grapheneui.opengl.framebuffer;

import io.homo.grapheneui.opengl.texture.GlTexture;

public class FrameBufferAttachment {
    public FrameBufferAttachmentType type;
    public GlTexture texture;

    public FrameBufferAttachment(FrameBufferAttachmentType type, GlTexture texture) {
        this.type = type;
        this.texture = texture;
    }

}
