package io.homo.grapheneui.opengl.vertex;

import static org.lwjgl.opengl.GL33.*;

public class VertexArray implements AutoCloseable {
    private final int id;

    public VertexArray() {
        id = glGenVertexArrays();
    }

    public void bind() {
        glBindVertexArray(id);
    }

    public void unbind() {
        glBindVertexArray(0);
    }

    @Override
    public void close() {
        glDeleteVertexArrays(id);
    }
}