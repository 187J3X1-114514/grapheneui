package io.homo.grapheneui.opengl.framebuffer;

import static org.lwjgl.opengl.GL30.*;

public enum FrameBufferAttachmentType {
    COLOR(GL_COLOR_ATTACHMENT0),
    DEPTH(GL_DEPTH_ATTACHMENT),
    DEPTH_STENCIL(GL_DEPTH_STENCIL_ATTACHMENT);
    private final int srcAttachmentId;
    private int attachmentId;

    FrameBufferAttachmentType(int attachmentId) {
        this.attachmentId = attachmentId;
        this.srcAttachmentId = attachmentId;
    }

    public FrameBufferAttachmentType index(int index) {
        if (this.srcAttachmentId != GL_COLOR_ATTACHMENT0) {
            throw new RuntimeException();
        }
        this.attachmentId = srcAttachmentId + index;
        return this;
    }

    public int attachmentId() {
        return attachmentId;
    }
}
