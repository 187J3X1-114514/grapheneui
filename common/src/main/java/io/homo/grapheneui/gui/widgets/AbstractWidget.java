package io.homo.grapheneui.gui.widgets;

import io.homo.grapheneui.core.impl.EventHandle;
import io.homo.grapheneui.core.impl.EventListener;
import io.homo.grapheneui.core.impl.TooltipHolder;
import io.homo.grapheneui.core.renderer.TooltipRenderer;
import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.impl.Renderable;
import io.homo.grapheneui.impl.Vec2;
import io.homo.grapheneui.nanovg.NanoVG;
import io.homo.grapheneui.nanovg.NanoVGContext;
import io.homo.grapheneui.nanovg.NanoVGFont;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class AbstractWidget<T> extends TooltipHolder implements EventListener, Renderable, EventHandle<T> {
    protected final NanoVGContext nvg = NanoVG.context;
    protected final long contextPtr = NanoVG.context.contextPtr;
    public boolean active = true;
    public boolean visible = true;
    protected boolean hovered;
    protected boolean pressed;
    protected boolean focused;
    protected int tooltipPos = TooltipRenderer.AUTO;
    protected Map<String, ArrayList<Consumer<T>>> eventListenerMap = new HashMap<>();
    protected TooltipRenderer tooltipRenderer = new TooltipRenderer(this, new Vec2(-1, -1));

    public AbstractWidget() {
        eventListenerMap.put("focus", new ArrayList<>());
        eventListenerMap.put("hover", new ArrayList<>());
        eventListenerMap.put("press", new ArrayList<>());
        eventListenerMap.put("release", new ArrayList<>());
    }

    public float getTooltipFontSize() {
        return tooltipRenderer.getFontSize();
    }

    public void setTooltipFontSize(float fontSize) {
        tooltipRenderer.setFontSize(fontSize);
    }

    public NanoVGFont getTooltipFont() {
        return tooltipRenderer.getFont();
    }

    public void setTooltipFont(NanoVGFont font) {
        tooltipRenderer.setFont(font);
    }

    public float getTooltipRadius() {
        return tooltipRenderer.getRadius();
    }

    public void setTooltipRadius(float radius) {
        tooltipRenderer.setRadius(radius);
    }

    public void renderTooltip(float delta) {
        tooltipRenderer.renderTooltip(delta);
    }

    @Override
    public void mouseMove(float x, float y) {
        tooltipRenderer.setPos(new Vec2(x, y));
    }

    public TooltipRenderer getTooltipRenderer() {
        return tooltipRenderer;
    }

    public void setTooltipPos(int tooltipPos) {
        this.tooltipPos = tooltipPos;
        tooltipRenderer.setTooltipPos(tooltipPos);
    }

    public boolean isPressed() {
        return pressed;
    }

    public void setPressed(boolean pressed) {
        this.pressed = pressed;
        invokeEventListener(pressed ? "press" : "release");
    }

    @Override
    public void removeEventListener(String type, Consumer<T> consumer) {
        if (eventListenerMap.get(type) != null) eventListenerMap.get(type).remove(consumer);
    }

    @Override
    public void addEventListener(String type, Consumer<T> consumer) {
        if (eventListenerMap.get(type) != null) eventListenerMap.get(type).add(consumer);
    }

    protected void invokeEventListener(String type) {
        if (eventListenerMap.get(type) != null)
            for (Consumer<T> consumer : eventListenerMap.get(type)) consumer.accept((T) this);
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        if (active == this.active) return;
        this.active = active;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        if (visible == this.visible) return;

        this.visible = visible;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        if (hovered == this.hovered) return;
        this.hovered = hovered;
        if (isVisible()) {
            if (hovered) {
                tooltipRenderer.showTooltip();
            } else {
                tooltipRenderer.hideTooltip();
            }
        }
        invokeEventListener("hover");
    }

    public boolean isFocused() {
        return focused;
    }

    public void setFocused(boolean focused) {
        if (focused == this.focused) return;

        this.focused = focused;
        invokeEventListener("focus");
    }

    public abstract Rectangle getRectangle();
}
