package io.homo.grapheneui.gui.widgets.buttons;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.animator.Easing;
import io.homo.grapheneui.animator.NumberAnimator;
import io.homo.grapheneui.core.Transform;
import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.impl.Vec2;
import io.homo.grapheneui.nanovg.NanoVG;
import io.homo.grapheneui.nanovg.renderer.TextAlign;
import io.homo.grapheneui.utils.Color;

import java.util.Optional;
import java.util.function.Supplier;


public class PressButton<T> extends AbstractButton<T> {
    protected final NumberAnimator pressAnimator = NumberAnimator.create();
    protected boolean zoom = true;

    public PressButton(Rectangle rectangle, Supplier<Optional<String>> textSupplier) {
        super(rectangle, textSupplier);
    }

    public PressButton(Rectangle rectangle) {
        super(rectangle);
    }

    public boolean isZoom() {
        return zoom;
    }

    public void setZoom(boolean zoom) {
        this.zoom = zoom;
    }

    public NumberAnimator getPressAnimator() {
        return pressAnimator;
    }

    @Override
    public void render(float mouseX, float mouseY, float delta) {
        panelRenderer.updateHighlight(delta);
        tooltipRenderer.updateHighlight(delta);
        pressAnimator.update();
        if (isVisible()) {
            renderBackground(mouseX, mouseY, delta);
            renderText(mouseX, mouseY, delta);
        }
    }

    @Override
    protected void renderBackground(float mouseX, float mouseY, float delta) {
        nvg.save();
        nvg.transform(
                Transform.identity()
                        .position(rectangle.x + (rectangle.width / 2f), rectangle.y + (rectangle.height / 2f))
                        .scale(new Vec2(1 - (pressAnimator.getFloat() * 0.08f)))
                        .pivot(new Vec2(rectangle.width / 2f, rectangle.height / 2f))
        );
        panelRenderer.renderPanel(rectangle.width, rectangle.height, delta);
        nvg.restore();
    }

    @Override
    protected void renderText(float mouseX, float mouseY, float delta) {
        Optional<String> text = textSupplier.get();
        if (text.isPresent()) {
            String txt = text.get();
            nvg.save();
            nvg.transform(null);
            NanoVG.RENDERER.TEXT.drawAlignedText(
                    GrapheneUI.regularFont(),
                    14f,
                    txt,
                    rectangle.x + rectangle.width * 0.5f,
                    rectangle.y + rectangle.height * 0.5f,
                    10000,
                    1,
                    Color.rgb(255, 255, 255).nvg(),
                    TextAlign.of(
                            TextAlign.ALIGN_CENTER,
                            TextAlign.ALIGN_MIDDLE
                    ),
                    false
            );
            nvg.restore();
        }
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);
        if (isZoom()) {
            if (pressed) {
                pressAnimator
                        .animateTo(1.0, 600)
                        .ease(Easing.EASE_OUT_CUBIC);
            } else {
                pressAnimator
                        .animateTo(0.0, 120)
                        .ease((Float x) -> Easing.LINEAR.apply(x));
            }
        }
    }
}
