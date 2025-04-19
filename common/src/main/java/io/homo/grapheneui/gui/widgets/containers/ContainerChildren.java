package io.homo.grapheneui.gui.widgets.containers;

import io.homo.grapheneui.core.impl.EventListener;
import io.homo.grapheneui.gui.widgets.AbstractWidget;

public class ContainerChildren implements EventListener {
    protected AbstractWidget<?> widget;
    protected Container parent;

    public ContainerChildren(AbstractWidget<?> widget) {
        this.widget = widget;
    }

    public void setParent(Container parent) {
        this.parent = parent;
    }

    @Override
    public void mousePress(float x, float y, int button) {
        widget.mousePress(x, y, button);
    }

    @Override
    public void mouseRelease(float x, float y, int button) {
        widget.mouseRelease(x, y, button);
    }

    @Override
    public void mouseMove(float x, float y) {
        widget.mouseMove(x, y);
    }

    @Override
    public void mouseDrag(float mouseX, float mouseY, float dragX, float dragY, int button) {
        widget.mouseDrag(mouseX, mouseY, dragX, dragY, button);
    }

    @Override
    public void mouseScroll(float x, float y, double scrollX) {
        widget.mouseScroll(x, y, scrollX);
    }

    @Override
    public void keyPress(int keyCode, int scancode, int modifiers) {
        widget.keyPress(keyCode, scancode, modifiers);
    }

    @Override
    public void keyRelease(int keyCode, int scancode, int modifiers) {
        widget.keyRelease(keyCode, scancode, modifiers);
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {
        widget.charTyped(codePoint, modifiers);
    }
}
