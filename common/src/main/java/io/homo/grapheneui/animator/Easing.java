package io.homo.grapheneui.animator;

import java.util.function.Function;

public class Easing {
    public static EasingMethod LINEAR = (Float T) -> T;
    public static EasingMethod EASE_IN_QUAD = (Float T) -> T * T;
    public static EasingMethod EASE_OUT_QUAD = (Float T) -> T * (2 - T);
    public static EasingMethod EASE_IN_OUT_QUAD = (Float T) -> T < 0.5 ? 2 * T * T : -1 + (4 - 2 * T) * T;
    public static EasingMethod EASE_IN_CUBIC = (Float T) -> T * T * T;
    public static EasingMethod EASE_OUT_CUBIC = (Float T) -> (--T) * T * T + 1;
    public static EasingMethod EASE_IN_OUT_CUBIC = (Float T) -> T < 0.5 ? 4 * T * T * T : (T - 1) * (2 * T - 2) * (2 * T - 2) + 1;
    public static EasingMethod EASE_IN_QUART = (Float T) -> T * T * T * T;
    public static EasingMethod EASE_OUT_QUART = (Float T) -> 1 - (--T) * T * T * T;
    public static EasingMethod EASE_IN_OUT_QUART = (Float T) -> T < 0.5 ? 8 * T * T * T * T : 1 - 8 * (--T) * T * T * T;
    public static EasingMethod EASE_IN_QUINT = (Float T) -> T * T * T * T * T;
    public static EasingMethod EASE_OUT_QUINT = (Float T) -> 1 + (--T) * T * T * T * T;
    public static EasingMethod EASE_IN_OUT_QUINT = (Float T) -> T < 0.5 ? 16 * T * T * T * T * T : 1 + 16 * (--T) * T * T * T * T;
    public static EasingMethod EASE_IN_BACK = (Float x) -> 2.70158f * x * x * x - 1.70158f * x * x;
    public static EasingMethod EASE_OUT_BACK = (Float x) -> (float) (1f + 2.70158f * Math.pow(x - 1f, 3f) + 1.70158f * Math.pow(x - 1f, 2f));

    public interface EasingMethod extends Function<Float, Float> {

    }
}