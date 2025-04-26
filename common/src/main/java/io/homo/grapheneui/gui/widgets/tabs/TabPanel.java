package io.homo.grapheneui.gui.widgets.tabs;

import io.homo.grapheneui.core.renderer.BasePanelRenderer;
import io.homo.grapheneui.gui.widgets.containers.Container;
import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.impl.Vec2;

public class TabPanel extends Container {
    protected Vec2 offset = new Vec2(0);

    public TabPanel() {
        panelRenderer.setVariant(BasePanelRenderer.PanelVariant.FILLED);
    }

    public void setOffset(Vec2 offset) {
        this.offset = offset;
    }

    public void setOffsetX(float offsetX) {
        this.offset.setX(offsetX);
    }

    public void setOffsetY(float offsetY) {
        this.offset.setY(offsetY);
    }

    @Override
    public Rectangle getRectangle() {
        Rectangle rect = super.getRectangle();
        return new Rectangle(
                (int) (rect.x + offset.x),
                (int) (rect.y + offset.y),
                rect.width,
                rect.height
        );
    }

    @Override
    public boolean isRenderPanel() {
        return false;
    }
}
