package io.homo.grapheneui.gui.widgets.buttons.builder;

import io.homo.grapheneui.gui.widgets.buttons.PressButton;

public class PressButtonBuilder extends AbstractButtonBuilder<PressButton<?>, PressButtonBuilder> {
    private boolean zoomEnabled = true;

    public PressButtonBuilder withZoom(boolean enabled) {
        this.zoomEnabled = enabled;
        return this;
    }

    @Override
    protected PressButton<?> createButton() {
        PressButton<?> button = new PressButton<>(rectangle);
        button.setZoom(zoomEnabled);
        return button;
    }
}