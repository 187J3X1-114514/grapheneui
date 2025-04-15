package io.homo.grapheneui.gui;

import io.homo.grapheneui.core.impl.EventListener;
import io.homo.grapheneui.gui.widgets.AbstractWidget;
import io.homo.grapheneui.impl.Renderable;
import io.homo.grapheneui.nanovg.NanoVG;
import io.homo.grapheneui.nanovg.NanoVGContext;
import io.homo.grapheneui.utils.MouseCursor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class NanoVGScreen extends Screen {
    protected final NanoVGContext nvg;
    protected final ArrayList<Renderable> renderable = new ArrayList<>();
    protected final ArrayList<EventListener> eventListener = new ArrayList<>();
    protected final ArrayList<AbstractWidget> widget = new ArrayList<>();
    protected boolean transparent = false;

    protected NanoVGScreen(Component title) {
        super(title);
        nvg = NanoVG.context;
        buildWidgets();
    }

    public boolean isTransparent() {
        return transparent;
    }

    public NanoVGScreen setTransparent(boolean transparent) {
        this.transparent = transparent;
        return this;
    }

    protected abstract void buildWidgets();

    @Override
    public void onClose() {
        super.onClose();
        MouseCursor.ARROW.use();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX_, int mouseY_, float partialTick) {
        float mouseX = (float) (Minecraft.getInstance().getWindow().getGuiScale() * mouseX_);
        float mouseY = (float) (Minecraft.getInstance().getWindow().getGuiScale() * mouseY_);
        drawBefore(guiGraphics, (int) mouseX, (int) mouseY, partialTick);
        nvg.begin(transparent);
        draw((int) mouseX, (int) mouseY, partialTick);
        nvg.end();
        drawAfter(guiGraphics, (int) mouseX, (int) mouseY, partialTick);
    }

    @Override
    protected void init() {
        super.init();
    }

    public void draw(int mouseX, int mouseY, float delta) {
        drawWidgets(mouseX, mouseY, delta);
        drawTooltips(mouseX, mouseY, delta);
    }

    public void drawTooltips(int mouseX, int mouseY, float delta) {
        for (AbstractWidget<?> abstractWidget : widget) {
            abstractWidget.renderTooltip(delta);
        }
    }

    public void drawAfter(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {

    }

    public void drawBefore(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.renderBackground(guiGraphics, mouseX, mouseY, delta);
    }

    public void drawWidgets(int mouseX, int mouseY, float delta) {
        for (AbstractWidget<?> w : widget) {
            w.render(mouseX, mouseY, delta);
        }
        for (Renderable w : renderable) {
            w.render(mouseX, mouseY, delta);
        }
    }


    @Override
    protected void clearWidgets() {
        super.clearWidgets();
        renderable.clear();
        widget.clear();
        eventListener.clear();
    }

    protected void removeWidget(Object widget) {
        if (widget instanceof Renderable) {
            renderable.remove(widget);
        }
        if (widget instanceof AbstractWidget) {
            this.widget.remove(widget);
        }
        if (widget instanceof EventListener) {
            this.eventListener.remove(widget);
        }
    }

    protected <T extends AbstractWidget<?>> T addWidget(T w) {
        widget.add(w);
        eventListener.add(w);
        return w;
    }

    @Override
    protected void rebuildWidgets() {
    }


    protected <T extends Renderable> T addRenderableOnly(T renderable) {
        this.renderable.add(renderable);
        return renderable;
    }

    protected <T extends AbstractWidget<?>> T addRenderableWidget(T widget) {
        renderable.add(widget);
        eventListener.add(widget);
        return widget;
    }

    protected void invokeEventHandle(Consumer<EventListener> consumer) {
        for (EventListener handle : eventListener) {
            consumer.accept(handle);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        invokeEventHandle((handle) -> handle.mousePress((float) transformPos(mouseX), (float) transformPos(mouseY), button));
        super.mouseClicked(mouseX, mouseY, button);
        return true;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        invokeEventHandle((handle) -> handle.mouseRelease((float) transformPos(mouseX), (float) transformPos(mouseY), button));
        super.mouseReleased(mouseX, mouseY, button);
        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        invokeEventHandle((handle) -> handle.mouseDrag((float) transformPos(mouseX), (float) transformPos(mouseY), (float) transformPos(dragX), (float) transformPos(dragY), button));
        super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
        return true;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        invokeEventHandle((handle) -> handle.mouseScroll((float) transformPos(mouseX), (float) transformPos(mouseY), (float) transformPos(scrollX)));
        super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
        return true;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        invokeEventHandle((handle) -> handle.keyRelease(keyCode, scanCode, modifiers));
        super.keyReleased(keyCode, scanCode, modifiers);
        return true;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        invokeEventHandle((handle) -> handle.keyPress(keyCode, scanCode, modifiers));
        super.keyPressed(keyCode, scanCode, modifiers);
        return true;
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        invokeEventHandle((handle) -> handle.mouseMove((float) transformPos(mouseX), (float) transformPos(mouseY)));
        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        invokeEventHandle((handle) -> handle.charTyped(codePoint, modifiers));
        return true;
    }

    protected double transformPos(double pos) {
        return Minecraft.getInstance().getWindow().getGuiScale() * pos;
    }
}
