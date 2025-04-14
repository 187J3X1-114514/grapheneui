package io.homo.grapheneui.core.impl;

public interface EventListener {
    default void mousePress(float x, float y, int button) {
    }

    default void mouseRelease(float x, float y, int button) {
    }

    default void mouseMove(float x, float y) {
    }

    default void mouseDrag(float mouseX, float mouseY, float dragX, float dragY, int button) {
    }

    default void mouseScroll(float x, float y, double scrollX) {
    }

    default void keyPress(int keyCode, int scancode, int modifiers) {
    }

    default void keyRelease(int keyCode, int scancode, int modifiers) {
    }

    default void charTyped(char codePoint, int modifiers) {
    }
}