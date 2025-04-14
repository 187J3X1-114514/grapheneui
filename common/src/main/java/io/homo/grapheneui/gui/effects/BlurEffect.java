package io.homo.grapheneui.gui.effects;

import com.mojang.blaze3d.pipeline.RenderTarget;
import io.homo.grapheneui.gui.effects.parameter.Float;
import io.homo.grapheneui.gui.effects.parameter.Vector2;
import io.homo.grapheneui.impl.Vec2;
import io.homo.grapheneui.opengl.GlState;
import io.homo.grapheneui.opengl.shader.GlGeneralShaderProgram;
import io.homo.grapheneui.opengl.vertex.VertexArray;
import io.homo.grapheneui.opengl.vertex.VertexBuffer;
import io.homo.grapheneui.utils.FileReadHelper;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;

public class BlurEffect extends AbstractEffect {
    @Override
    public void load() {
        shaderProgram = GlGeneralShaderProgram.create()
                .addAllFragShaderTextList(FileReadHelper.readText("/shader/blur/GaussianBlur.frag.glsl"))
                .addAllVertShaderTextList(FileReadHelper.readText("/shader/blur/GaussianBlur.vert.glsl"))
                .setShaderName("GaussianBlur")
                .build()
                .compileShader();
        addParameters();
    }

    @Override
    protected void addParameters() {
        parameters.put("screenSize", new Vector2(true));
        parameters.put("radius", new Float(true));
        parameters.put("sigma", new Float(true));
    }

    @Override
    public void process(RenderTarget input, RenderTarget output) {
        _process(input, output, new Vec2(0, 1));
        _process(output, output, new Vec2(1, 0));
    }

    private void _process(RenderTarget input, RenderTarget output, Vec2 direction) {
        try (GlState ignored = new GlState()) {
            output.bindWrite(true);
            shaderProgram.use();
            shaderProgram.setTexture("uTexture", input.getColorTextureId(), 0);
            shaderProgram.setVec2("direction", direction.x, direction.y);
            glColorMask(true, true, true, false);
            glDisable(GL_DEPTH_TEST);
            glDepthMask(false);
            try (VertexArray vao = new VertexArray();
                 VertexBuffer vbo = new VertexBuffer()) {
                float[] vertices = {
                        -1f, -1f, 0f, 0f,
                        1f, -1f, 1f, 0f,
                        1f, 1f, 1f, 1f,
                        -1f, 1f, 0f, 1f
                };
                vao.bind();
                vbo.bind(GL_ARRAY_BUFFER);
                vbo.uploadData(vertices, GL_STATIC_DRAW);
                int stride = 4 * java.lang.Float.BYTES;
                glEnableVertexAttribArray(0);
                glVertexAttribPointer(0, 2, GL_FLOAT, false, stride, 0);
                glEnableVertexAttribArray(1);
                glVertexAttribPointer(1, 2, GL_FLOAT, false, stride, 2 * java.lang.Float.BYTES);
                glDrawArrays(GL_TRIANGLE_FAN, 0, 4);
            }
            shaderProgram.clear();
            glDepthMask(true);
            glColorMask(true, true, true, true);
        }
    }
}
