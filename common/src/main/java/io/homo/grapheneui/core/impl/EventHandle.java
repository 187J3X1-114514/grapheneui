package io.homo.grapheneui.core.impl;

import java.util.function.Consumer;

public interface EventHandle<T> {
    void addEventListener(String type, Consumer<T> consumer);

    void removeEventListener(String type, Consumer<T> consumer);
}
