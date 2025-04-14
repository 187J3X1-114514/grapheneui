package io.homo.grapheneui.opengl.framebuffer;


import com.mojang.blaze3d.pipeline.RenderTarget;

public class FrameBufferRenderTargetAdapter extends RenderTarget {
    private GlFrameBuffer frameBuffer;

    FrameBufferRenderTargetAdapter(GlFrameBuffer frameBuffer) {
        super(frameBuffer.getDepthTextureFormat() != null);
        this.frameBuffer = frameBuffer;
        updateState();
    }

    protected static FrameBufferRenderTargetAdapter ofRenderTarget(GlFrameBuffer frameBuffer) {
        return new FrameBufferRenderTargetAdapter(frameBuffer);
    }

    public FrameBufferRenderTargetAdapter bindFrameBuffer(GlFrameBuffer frameBuffer) {
        this.frameBuffer = frameBuffer;
        return this;
    }

    private void updateState() {
        this.width = frameBuffer.getWidth();
        this.height = frameBuffer.getHeight();
        this.viewWidth = frameBuffer.getWidth();
        this.viewHeight = frameBuffer.getHeight();
        this.frameBufferId = frameBuffer.getFrameBufferId();
        this.colorTextureId = frameBuffer.getTextureId(FrameBufferAttachmentType.COLOR);
        this.depthBufferId = frameBuffer.getTextureId(FrameBufferAttachmentType.DEPTH_STENCIL) == -1 ? frameBuffer.getTextureId(FrameBufferAttachmentType.DEPTH) : frameBuffer.getTextureId(FrameBufferAttachmentType.DEPTH_STENCIL);
    }


    public void bindRead() {
        updateState();
        frameBuffer.bind(BindPoint.READ);
    }

    public void unbindRead() {
        updateState();
        frameBuffer.unbind(BindPoint.READ);

    }

    public void bindWrite(boolean setViewport) {
        updateState();
        frameBuffer.bind(BindPoint.WRITE, setViewport);

    }

    public void unbindWrite() {
        updateState();
        frameBuffer.unbind(BindPoint.WRITE);
    }

    public void setClearColor(float red, float green, float blue, float alpha) {
        updateState();
        frameBuffer.setClearColor(red, green, blue, alpha);
    }

    public void blitToScreen(int width, int height) {
        updateState();
    }

    public void blitAndBlendToScreen(int width, int height) {
        updateState();
        blitToScreen(width, height);
    }

    #if MC_VER < MC_1_21_4
    public void clear(boolean a) {
        updateState();
        frameBuffer.clearFrameBuffer();
    }

    public void resize(int width, int height, boolean clearError) {
    }

    public void createBuffers(int width, int height, boolean clearError) {
        updateState();
    }
    #else
    public void clear() {
        updateState();
        frameBuffer.clearFrameBuffer();
    }
    public void resize(int width, int height) {
    }
    public void createBuffers(int width, int height) {
        updateState();
    }
    #endif

    public int getColorTextureId() {
        updateState();
        return frameBuffer.getTextureId(FrameBufferAttachmentType.COLOR);
    }

    public int getDepthTextureId() {
        updateState();
        return frameBuffer.getTextureId(FrameBufferAttachmentType.DEPTH_STENCIL) == -1 ? frameBuffer.getTextureId(FrameBufferAttachmentType.DEPTH) : frameBuffer.getTextureId(FrameBufferAttachmentType.DEPTH_STENCIL);
    }

    public void destroyBuffers() {
        updateState();
    }

    public void copyDepthFrom(RenderTarget otherTarget) {
        updateState();
        return;
    }

}