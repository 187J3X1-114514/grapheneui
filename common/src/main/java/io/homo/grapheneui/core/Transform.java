package io.homo.grapheneui.core;

import io.homo.grapheneui.impl.Vec2;

public class Transform {
    private Vec2 position = new Vec2(0);
    private Vec2 scale = new Vec2(1);
    private Vec2 pivot = new Vec2(0);
    private float rotation;

    public static Transform identity() {
        return new Transform();
    }

    public static Transform fromPosition(float x, float y) {
        return new Transform().position(new Vec2(x, y));
    }

    public static Transform fromScale(float sx, float sy) {
        return new Transform().scale(new Vec2(sx, sy));
    }

    public Vec2 getPivot() {
        return pivot;
    }

    public Transform pivot(Vec2 pivot) {
        this.pivot = pivot;
        return this;
    }

    public float[] transformMatrix() {
        // M = T(position) * R(rotation) * S(scale) * T(-pivot)
        float cos = (float) Math.cos(rotation);
        float sin = (float) Math.sin(rotation);
        float sx = scale.x();
        float sy = scale.y();
        float[] S = {
                sx, 0,
                0, sy,
                0, 0
        };
        float[] R = {
                cos, sin,
                -sin, cos,
                0, 0
        };
        float[] T1 = {
                1, 0,
                0, 1,
                -pivot.x(), -pivot.y()
        };
        float[] T2 = {
                1, 0,
                0, 1,
                position.x(), position.y()
        };
        return multiplyMatrix(
                T2,
                multiplyMatrix(
                        R,
                        multiplyMatrix
                                (S, T1)
                )
        );
    }

    private float[] multiplyMatrix(float[] a, float[] b) {
        return new float[]{
                a[0] * b[0] + a[2] * b[1],  // a
                a[1] * b[0] + a[3] * b[1],  // b
                a[0] * b[2] + a[2] * b[3],  // c
                a[1] * b[2] + a[3] * b[3],  // d
                a[0] * b[4] + a[2] * b[5] + a[4],  // e
                a[1] * b[4] + a[3] * b[5] + a[5]   // f
        };
    }

    public Transform copy() {
        return new Transform()
                .position(position)
                .scale(scale)
                .rotation(rotation);
    }

    @Override
    public String toString() {
        return String.format(
                "Transform[pos=%s, scale=%s, rot=%.2f°]",
                position, scale, Math.toDegrees(rotation)
        );
    }

    public Vec2 getPosition() {
        return position;
    }

    public Transform position(Vec2 position) {
        this.position = position;
        return this;
    }

    public Transform position(float positionX, float positionY) {
        this.position = new Vec2(positionX, positionY);
        return this;
    }

    public Vec2 getScale() {
        return scale;
    }

    public Transform scale(Vec2 scale) {
        this.scale = scale;
        return this;
    }

    public Transform scale(float scale) {
        this.scale = new Vec2(scale);
        return this;
    }


    public Transform scale(float scaleX, float scaleY) {
        this.scale = new Vec2(scaleX, scaleY);
        return this;
    }

    public float getRotation() {
        return rotation;
    }

    public Transform rotation(float radians) {
        this.rotation = radians;
        return this;
    }

    public float getAngle() {
        return (float) Math.toDegrees(rotation);
    }

    public Transform angle(float angle) {
        this.rotation = (float) Math.toRadians(angle);
        return this;
    }
}