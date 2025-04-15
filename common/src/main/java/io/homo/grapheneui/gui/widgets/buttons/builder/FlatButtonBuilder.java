package io.homo.grapheneui.gui.widgets.buttons.builder;

import io.homo.grapheneui.gui.widgets.buttons.AbstractButton;
import io.homo.grapheneui.gui.widgets.buttons.FlatButton;
import io.homo.grapheneui.utils.Color;

public class FlatButtonBuilder extends AbstractButtonBuilder<FlatButton<?>, FlatButtonBuilder> {
    private Color backgroundColor = Color.rgba(255, 255, 255, 0);

    public FlatButtonBuilder withBackgroundColor(Color color) {
        this.backgroundColor = color;
        return this;
    }

    @Override
    protected FlatButton<?> createButton() {
        return new FlatButton<>(rectangle);
    }

    @Override
    protected void configureButton(AbstractButton<?> button) {
        super.configureButton(button);
        button.getPanelRenderer().panelColor(backgroundColor);
    }
}