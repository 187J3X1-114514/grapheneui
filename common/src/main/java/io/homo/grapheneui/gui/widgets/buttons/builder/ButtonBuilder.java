package io.homo.grapheneui.gui.widgets.buttons.builder;

public class ButtonBuilder {
    public static BasicButtonBuilder basicButton() {
        return new BasicButtonBuilder();
    }

    public static FlatButtonBuilder flatButton() {
        return new FlatButtonBuilder();
    }

    public static IconButtonBuilder iconButton() {
        return new IconButtonBuilder();
    }

    public static PressButtonBuilder pressButton() {
        return new PressButtonBuilder();
    }
}
