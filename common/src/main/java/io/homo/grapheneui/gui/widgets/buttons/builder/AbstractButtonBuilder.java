package io.homo.grapheneui.gui.widgets.buttons.builder;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.gui.widgets.AbstractWidgetBuilder;
import io.homo.grapheneui.gui.widgets.buttons.AbstractButton;
import io.homo.grapheneui.impl.Rectangle;

public abstract class AbstractButtonBuilder<T extends AbstractButton<?>, B extends AbstractButtonBuilder<T, B>> extends AbstractWidgetBuilder<T, B> {
    protected Rectangle rectangle;
    protected String text;
    protected float roundedSize = GrapheneUI.CONST.BUTTON_ROUNDED_SIZE;
    protected boolean visible = true;

    public B withRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
        return self();
    }

    public B withText(String text) {
        this.text = text;
        return self();
    }


    public B withRoundedSize(float roundedSize) {
        this.roundedSize = roundedSize;
        return self();
    }

    public B withVisibility(boolean visible) {
        this.visible = visible;
        return self();
    }

    protected abstract T createButton();

    @SuppressWarnings("unchecked")
    protected B self() {
        return (B) this;
    }

    public T build() {
        validate();
        T button = createButton();
        configureWidget(button);
        configureButton(button);
        return button;
    }

    protected void validate() {
        if (rectangle == null) {
            throw new IllegalArgumentException("Rectangle must be specified");
        }
    }

    protected void configureButton(AbstractButton<?> button) {
        button.setRoundedSize(roundedSize);
        button.setVisible(visible);
        if (text != null) {
            button.setText(text);
        } else if (textSupplier != null) {
            button.setTextSupplier(textSupplier);
        }
    }
}