package io.homo.grapheneui.gui.widgets.buttons;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.core.MouseButton;
import io.homo.grapheneui.core.Transform;
import io.homo.grapheneui.core.renderer.BasePanelRenderer;
import io.homo.grapheneui.gui.widgets.AbstractWidget;
import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.nanovg.NanoVG;
import io.homo.grapheneui.nanovg.renderer.TextAlign;
import io.homo.grapheneui.utils.Color;
import io.homo.grapheneui.utils.MouseCursor;

import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class AbstractButton<T> extends AbstractWidget<T> {
    public Rectangle rectangle;
    protected BasePanelRenderer panelRenderer = new BasePanelRenderer(
            GrapheneUI.THEME.BUTTON_PANEL,
            GrapheneUI.THEME.BUTTON_SHADOW,
            BasePanelRenderer.PanelVariant.SHADOW
    ).setClickable(true);
    protected Supplier<Optional<String>> textSupplier;
    protected float roundedSize = GrapheneUI.CONST.BUTTON_ROUNDED_SIZE;

    public AbstractButton(Rectangle rectangle, Supplier<Optional<String>> textSupplier) {
        super();
        panelRenderer.setRadius(roundedSize);
        this.rectangle = rectangle;
        this.textSupplier = textSupplier == null ? Optional::empty : textSupplier;
        eventListenerMap.put("click", new ArrayList<>());

    }

    public AbstractButton(Rectangle rectangle) {
        this(rectangle, null);
    }

    public BasePanelRenderer getPanelRenderer() {
        return panelRenderer;
    }

    public Supplier<Optional<String>> getTextSupplier() {
        return textSupplier;
    }

    public void setTextSupplier(Supplier<Optional<String>> textSupplier) {
        this.textSupplier = textSupplier == null ? Optional::empty : textSupplier;
    }

    public float getRoundedSize() {
        return roundedSize;
    }

    public void setRoundedSize(float roundedSize) {
        this.roundedSize = roundedSize;
        panelRenderer.setRadius(roundedSize);
    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public void setText(String text) {
        this.textSupplier = text == null ? Optional::empty : () -> Optional.of(text);
    }

    @Override
    public void mousePress(float x, float y, int button) {
        if (button == MouseButton.LEFT && rectangle.in(x, y)) {
            setPressed(true);
            setFocused(true);
        }
    }

    @Override
    public void mouseRelease(float x, float y, int button) {
        setPressed(false);
        if (button == MouseButton.LEFT && rectangle.in(x, y)) {
            panelRenderer.flash();
        }
    }


    @Override
    public void mouseMove(float x, float y) {
        super.mouseMove(x, y);
        if (rectangle.in(x, y)) {
            if (!hovered) {
                MouseCursor.HAND.use();
            }
            setHovered(true);
            panelRenderer.setHover(true);
        } else {
            if (hovered) {
                MouseCursor.ARROW.use();
            }
            setHovered(false);
            panelRenderer.setHover(false);
        }
    }

    @Override
    public void render(float mouseX, float mouseY, float delta) {
        panelRenderer.updateHighlight(delta);
        tooltipRenderer.updateHighlight(delta);
        if (isVisible()) {
            nvg.save();
            nvg.transform(
                    Transform.identity()
                            .position(rectangle.x, rectangle.y)
            );
            renderBackground(mouseX, mouseY, delta);
            renderText(mouseX, mouseY, delta);
            nvg.restore();
        }
    }

    protected void renderBackground(float mouseX, float mouseY, float delta) {
        panelRenderer.renderPanel(rectangle.width, rectangle.height, delta);
    }

    protected void renderText(float mouseX, float mouseY, float delta) {
        Optional<String> text = textSupplier.get();
        if (text.isPresent()) {
            nvg.save();
            nvg.transform(null);
            NanoVG.RENDERER.TEXT.drawAlignedText(
                    GrapheneUI.regularFont(),
                    14f,
                    text.get(),
                    rectangle.x + rectangle.width * 0.5f,
                    rectangle.y + rectangle.height * 0.5f,
                    10000,
                    1,
                    Color.rgb(255, 255, 255),
                    TextAlign.of(
                            TextAlign.ALIGN_CENTER,
                            TextAlign.ALIGN_MIDDLE
                    ),
                    false
            );
            nvg.restore();

        }

    }
}
