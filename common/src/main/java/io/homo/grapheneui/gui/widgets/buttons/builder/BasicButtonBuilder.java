package io.homo.grapheneui.gui.widgets.buttons.builder;

import io.homo.grapheneui.gui.widgets.buttons.AbstractButton;

public class BasicButtonBuilder extends AbstractButtonBuilder<AbstractButton<?>, BasicButtonBuilder> {
    @Override
    protected AbstractButton<?> createButton() {
        return new AbstractButton<>(rectangle) {
        };
    }


}