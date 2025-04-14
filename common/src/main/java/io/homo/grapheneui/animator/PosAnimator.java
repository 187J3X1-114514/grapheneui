package io.homo.grapheneui.animator;

public class PosAnimator {
    private final long duration;
    private final NumberAnimator posXAnimator = NumberAnimator.create();
    private final NumberAnimator posYAnimator = NumberAnimator.create();

    public PosAnimator(long duration) {
        this.duration = duration;
    }

    public void update(int x, int y) {
        if (posXAnimator.targetValue() == 0) {
            posXAnimator.set(x);
        }
        if (posYAnimator.targetValue() == 0) {
            posYAnimator.set(y);
        }
        if (posXAnimator.targetValue() != x && !posXAnimator.isRunning()) {
            posXAnimator.animateTo(x, duration, false).ease(Easing.EASE_IN_QUAD);
        }
        if (posYAnimator.targetValue() != y && !posYAnimator.isRunning()) {
            posYAnimator.animateTo(y, duration, false).ease(Easing.EASE_IN_QUAD);
        }

        if (posXAnimator.targetValue() != x && posXAnimator.isRunning()) {
            posXAnimator.setTargetValue(x);
        }
        if (posYAnimator.targetValue() != y && posYAnimator.isRunning()) {
            posYAnimator.setTargetValue(y);

        }
        posXAnimator.update();
        posYAnimator.update();
    }

    public float x() {
        return posXAnimator.getFloat();
    }

    public float y() {
        return posYAnimator.getFloat();
    }
}
