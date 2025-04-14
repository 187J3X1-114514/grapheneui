package io.homo.grapheneui.core.renderer;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.utils.Color;

public class BasePanelRenderer extends HighlightRenderer {
    protected Color panelColor;
    protected Color shadowColor;
    protected PanelVariant variant;
    protected boolean clickable;
    protected boolean shadow;
    protected float radius = 5;


    public BasePanelRenderer(Color panelColor, Color shadowColor, PanelVariant variant) {
        this.panelColor = panelColor;
        this.shadowColor = shadowColor;
        this.variant = variant;
        this.highlightAlpha = 40;
        this.hoverAlpha = 20;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public BasePanelRenderer panelColor(Color panelColor) {
        this.panelColor = panelColor;
        return this;
    }

    public BasePanelRenderer shadowColor(Color shadowColor) {
        this.shadowColor = shadowColor;
        return this;
    }

    public BasePanelRenderer variant(PanelVariant variant) {
        this.variant = variant;
        return this;
    }

    public BasePanelRenderer clickable(boolean clickable) {
        this.clickable = clickable;
        return this;
    }

    public BasePanelRenderer shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public void renderPanel(float width, float height, float delta) {
        nvg.save();
        if (shadow) {
            nvg.drawRoundedRect(
                    0,
                    0,
                    width,
                    height,
                    radius,
                    shadowColor,
                    true
            );
            nvg.drawRoundedRect(
                    0,
                    0,
                    width,
                    height - GrapheneUI.CONST.SHADOW_HEIGHT,
                    radius,
                    panelColor,
                    true
            );
            this.renderHighlight(
                    0,
                    0,
                    width,
                    height - GrapheneUI.CONST.SHADOW_HEIGHT,
                    radius
            );
        } else {
            nvg.drawRoundedRect(
                    0,
                    0,
                    width,
                    height,
                    radius,
                    panelColor,
                    true
            );
            this.renderHighlight(
                    0,
                    0,
                    width,
                    height - GrapheneUI.CONST.SHADOW_HEIGHT,
                    radius
            );
        }
        nvg.restore();
    }

    public enum PanelVariant {
        FILLED, OUTLINED
    }
}
