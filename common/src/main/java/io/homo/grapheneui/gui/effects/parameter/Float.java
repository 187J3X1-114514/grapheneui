package io.homo.grapheneui.gui.effects.parameter;

import org.lwjgl.opengl.GL30;

public class Float extends Parameter<java.lang.Float> {
    public Float(boolean isUniform) {
        super(isUniform);
    }

    @Override
    public void setUniform(int loc) {

        GL30.glUniform1f(loc, java.lang.Float.parseFloat(String.valueOf(value))); // 防止某些情况下value为Double而不是Float
    }
}
