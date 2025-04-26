package io.homo.grapheneui.utils;

import io.homo.grapheneui.nanovg.NanoVG;
import org.lwjgl.nanovg.NVGColor;

public class Color {
    private final int[] color;

    public Color(int[] color) {
        this.color = color;
    }

    public static Color hex(String hex) {
        return new Color(ColorUtil.toArray(hex));
    }

    public static Color from(Object object) {
        if (object instanceof Color) {
            return Color.rgba(
                    ((Color) object).red(),
                    ((Color) object).green(),
                    ((Color) object).blue(),
                    ((Color) object).alpha()
            );
        }
        if (object instanceof NVGColor) {
            return Color.rgba(
                    ((NVGColor) object).r(),
                    ((NVGColor) object).g(),
                    ((NVGColor) object).b(),
                    ((NVGColor) object).a()
            );
        }
        return new Color(ColorUtil.toArray(object));
    }

    public static Color rgb(int[] rgb) {
        return new Color(new int[]{rgb[0], rgb[1], rgb[2], 255});
    }

    public static Color rgba(int[] rgba) {
        return new Color(new int[]{rgba[0], rgba[1], rgba[2], rgba[3]});
    }

    public static Color rgb(int r, int g, int b) {
        return new Color(new int[]{r, g, b, 255});
    }

    public static Color rgba(int r, int g, int b, int a) {
        return new Color(new int[]{r, g, b, a});
    }

    public static Color rgb(float r, float g, float b) {
        return rgb(
                (int) (r * 255),
                (int) (g * 255),
                (int) (b * 255)
        );
    }

    public static Color rgba(float r, float g, float b, float a) {
        return rgba(
                (int) (r * 255),
                (int) (g * 255),
                (int) (b * 255),
                (int) (a * 255)
        );
    }

    public static Color rgb(int rgb) {
        return rgb(
                ColorUtil.red(rgb),
                ColorUtil.green(rgb),
                ColorUtil.blue(rgb)
        );
    }

    public static Color black() {
        return rgb(0, 0, 0);
    }

    public static Color rgba(int rgba) {
        return rgba(
                ColorUtil.red(rgba),
                ColorUtil.green(rgba),
                ColorUtil.blue(rgba),
                ColorUtil.alpha(rgba)
        );
    }

    public static NVGColor toNVG(Color color) {
        return NanoVG.colorRGBA(
                color.red(),
                color.green(),
                color.blue(),
                color.alpha()
        );
    }

    public static String toHEX(Color color) {
        return ColorUtil.toCode(color.color);
    }

    public static int toInt(Color color) {
        return ColorUtil.color(
                color.alpha(),
                color.red(),
                color.green(),
                color.blue()
        );
    }

    public int red() {
        return color[0];
    }

    public int green() {
        return color[1];
    }

    public int blue() {
        return color[2];
    }

    public int alpha() {
        return color[3];
    }

    public Color red(int v) {
        color[0] = v;
        return this;
    }

    public Color green(int v) {
        color[1] = v;
        return this;

    }

    public Color blue(int v) {
        color[2] = v;
        return this;

    }

    public Color alpha(int v) {
        color[3] = v;
        return this;

    }

    public NVGColor nvg() {
        return Color.toNVG(this);
    }

    public String hex() {
        return Color.toHEX(this);
    }

    public int integer() {
        return Color.toInt(this);
    }

    public Color copy() {
        return rgba(
                color[0],
                color[1],
                color[2],
                color[3]
        );
    }

}
