package io.homo.grapheneui.core.renderer;

import io.homo.grapheneui.animator.Easing;
import io.homo.grapheneui.animator.NumberAnimator;
import io.homo.grapheneui.nanovg.NanoVGRendererBase;
import io.homo.grapheneui.utils.Color;

public class HighlightRenderer extends NanoVGRendererBase {
    protected final NumberAnimator highlightAlphaAnimator = NumberAnimator.create();
    protected final NumberAnimator hoverAlphaAnimator = NumberAnimator.create();
    protected int highlightAlpha = 127;
    protected int hoverAlpha = 30;

    public HighlightRenderer(int highlightAlpha, int hoverAlpha) {
        this.highlightAlpha = highlightAlpha;
        this.hoverAlpha = hoverAlpha;
    }

    public HighlightRenderer() {
    }

    public void setHighlightAlpha(int highlightAlpha) {
        this.highlightAlpha = highlightAlpha;
    }

    public void setHoverAlpha(int hoverAlpha) {
        this.hoverAlpha = hoverAlpha;
    }

    public void updateHighlight(float delta) {
        highlightAlphaAnimator.update();
        hoverAlphaAnimator.update();
    }

    public void renderHighlight(float x, float y,
                                float width, float height,
                                float radius) {
        nvg.save();

        Color color = Color.rgba(255, 255, 255, (int) (hoverAlpha * hoverAlphaAnimator.getFloat()));
        if (radius == 0) {
            nvg.drawRect(
                    x,
                    y,
                    width,
                    height,
                    color,
                    true
            );
        } else {
            nvg.drawRoundedRect(
                    x,
                    y,
                    width,
                    height,
                    radius,
                    color,
                    true
            );
        }

        color = Color.rgba(255, 255, 255, (int) (highlightAlpha * highlightAlphaAnimator.getFloat()));
        if (radius == 0) {
            nvg.drawRect(
                    x,
                    y,
                    width,
                    height,
                    color,
                    true
            );
        } else {
            nvg.drawRoundedRect(
                    x,
                    y,
                    width,
                    height,
                    radius,
                    color,
                    true
            );
        }
        nvg.restore();


    }

    public void flash() {
        highlightAlphaAnimator
                .animateTo(1.0, 70)
                .ease(Easing.EASE_OUT_CUBIC)
                .onComplete(() ->
                        highlightAlphaAnimator
                                .animateTo(0.0, 160)
                                .ease(Easing.LINEAR));
    }

    public void setHover(boolean hover) {
        if (hover) {
            hoverAlphaAnimator
                    .animateTo(1.0, 100)
                    .ease(Easing.LINEAR);
        } else {
            hoverAlphaAnimator
                    .animateTo(0.0, 100)
                    .ease(Easing.LINEAR);
        }
    }
}
