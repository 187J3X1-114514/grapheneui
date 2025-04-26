package io.homo.grapheneui.utils;

public class UIScalingCalculator {

    public static double calculateUIScaling(int displayWidth, int displayHeight, double userZoom) {
        int BASE_WIDTH = 1366;
        int BASE_HEIGHT = 768;
        double MIN_SCALE = 1.0;
        double MAX_SCALE = 2.0;
        double SCALING_EXPONENT = 0.25;
        double baseArea = BASE_WIDTH * BASE_HEIGHT;
        double currentArea = displayWidth * displayHeight;
        if (currentArea <= 0) return clamp(userZoom, MIN_SCALE, MAX_SCALE);
        double areaRatio = currentArea / baseArea;
        double areaScaling = Math.pow(areaRatio, SCALING_EXPONENT);
        double sizeCompensation = calculateSizeCompensation(
                BASE_WIDTH, BASE_HEIGHT,
                displayWidth, displayHeight
        );
        return clamp(areaScaling * sizeCompensation * userZoom, MIN_SCALE, MAX_SCALE);
    }

    private static double calculateSizeCompensation(double baseW, double baseH,
                                                    double currentW, double currentH) {
        double baseLong = Math.max(baseW, baseH);
        double currentLong = Math.max(currentW, currentH);
        double longRatio = baseLong / currentLong;
        return longRatio < 0.7 ? 1.15 : 1.0;
    }

    private static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }
}
