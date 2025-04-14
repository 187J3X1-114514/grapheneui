package io.homo.grapheneui.utils;

import java.util.Arrays;

public class ColorUtil {
    public static int alpha(int color) {
        return color >>> 24;
    }

    public static int red(int color) {
        return color >> 16 & 255;
    }

    public static int green(int color) {
        return color >> 8 & 255;
    }

    public static int blue(int color) {
        return color & 255;
    }

    public static int color(int alpha, int red, int green, int blue) {
        return alpha << 24 | red << 16 | green << 8 | blue;
    }

    public static int color(int red, int green, int blue) {
        return color(255, red, green, blue);
    }

    public static String RGB_to_RGBA(String code) {
        String codeData = code.replace("#", "");
        if (codeData.length() == 6) {
            return "#FF" + codeData.toUpperCase();
        }
        if (codeData.length() == 8) {
            return code.toUpperCase();
        }
        throw new IllegalArgumentException("Invalid RGB color code: " + code);
    }

    public static int[] toArray(String code, String toFormat) {
        code = RGB_to_RGBA(code).replace("#", "");
        int a = Integer.parseInt(code.substring(0, 2), 16);
        int r = Integer.parseInt(code.substring(2, 4), 16);
        int g = Integer.parseInt(code.substring(4, 6), 16);
        int b = Integer.parseInt(code.substring(6, 8), 16);

        toFormat = toFormat.toLowerCase();
        if (!Arrays.asList("rgba", "argb", "rgb").contains(toFormat)) {
            throw new IllegalArgumentException(toFormat + " is not a valid format (rgba, argb, rgb)");
        }
        if (toFormat.equals("rgba")) {
            return new int[]{r, g, b, a};
        }
        if (toFormat.equals("argb")) {
            return new int[]{a, r, g, b};
        }
        if (toFormat.equals("rgb")) {
            return new int[]{r, g, b};
        }
        throw new IllegalArgumentException("Invalid format: " + toFormat);
    }


    public static int[] toArray(Object code) {
        if (code instanceof int[]) {
            return (int[]) code;
        }
        if (code instanceof String) {
            return toArray((String) code, "rgba");
        }
        throw new IllegalArgumentException("Unsupported code type: " + code.getClass().getName());
    }

    public static String toCode(int[] value, boolean forceRgba) {
        int r, g, b, a;
        if (value.length == 3) {
            r = value[0];
            g = value[1];
            b = value[2];
            a = 255;
        } else if (value.length == 4) {
            a = value[0];
            r = value[1];
            g = value[2];
            b = value[3];
        } else {
            throw new IllegalArgumentException("Unexpected shape of input: " + Arrays.toString(value));
        }

        if (forceRgba || a != 255) {
            return String.format("#%02X%02X%02X%02X", a, r, g, b);
        } else {
            return String.format("#%02X%02X%02X", r, g, b);
        }
    }

    public static String toCode(int[] value) {
        return toCode(value, false);
    }

    public static String mix(String codeFore, String codePost, double weight) {
        int[] foreArray = toArray(codeFore);
        int[] postArray = toArray(codePost);
        int[] mixedRgb = new int[foreArray.length];
        for (int i = 0; i < foreArray.length; i++) {
            mixedRgb[i] = (int) (foreArray[i] * weight + postArray[i] * (1 - weight));
        }
        return toCode(mixedRgb);
    }

}
