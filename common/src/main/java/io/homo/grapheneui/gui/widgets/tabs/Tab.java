package io.homo.grapheneui.gui.widgets.tabs;

import io.homo.grapheneui.core.MouseButton;
import io.homo.grapheneui.core.icon.Icon;
import io.homo.grapheneui.core.renderer.TooltipRenderer;
import io.homo.grapheneui.gui.widgets.buttons.IconButton;
import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.utils.MouseCursor;

import java.util.Optional;
import java.util.function.Supplier;

public class Tab extends IconButton {
    public static final int TOP = 0x20001;
    public static final int CENTER = 0x20002;
    public static final int BOTTOM = 0x20003;
    private int pos = TOP;
    private TabBar bar;

    public Tab() {
        super(new Rectangle(0, 0, 0, 0), null);
        tooltipRenderer.setTooltipPos(TooltipRenderer.RIGHT);
    }

    public void setBar(TabBar bar) {
        this.bar = bar;
    }

    public int getPos() {
        return pos;
    }

    public Tab setPos(int pos) {
        this.pos = pos;
        return this;
    }

    @Override
    public void renderTooltip(float delta) {
        super.renderTooltip(delta);
    }

    @Override
    public void render(float mouseX, float mouseY, float delta) {
        super.render(mouseX, mouseY, delta);
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
                bar.setCurrentTab(this);
            }
        }
    }

    public Tab setTabIcon(Icon icon) {
        this.setIcon(icon);
        return this;
    }

    public Tab setTabText(String text) {
        this.setTooltip(text);
        return this;
    }

    public Tab setTabTextSupplier(Supplier<Optional<String>> text) {
        this.setTooltipSupplier(text);
        return this;
    }
}
