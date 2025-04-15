package io.homo.grapheneui.gui.widgets.switchs;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.animator.Easing;
import io.homo.grapheneui.animator.NumberAnimator;
import io.homo.grapheneui.core.MouseButton;
import io.homo.grapheneui.core.Transform;
import io.homo.grapheneui.gui.widgets.AbstractWidget;
import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.impl.Vec2;
import io.homo.grapheneui.utils.Color;
import io.homo.grapheneui.utils.MouseCursor;

import java.util.ArrayList;


public class Switch extends AbstractWidget<Switch> {
    protected final NumberAnimator zoomAnimator = NumberAnimator.create();
    protected final NumberAnimator toggleAnimator = NumberAnimator.create();
    protected final NumberAnimator widthAnimator = NumberAnimator.create();
    private final Color trackColor = Color.rgba(0xEEEEEEFF);
    private final Color activeColor = Color.rgba(0x4CD964FF);
    private final Color thumbColor = Color.rgba(0xFFFFFFFF);
    private final Color borderColor = Color.rgba(0xCCCCCCFF);
    public Rectangle rectangle;
    protected boolean zoom = true;
    protected boolean checked = false;

    public Switch(Rectangle rectangle) {
        super();
        this.rectangle = rectangle;
        setChecked(false);
        this.eventListenerMap.put("change", new ArrayList<>());
    }

    public boolean isZoom() {
        return zoom;
    }

    public void setZoom(boolean zoom) {
        this.zoom = zoom;
    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    @Override
    public void render(float mouseX, float mouseY, float delta) {
        toggleAnimator.update();
        zoomAnimator.update();
        widthAnimator.update();
        if (isVisible()) {
            nvg.save();
            float scale = zoom ? 1 - (zoomAnimator.getFloat() * 0.08f) : 1;
            nvg.transform(
                    Transform.identity()
                            .position(rectangle.x, rectangle.y)
                            .scale(new Vec2(scale))
                            .pivot(new Vec2(
                                    -(1 - scale) * rectangle.width * 0.5f,
                                    -(1 - scale) * rectangle.height * 0.5f
                            ))
            );
            float width = rectangle.width;
            float height = rectangle.height;
            float radius = height * 0.5f;
            float thumbSize = height - 4;

            final float borderCompensation = 2.0f;
            final float actualWidth = width - thumbSize - borderCompensation * 2;

            /// 背景
            nvg.save();
            nvg.beginPath();
            nvg.roundedRect(
                    0,
                    0,
                    width,
                    height,
                    radius
            );
            if (checked) {
                nvg.linearGradient(
                        0,
                        0,
                        width,
                        height,
                        GrapheneUI.THEME.THEME_TRANSITION_A,
                        GrapheneUI.THEME.THEME_TRANSITION_B
                );
            } else {
                nvg.strokeColor(GrapheneUI.THEME.SWITCH_BACKGROUND_DEACTIVATE);
                nvg.strokeWidth(1);
            }
            nvg.endPath(checked);
            nvg.restore();
            /// 背景

            /// 滑块
            nvg.save();
            float thumbWidth = 5 * widthAnimator.getFloat();
            nvg.beginPath();
            nvg.roundedRect(
                    borderCompensation + (actualWidth * toggleAnimator.getFloat()) - (isChecked() ? thumbWidth : 0),
                    (height / 2) - (thumbSize / 2),
                    thumbSize + thumbWidth,
                    thumbSize,
                    thumbSize
            );
            nvg.fillColor(isChecked() ? GrapheneUI.THEME.SWITCH_THUMB_ACTIVATE : GrapheneUI.THEME.SWITCH_THUMB_DEACTIVATE);
            nvg.endPath();
            nvg.restore();
            /// 滑块


            nvg.restore();
        }
    }

    @Override
    public void mouseMove(float x, float y) {
        super.mouseMove(x, y);
        if (rectangle.in(x, y)) {
            if (!hovered) {
                MouseCursor.HAND.use();
            }
            setHovered(true);
        } else {
            if (hovered) {
                MouseCursor.ARROW.use();
            }
            setHovered(false);
        }
    }


    @Override
    public void mousePress(float x, float y, int button) {
        super.mousePress(x, y, button);
        if (button == MouseButton.LEFT) {
            if (rectangle.in(x, y)) {
                setPressed(true);
                setFocused(true);
            }
        }
    }

    @Override
    public void mouseRelease(float x, float y, int button) {
        super.mouseRelease(x, y, button);
        if (button == MouseButton.LEFT) {

            setPressed(false);
            setFocused(false);
            if (rectangle.in(x, y)) {
                setChecked(!checked);
                invokeEventListener("change");
            }
        }
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        toggleAnimator
                .animateTo(checked ? 1 : 0, 120)
                .ease(Easing.EASE_OUT_QUINT);
    }

    @Override
    public void setPressed(boolean pressed) {
        super.setPressed(pressed);

        if (pressed) {
            zoomAnimator
                    .animateTo(1.0, 600)
                    .ease(Easing.EASE_OUT_CUBIC);
            widthAnimator.animateTo(1.0, 80)
                    .ease(Easing.EASE_OUT_QUINT);
        } else {

            zoomAnimator
                    .animateTo(0.0, 120)
                    .ease((Float x) -> Easing.EASE_IN_QUINT.apply(x));
            widthAnimator.animateTo(0.0, 80)
                    .ease(Easing.EASE_IN_QUINT);
        }

    }
}
