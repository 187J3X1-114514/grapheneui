package io.homo.grapheneui.gui.widgets.buttons;

import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.utils.Color;

import java.util.Optional;
import java.util.function.Supplier;

public class FlatButton<T> extends PressButton<T> {
    public FlatButton(Rectangle rectangle) {
        super(rectangle);
    }

    public FlatButton(Rectangle rectangle, Supplier<Optional<String>> textSupplier) {
        super(rectangle, textSupplier);
    }

    @Override
    protected void renderBackground(float mouseX, float mouseY, float delta) {
        panelRenderer.shadow(false);
        panelRenderer.setHoverAlpha(30);
        panelRenderer.panelColor(Color.rgba(255, 255, 255, 0));
        panelRenderer.shadowColor(Color.rgba(255, 255, 255, 0));
        super.renderBackground(mouseX, mouseY, delta);
    }
}
