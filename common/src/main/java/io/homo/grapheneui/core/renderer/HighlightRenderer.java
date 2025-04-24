package io.homo.grapheneui.core.renderer;

import io.homo.grapheneui.animator.Easing;
import io.homo.grapheneui.animator.NumberAnimator;
import io.homo.grapheneui.nanovg.NanoVGRendererBase;
import io.homo.grapheneui.utils.Color;

public class HighlightRenderer extends NanoVGRendererBase {
    protected final NumberAnimator highlightAlphaAnimator = NumberAnimator.create();
    protected final NumberAnimator hoverAlphaAnimator = NumberAnimator.create();
    protected final NumberAnimator checkAlphaAnimator = NumberAnimator.create();

    protected int highlightAlpha = 127;
    protected int hoverAlpha = 30;
    protected int checkAlpha = 40;

    protected boolean hovered = false;
    protected boolean flashing = false;
    protected boolean checked = false;
    private boolean _checked = false;


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
        checkAlphaAnimator.update();
    }

    public void renderHighlight(float x, float y,
                                float width, float height,
                                float radius) {
        nvg.save();
        Color hoverColor = Color.rgba(255, 255, 255, (int) (
                        _checked ? checkAlpha * checkAlphaAnimator.getFloat() :
                                hoverAlpha * hoverAlphaAnimator.getFloat()
                )
        );
        if (radius == 0) {
            nvg.drawRect(
                    x,
                    y,
                    width,
                    height,
                    hoverColor,
                    true
            );
        } else {
            nvg.drawRoundedRect(
                    x,
                    y,
                    width,
                    height,
                    radius,
                    hoverColor,
                    true
            );
        }

        Color highlightColor = Color.rgba(255, 255, 255, (int) (highlightAlpha * highlightAlphaAnimator.getFloat()));
        if (radius == 0) {
            nvg.drawRect(
                    x,
                    y,
                    width,
                    height,
                    highlightColor,
                    true
            );
        } else {
            nvg.drawRoundedRect(
                    x,
                    y,
                    width,
                    height,
                    radius,
                    highlightColor,
                    true
            );
        }
        nvg.restore();
    }

    public void flash() {
        this.flashing = true;
        highlightAlphaAnimator
                .animateTo(1.0, 50)
                .ease(Easing.EASE_OUT_CUBIC)
                .onComplete(() ->
                        highlightAlphaAnimator
                                .animateTo(0.0, 160)
                                .ease(Easing.LINEAR)
                                .onComplete(() -> this.flashing = false));
    }

    public void setHover(boolean hover) {
        this.hovered = hover;
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

    public boolean isHovered() {
        return hovered;
    }

    public boolean isFlashing() {
        return flashing;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        if (checked != this.checked) {
            this.checked = checked;
            if (checked) {
                setHover(false);
                _checked = true;
                checkAlphaAnimator.set(0.0f);
                checkAlphaAnimator
                        .animateTo(1.0, 100)
                        .ease(Easing.LINEAR);
            } else {
                checkAlphaAnimator
                        .animateTo(0.0, 100)
                        .ease(Easing.LINEAR)
                        .onComplete(() -> {
                            _checked = false;
                        });
            }
        }

    }
}