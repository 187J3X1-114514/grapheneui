package io.homo.grapheneui.gui.effects.parameter;

import io.homo.grapheneui.impl.Vec2;
import org.lwjgl.opengl.GL30;

public class Vector2 extends Parameter<Vec2> {
    public Vector2(boolean isUniform) {
        super(isUniform);
    }

    @Override
    public void setUniform(int loc) {
        GL30.glUniform2f(loc, value.x, value.y);
    }
}
