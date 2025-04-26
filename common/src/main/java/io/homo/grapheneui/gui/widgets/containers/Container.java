package io.homo.grapheneui.gui.widgets.containers;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.core.Transform;
import io.homo.grapheneui.core.renderer.BasePanelRenderer;
import io.homo.grapheneui.gui.widgets.AbstractWidget;
import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.impl.Vec2;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Container extends AbstractWidget<Container> {
    protected Rectangle rectangle;
    protected BasePanelRenderer panelRenderer = new BasePanelRenderer(
            GrapheneUI.THEME.INTERFACE_BG_C,
            GrapheneUI.THEME.INTERFACE_BG_A,
            BasePanelRenderer.PanelVariant.SHADOW
    );
    protected List<ContainerChildren> children = new ArrayList<>();
    private Vec2 padding = new Vec2(2, 2);
    private boolean renderPanel = true;

    public boolean isRenderPanel() {
        return renderPanel;
    }

    public Container setRenderPanel(boolean renderPanel) {
        this.renderPanel = renderPanel;
        return this;
    }

    public Vec2 getPadding() {
        return padding;
    }

    public void setPadding(Vec2 padding) {
        this.padding = padding;
    }

    public Container addChild(AbstractWidget<?> widget) {
        children.add(new ContainerChildren(widget));
        return this;
    }

    public Container addChild(ContainerChildren child) {
        children.add(child);
        return this;
    }

    public Container removeChild(AbstractWidget<?> widget) {
        children.remove(widget);
        return this;
    }

    public Container clearChildren() {
        children.clear();
        return this;
    }

    @Override
    public Rectangle getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle rectangle) {
        this.rectangle = rectangle;
    }

    public BasePanelRenderer getPanelRenderer() {
        return panelRenderer;
    }

    @Override
    public void render(float mouseX, float mouseY, float delta) {
        nvg.save();
        panelRenderer.setClickable(false);
        if (!isVisible()) return;
        nvg.transform(Transform.fromPosition(
                getRectangle().x,
                getRectangle().y
        ));
        if (isRenderPanel())
            panelRenderer.renderPanel(
                    getRectangle().width,
                    getRectangle().height,
                    delta
            );
        nvg.resetTransform();
        nvg.resetGlobalTransform();
        Map<Integer, List<ContainerChildren>> layers = children.stream()
                .collect(Collectors.groupingBy(
                        (child) -> child.widget.getZIndex(),
                        TreeMap::new,
                        Collectors.toList()
                ));
        for (List<ContainerChildren> layer : layers.values()) {
            for (ContainerChildren child : layer) {
                child.setParent(this);
                nvg.resetTransform();
                nvg.resetGlobalTransform();
                nvg.globalTransform(
                        Transform.fromPosition(
                                getRectangle().x,
                                getRectangle().y
                        )
                );
                child.widget.render(
                        mouseX - getRectangle().x,
                        mouseY - getRectangle().y,
                        delta
                );
                nvg.resetTransform();
                nvg.resetGlobalTransform();
            }
        }
        nvg.restore();
    }

    @Override
    public void renderTooltip(float delta) {
        if (!isVisible()) return;
        children.forEach(child -> child.widget.renderTooltip(delta));
    }

    public Container setPadding(float x, float y) {
        this.padding = new Vec2(x, y);
        return this;
    }

    @Override
    public void mouseMove(float x, float y) {
        x = x - getRectangle().x;
        y = y - getRectangle().y;
        float finalX = x;
        float finalY = y;
        children.forEach(child -> child.mouseMove(finalX, finalY));
    }

    @Override
    public void mousePress(float x, float y, int button) {
        x = x - getRectangle().x;
        y = y - getRectangle().y;
        float finalX = x;
        float finalY = y;
        children.forEach(child -> child.mousePress(finalX, finalY, button));

    }

    @Override
    public void mouseRelease(float x, float y, int button) {
        x = x - getRectangle().x;
        y = y - getRectangle().y;
        float finalX = x;
        float finalY = y;
        children.forEach(child -> child.mouseRelease(finalX, finalY, button));

    }

    @Override
    public void mouseDrag(float mouseX, float mouseY, float dragX, float dragY, int button) {
        float x = mouseX - getRectangle().x;
        float y = mouseY - getRectangle().y;
        children.forEach(child -> child.mouseDrag(x, y, dragX, dragY, button));

    }

    @Override
    public void mouseScroll(float x, float y, double scrollX) {
        x = x - getRectangle().x;
        y = y - getRectangle().y;
        float finalX = x;
        float finalY = y;
        children.forEach(child -> child.mouseScroll(finalX, finalY, scrollX));

    }

    @Override
    public void keyPress(int keyCode, int scancode, int modifiers) {
        children.forEach(child -> child.keyPress(keyCode, scancode, modifiers));

    }

    @Override
    public void keyRelease(int keyCode, int scancode, int modifiers) {
        children.forEach(child -> child.keyRelease(keyCode, scancode, modifiers));

    }

    @Override
    public void charTyped(char codePoint, int modifiers) {
        children.forEach(child -> child.charTyped(codePoint, modifiers));

    }
}