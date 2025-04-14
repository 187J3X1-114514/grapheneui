package io.homo.grapheneui.nanovg;

public class NanoVGRendererBase {
    protected final NanoVGContext nvg = NanoVG.getContext();
    protected final long contextPtr = NanoVG.getContext().contextPtr;
}
