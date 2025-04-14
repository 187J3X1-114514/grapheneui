package io.homo.grapheneui.gui.effects.parameter;

import org.lwjgl.opengl.GL30;

public class Int extends Parameter<Integer> {
    public Int(boolean isUniform) {
        super(isUniform);
    }

    @Override
    public void setUniform(int loc) {
        GL30.glUniform1i(loc, value);
    }
}
