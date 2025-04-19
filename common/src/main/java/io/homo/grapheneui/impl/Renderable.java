package io.homo.grapheneui.impl;

public interface Renderable {
    void render(float mouseX, float mouseY, float delta);

    default int getZIndex() {
        return 0;
    }
}
