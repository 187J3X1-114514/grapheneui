package io.homo.grapheneui.gui.effects.parameter;

public abstract class Parameter<T> {
    protected boolean isUniform = false;
    protected T value;

    public Parameter(boolean isUniform) {
        this.isUniform = isUniform;
    }

    public boolean isUniform() {
        return isUniform;
    }

    public abstract void setUniform(int loc);

    public T getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = (T) value;
    }
}
