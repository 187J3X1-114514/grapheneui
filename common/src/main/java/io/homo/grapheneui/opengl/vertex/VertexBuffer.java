package io.homo.grapheneui.opengl.vertex;

import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL15.*;


public class VertexBuffer implements AutoCloseable {
    private final int id;
    private int target;

    public VertexBuffer() {
        id = glGenBuffers();
    }

    public void bind(int target) {
        this.target = target;
        glBindBuffer(target, id);
    }

    public void uploadData(float[] data, int usage) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
        buffer.put(data).flip();
        glBufferData(target, buffer, usage);
    }

    public void uploadData(int[] data, int usage) {
        IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
        buffer.put(data).flip();
        glBufferData(target, buffer, usage);
    }

    @Override
    public void close() {
        glDeleteBuffers(id);
    }
}