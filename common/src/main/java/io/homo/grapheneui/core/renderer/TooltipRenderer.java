package io.homo.grapheneui.core.renderer;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.animator.Easing;
import io.homo.grapheneui.animator.NumberAnimator;
import io.homo.grapheneui.animator.PosAnimator;
import io.homo.grapheneui.core.Transform;
import io.homo.grapheneui.core.impl.TooltipHolder;
import io.homo.grapheneui.impl.Vec2;
import io.homo.grapheneui.nanovg.NanoVG;
import io.homo.grapheneui.nanovg.NanoVGFont;
import io.homo.grapheneui.nanovg.renderer.NanoVGTextRenderer;
import io.homo.grapheneui.nanovg.renderer.TextAlign;

import java.util.Optional;

public class TooltipRenderer extends HighlightRenderer {
    public static final int AUTO = 0x8000;
    public static final int TOP = 0x8001;
    public static final int BOTTOM = 0x8002;
    public static final int LEFT = 0x8003;
    public static final int RIGHT = 0x8004;
    private final PosAnimator posAnimator = new PosAnimator(70);
    private final NumberAnimator alphaAnimator = NumberAnimator.create();
    private final NumberAnimator widthAnimator = NumberAnimator.create();
    protected int tooltipPos = TooltipRenderer.AUTO;
    protected boolean show = false;
    protected float radius = GrapheneUI.CONST.TOOLTIP_ROUNDED_SIZE;
    protected float fontSize = 15f;
    protected NanoVGFont font = GrapheneUI.regularFont();
    private Vec2 size;
    private TooltipHolder tooltipHolder;
    private boolean isHiding;
    private Vec2 pos = new Vec2(0);

    public TooltipRenderer(TooltipHolder tooltipHolder, Vec2 size) {
        this.tooltipHolder = tooltipHolder;
        this.size = size;
    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public NanoVGFont getFont() {
        return font;
    }

    public void setFont(NanoVGFont font) {
        this.font = font;
    }

    public void setPos(Vec2 pos) {
        this.pos = pos;
        float width = getWidth();
        float height = getHeight();
        float screenWidth = NanoVG.getScreenWidth();
        float screenHeight = NanoVG.getScreenHeight();
        Vec2 position = calculatePosition((int) pos.x, (int) pos.y, width, height, tooltipPos, screenWidth, screenHeight);
        float baseX = position.x();
        float baseY = position.y();
        baseX = Math.max(8, Math.min(baseX, screenWidth - width - 8));
        baseY = Math.max(8, Math.min(baseY, screenHeight - height - 8));
        posAnimator.update((int) baseX, (int) baseY);
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public Vec2 getSize() {
        return size;
    }

    public void setSize(Vec2 size) {
        this.size = size;
    }

    public int getTooltipPos() {
        return tooltipPos;
    }

    public TooltipRenderer setTooltipPos(int tooltipPos) {
        this.tooltipPos = tooltipPos;
        return this;
    }

    public TooltipHolder getTooltipHolder() {
        return tooltipHolder;
    }

    public void setTooltipHolder(TooltipHolder tooltipHolder) {
        this.tooltipHolder = tooltipHolder;
    }

    private Vec2 calculatePosition(int targetX, int targetY,
                                   float width, float height,
                                   int pos, float screenWidth, float screenHeight) {
        final float offset = 12f;
        switch (pos) {
            case AUTO:
                if (targetY - height - offset > 0) {
                    return calculatePosition(targetX, targetY, width, height, TOP, screenWidth, screenHeight);
                } else if (targetY + offset + height < screenHeight) {
                    return calculatePosition(targetX, targetY, width, height, BOTTOM, screenWidth, screenHeight);
                } else if (targetX - width - offset > 0) {
                    return calculatePosition(targetX, targetY, width, height, LEFT, screenWidth, screenHeight);
                } else {
                    return calculatePosition(targetX, targetY, width, height, RIGHT, screenWidth, screenHeight);
                }

            case TOP:
                return new Vec2(
                        targetX - (width * 0.5f) + offset,
                        targetY - height - offset
                );

            case BOTTOM:
                return new Vec2(
                        targetX - (width * 0.5f) + offset,
                        targetY + offset
                );

            case LEFT:
                return new Vec2(
                        targetX - width - offset,
                        targetY - height - offset
                );

            case RIGHT:
                return new Vec2(
                        targetX + offset,
                        targetY - height - offset
                );

            default:
                return new Vec2(
                        targetX + offset,
                        targetY + offset
                );
        }
    }

    public boolean shouldRender() {
        float currentAlpha = alphaAnimator.getFloat();
        float currentWidth = widthAnimator.getFloat();
        return currentAlpha > 0.01 || currentWidth > 0;
    }

    public void renderTooltip(float delta) {
        nvg.save();
        alphaAnimator.update();
        widthAnimator.update();
        int x = (int) pos.x;
        int y = (int) pos.y;
        this.updateHighlight(delta);
        Optional<String> optional = tooltipHolder.getTooltip();
        if (optional.isEmpty()) return;
        String tooltip = optional.get();
        float currentAlpha = alphaAnimator.getFloat();
        float currentWidth = widthAnimator.getFloat();
        float width = getWidth();
        float height = getHeight();
        float textWidth = getTextWidth();
        float textHeight = getTextHeight();
        float screenWidth = NanoVG.getScreenWidth();
        float screenHeight = NanoVG.getScreenHeight();
        Vec2 position = calculatePosition(x, y, width, height, tooltipPos, screenWidth, screenHeight);
        float baseX = position.x();
        float baseY = position.y();
        baseX = Math.max(8, Math.min(baseX, screenWidth - width - 8));
        baseY = Math.max(8, Math.min(baseY, screenHeight - height - 8));
        //posAnimator.update((int) baseX, (int) baseY, delta);
        //baseX = posAnimator.x();
        //baseY = posAnimator.y();
        if (shouldRender()) {
            nvg.save();
            nvg.globalAlpha(Math.min(currentAlpha * 1.2f, 1.0f));
            nvg.transform(
                    Transform.identity()
                            .position(baseX, baseY)
            );
            nvg.drawRoundedRect(
                    0,
                    0,
                    Math.max(width * currentWidth, 20),
                    height,
                    radius,
                    GrapheneUI.THEME.TOOLTIP_BG,
                    true
            );
            this.renderHighlight(
                    0,
                    0,
                    Math.max(width * currentWidth, 20),
                    height,
                    radius
            );
            nvg.restore();
            nvg.save();
            nvg.globalAlpha(currentAlpha);
            nvg.scissor(
                    baseX,
                    baseY,
                    Math.max(width * currentWidth, 20),
                    height
            );
            nvg.transform(null);
            NanoVG.RENDERER.TEXT.drawAlignedText(
                    font,
                    fontSize,
                    tooltip,
                    baseX + width / 2 - textWidth / 2,
                    baseY + height / 2 - textHeight / 2 + 1,
                    textWidth,
                    fontSize + 2,
                    GrapheneUI.THEME.TEXT_A.nvg(),
                    TextAlign.of(
                            TextAlign.ALIGN_LEFT,
                            TextAlign.ALIGN_TOP
                    ),
                    false
            );
            nvg.resetScissor();
            nvg.restore();
        }
        nvg.restore();
    }

    public NanoVGTextRenderer.TextMetrics getTooltipTextMetrics() {
        return NanoVGTextRenderer.INSTANCE.calculateTextMetrics(
                font,
                fontSize,
                tooltipHolder.getTooltip().orElse(""),
                (size.x == -1 ? 9999999 : size.x) - (GrapheneUI.CONST.TOOLTIP_BORDER * 2),
                fontSize + 2,
                false
        );
    }

    public float getTextHeight() {
        NanoVGTextRenderer.TextMetrics metrics = getTooltipTextMetrics();
        return metrics.totalHeight;
    }

    public float getTextWidth() {
        return size.x == -1 ? getTooltipTextMetrics().maxLineWidth : size.x - (GrapheneUI.CONST.TOOLTIP_BORDER * 2);
    }

    public float getHeight() {
        return getTextHeight() + (GrapheneUI.CONST.TOOLTIP_BORDER * 2);
    }

    public float getWidth() {
        return getTextWidth() + (GrapheneUI.CONST.TOOLTIP_BORDER * 2);
    }

    public void showTooltip() {
        if (this.show) return;
        this.show = true;
        this.isHiding = false;
        updateAnimatorTarget();
        flash();
    }

    public void hideTooltip() {
        if (!this.show) return;
        this.show = false;
        this.isHiding = true;

        updateAnimatorTarget();
    }

    protected void updateAnimatorTarget() {
        if (show) {
            widthAnimator
                    .animateTo(1, 250)
                    .ease(Easing.EASE_OUT_QUINT);
            alphaAnimator.animateTo(1.0, 250)
                    .ease(Easing.EASE_OUT_CUBIC);
        } else if (isHiding) {
            alphaAnimator.animateTo(0.0, 200)
                    .ease(Easing.EASE_IN_QUAD)
                    .onComplete(() -> {
                        if (!show) widthAnimator.set(0);
                    });
        }
    }

    public void reset() {
        alphaAnimator.set(0.0);
        widthAnimator.set(0.0);
        isHiding = false;
        show = false;
    }
}