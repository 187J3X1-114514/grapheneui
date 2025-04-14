package io.homo.grapheneui.gui.effects;

import com.mojang.blaze3d.pipeline.RenderTarget;
import io.homo.grapheneui.gui.effects.parameter.Parameter;
import io.homo.grapheneui.opengl.shader.GlGeneralShaderProgram;
import io.homo.grapheneui.utils.MinecraftUtil;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractEffect {
    protected final Map<String, Parameter<?>> parameters = new HashMap<>();
    protected GlGeneralShaderProgram shaderProgram;

    public void load() {
        addParameters();
    }

    public void prepare() {
        shaderProgram.use();
        if (parameters.containsKey("screenSize")) {
            parameters.get("screenSize").setValue(MinecraftUtil.getScreenSize());
        }
        for (String key : parameters.keySet()) {
            if (parameters.get(key).isUniform()) {
                parameters.get(key).setUniform(shaderProgram.getUniformLocation(key));
            }
        }
    }

    protected abstract void addParameters();

    public abstract void process(RenderTarget input, RenderTarget output);

    @SuppressWarnings("unchecked")
    public <T> T getParameter(String key) {
        return (T) parameters.get(key);
    }

    public void setParameter(String key, Object value) {
        if (parameters.containsKey(key)) {
            parameters.get(key).setValue(value);
        }
    }
}
