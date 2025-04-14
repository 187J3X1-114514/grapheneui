package io.homo.grapheneui.gui.widgets.sliders;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.animator.Easing;
import io.homo.grapheneui.animator.NumberAnimator;
import io.homo.grapheneui.gui.widgets.AbstractWidget;
import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.utils.MouseCursor;

import java.util.ArrayList;

public class Slider extends AbstractWidget<Slider> {
    private final NumberAnimator valueAnimator = NumberAnimator.create();
    private final NumberAnimator sliderWidthAnimator = NumberAnimator.create();
    public Rectangle rectangle;
    protected float step = 1;
    protected float max = 1;
    protected float min = 1;
    protected float value = 1;
    private boolean dragging = false;


    public Slider(Rectangle rectangle, float step, float max, float min, float value) {
        this.rectangle = rectangle;
        this.step = step;
        this.max = max;
        this.min = min;
        this.value = value;
        valueAnimator.set((Math.clamp(value, min, max) - min) / (max - min));
        this.eventListenerMap.put("change", new ArrayList<>());
    }

    public float getStep() {
        return step;
    }

    public void setStep(float step) {
        this.step = step;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = Math.clamp(value, min, max);
    }

    @Override
    public void render(float mouseX, float mouseY, float delta) {
        nvg.save();
        valueAnimator.update();
        sliderWidthAnimator.update();
        float progress = valueAnimator.getFloat();
        float trackHeight = rectangle.height * 0.75f;
        float srcSliderWidth = 8;
        float sliderWidth = srcSliderWidth * (1 - sliderWidthAnimator.getFloat() * 0.4f);
        float sliderHeight = trackHeight * 1.25f;
        float sliderMargin = 2;
        float maxLeftWidth = rectangle.width - sliderMargin * 2 - sliderWidth;
        float leftWidth = Math.min(
                Math.max(rectangle.width * progress - sliderMargin, 0),
                maxLeftWidth
        );

        float rightStartX = rectangle.x + leftWidth + sliderMargin * 2 + srcSliderWidth;
        float rightWidth = Math.max(rectangle.width - leftWidth - sliderMargin * 2 - srcSliderWidth, 0);

        nvg.beginPath();
        if (leftWidth >= 1) {
            nvg.roundedRect(
                    rectangle.x,
                    rectangle.y + (rectangle.height / 2f) - trackHeight / 2f,
                    leftWidth,
                    trackHeight,
                    3.5f
            );
        }

        float thumbX = Math.min(
                rectangle.x + leftWidth + sliderMargin,
                rectangle.x + rectangle.width - sliderWidth - sliderMargin
        );
        nvg.roundedRect(
                thumbX - sliderWidth / 2 + sliderMargin * 2,
                rectangle.y,
                sliderWidth,
                sliderHeight,
                2
        );

        nvg.linearGradient(
                rectangle.x - 50,
                rectangle.y + 50,
                rectangle.getLimitX(),
                rectangle.getLimitY(),
                GrapheneUI.THEME.THEME_TRANSITION_B,
                GrapheneUI.THEME.THEME_TRANSITION_A
        );
        nvg.endPath(true);

        nvg.beginPath();
        nvg.fillColor(GrapheneUI.THEME.INTERFACE_BG_A);
        if (rightWidth >= 2) {
            nvg.roundedRect(
                    rightStartX,
                    rectangle.y + (rectangle.height / 2f) - trackHeight / 2f,
                    rightWidth,
                    trackHeight,
                    3.5f
            );
        }
        nvg.endPath(true);
        nvg.restore();
    }

    @Override
    public void mousePress(float x, float y, int button) {
        super.mousePress(x, y, button);
        if (rectangle.in(x, y)) {
            dragging = true;
            updateValue(x, true);
            sliderWidthAnimator
                    .animateTo(1, 80)
                    .ease(Easing.LINEAR);
        }
    }

    @Override
    public void mouseDrag(float mouseX, float mouseY, float dragX, float dragY, int button) {
        super.mouseDrag(mouseX, mouseY, dragX, dragY, button);
        if (dragging) {
            updateValue(mouseX, false);
        }
    }

    @Override
    public void mouseRelease(float x, float y, int button) {
        super.mouseRelease(x, y, button);
        sliderWidthAnimator
                .animateTo(0, 80)
                .ease(Easing.LINEAR);
        dragging = false;
    }

    @Override
    public void mouseMove(float x, float y) {
        super.mouseMove(x, y);
        if (rectangle.in(x, y)) {
            if (!hovered) {
                MouseCursor.HAND.use();
            }
            setHovered(true);
        } else {
            if (hovered) {
                MouseCursor.ARROW.use();
            }
            setHovered(false);
        }
    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    private void updateValue(float mouseX, boolean smooth) {
        float relativeX = mouseX - rectangle.x;
        float progress = relativeX / rectangle.width;
        float newValue = min + progress * (max - min);
        if (step > 0) {
            newValue = newValue - (newValue % step);
        }
        setValue(newValue);
        invokeEventListener("change");
        if (smooth) {
            valueAnimator.animateTo((Math.clamp(value, min, max) - min) / (max - min), 100)
                    .ease(Easing.EASE_OUT_QUINT);
        } else {
            valueAnimator.animateTo((Math.clamp(value, min, max) - min) / (max - min), 1).ease(Easing.EASE_OUT_QUINT);
        }

    }
}