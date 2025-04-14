package io.homo.grapheneui.animator;

import net.minecraft.Util;

import java.util.function.Consumer;

public class NumberAnimator {
    private double currentValue;
    private double startValue;
    private double targetValue;
    private long startTime;
    private long duration;
    private long delay;
    private Easing.EasingMethod easing = Easing.LINEAR;
    private Runnable onComplete;
    private Consumer<Double> onUpdate;
    private AnimationState state = AnimationState.IDLE;
    private boolean isReversed = false;
    
    public static NumberAnimator create() {
        return new NumberAnimator();
    }

    public static NumberAnimator create(double initialValue) {
        return new NumberAnimator().set(initialValue);
    }

    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }

    public double targetValue() {
        return targetValue;
    }

    public NumberAnimator animateTo(double target, long duration, boolean reset) {
        if (reset) {
            this.startValue = this.currentValue;
        }
        this.targetValue = target;
        this.duration = duration;
        this.startTime = Util.getMillis() + delay;
        this.state = AnimationState.RUNNING;
        return this;
    }

    public NumberAnimator smoothUpdateTo(double target, long duration) {
        if (state != AnimationState.RUNNING) {
            return animateTo(target, duration);
        }
        double remainingDistance = targetValue - currentValue;
        double newDistance = target - currentValue;
        if (remainingDistance * newDistance <= 0) {
            return animateTo(target, duration);
        }
        double speed = remainingDistance / this.duration;
        this.duration = (long) (Math.abs(newDistance / speed));
        this.targetValue = target;
        this.startValue = currentValue;
        this.startTime = Util.getMillis();
        return this;
    }

    public NumberAnimator animateTo(double target, long duration) {
        return animateTo(target, duration, true);
    }

    public NumberAnimator set(double value) {
        this.currentValue = value;
        this.targetValue = value;
        this.state = AnimationState.IDLE;
        return this;
    }

    public NumberAnimator delay(long delayMs) {
        this.delay = delayMs;
        return this;
    }

    public NumberAnimator ease(Easing.EasingMethod easing) {
        this.easing = easing;
        return this;
    }

    public NumberAnimator onComplete(Runnable callback) {
        this.onComplete = callback;
        return this;
    }

    public NumberAnimator onUpdate(Consumer<Double> callback) {
        this.onUpdate = callback;
        return this;
    }

    public void update() {
        if (state != AnimationState.RUNNING) return;

        final long currentTime = Util.getMillis();
        if (currentTime < startTime) return;

        final long elapsed = currentTime - startTime;
        if (elapsed <= 0) return;

        double progress = Math.min(elapsed / (double) duration, 1.0);

        if (progress >= 1.0) {
            finishAnimation();
            return;
        }

        double easedProgress = easing.apply((float) progress);
        currentValue = interpolate(startValue, targetValue, easedProgress);

        if (onUpdate != null) {
            onUpdate.accept(currentValue);
        }
    }

    private double interpolate(double start, double end, double progress) {
        return start + (end - start) * progress;
    }

    private void finishAnimation() {
        currentValue = targetValue;
        state = AnimationState.COMPLETED;
        if (onComplete != null) {
            onComplete.run();
            onComplete = null;
        }
    }

    public NumberAnimator reverse() {
        double temp = startValue;
        startValue = targetValue;
        targetValue = temp;
        startTime = Util.getMillis();
        isReversed = !isReversed;
        return this;
    }

    public void cancel() {
        state = AnimationState.IDLE;
    }

    public void reset() {
        currentValue = startValue;
        state = AnimationState.IDLE;
    }

    public double getValue() {
        return currentValue;
    }

    public float getFloat() {
        return (float) currentValue;
    }

    public int getInt() {
        return (int) Math.round(currentValue);
    }

    public boolean isRunning() {
        return state == AnimationState.RUNNING;
    }

    public boolean isCompleted() {
        return state == AnimationState.COMPLETED;
    }

    public enum AnimationState {
        IDLE,
        RUNNING,
        COMPLETED
    }
}