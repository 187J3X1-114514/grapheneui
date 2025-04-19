package io.homo.grapheneui.gui.widgets;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.core.renderer.TooltipRenderer;
import io.homo.grapheneui.impl.Vec2;
import io.homo.grapheneui.nanovg.NanoVGFont;

import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractWidgetBuilder<W, B> {
    protected Supplier<Optional<String>> textSupplier;
    protected String tooltip;
    protected int tooltipPos = TooltipRenderer.AUTO;
    protected Supplier<Optional<String>> tooltipSupplier;
    protected float tooltipRadius = GrapheneUI.CONST.TOOLTIP_ROUNDED_SIZE;
    protected float tooltipFontSize = 15f;
    protected NanoVGFont tooltipFont = GrapheneUI.regularFont();
    protected Vec2 tooltipSize = new Vec2(-1, -1);

    @SuppressWarnings("unchecked")
    public B withTextSupplier(Supplier<Optional<String>> textSupplier) {
        this.textSupplier = textSupplier;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B withTooltip(String tooltip) {
        this.tooltip = tooltip;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B withTooltipSupplier(Supplier<Optional<String>> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B withTooltipPosition(int tooltipPos) {
        this.tooltipPos = tooltipPos;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B withTooltipRadius(float tooltipRadius) {
        this.tooltipRadius = tooltipRadius;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B withTooltipFontSize(float tooltipFontSize) {
        this.tooltipFontSize = tooltipFontSize;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B withTooltipFont(NanoVGFont tooltipFont) {
        this.tooltipFont = tooltipFont;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B withTooltipSize(Vec2 tooltipSize) {
        this.tooltipSize = tooltipSize;
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B withTooltipSize(float width, float height) {
        this.tooltipSize = new Vec2(width, height);
        return (B) this;
    }

    protected void configureWidget(AbstractWidget<?> widget) {
        if (tooltip != null) {
            widget.setTooltip(tooltip);
        } else if (tooltipSupplier != null) {
            widget.setTooltipSupplier(tooltipSupplier);
        }
        widget.setTooltipFontSize(tooltipFontSize);
        widget.setTooltipFont(tooltipFont);
        widget.setTooltipRadius(tooltipRadius);
        widget.getTooltipRenderer().setSize(tooltipSize);
        widget.setTooltipPos(tooltipPos);
    }

    public abstract W build();
}