package io.homo.grapheneui.gui.widgets.tabs;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.animator.Easing;
import io.homo.grapheneui.animator.NumberAnimator;
import io.homo.grapheneui.core.Transform;
import io.homo.grapheneui.core.renderer.BasePanelRenderer;
import io.homo.grapheneui.gui.widgets.containers.Container;
import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.utils.Color;
import io.homo.grapheneui.utils.ColorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TabBar extends Container {
    private static final float TAB_SPACING = 4;
    private static final float SLIDER_WIDTH = 3;
    private static final float SLIDER_HEIGHT_RATIO = 0.45f;
    private final NumberAnimator sliderY = NumberAnimator.create();
    private final NumberAnimator currentPanelPosAnimator = NumberAnimator.create();
    private final NumberAnimator lastPanelPosAnimator = NumberAnimator.create();
    private final NumberAnimator panelAlphaAnimator = NumberAnimator.create();
    private final List<Tab> tabsOrder = new ArrayList<>();
    public Map<Tab, TabPanel> tabMap = new HashMap<>();
    protected Tab currentTab;
    protected Tab lastTab;

    public TabBar() {
        panelRenderer.setVariant(BasePanelRenderer.PanelVariant.FILLED).panelColor(
                ColorUtil.mix(
                        GrapheneUI.THEME.INTERFACE_BG_C,
                        Color.rgb(0xFFFFFF),
                        0.95
                )
        );
    }

    public void setCurrentTab(Tab currentTab) {
        if (this.currentTab == null) {
            this.lastTab = currentTab;
            this.currentTab = currentTab;
            return;
        }
        if (this.currentTab != currentTab) {
            this.lastTab = this.currentTab;
            this.currentTab = currentTab;
            this.lastTab.getPanelRenderer().setChecked(false);
            this.lastTab.getPanelRenderer().setHover(false);
            this.currentTab.getPanelRenderer().setChecked(true);
            this.currentTab.getPanelRenderer().setHover(false);
            List<Tab> tabsOrder = new ArrayList<>(tabMap.keySet());
            int currentIndex = tabsOrder.indexOf(currentTab);
            int lastIndex = tabsOrder.indexOf(lastTab);
            boolean isDownward = currentIndex > lastIndex;

            float panelHeight = getRectangle().height - 2;
            int duration = 400;

            if (isDownward) {
                currentPanelPosAnimator.set(panelHeight).animateTo(0, duration).ease(Easing.EASE_OUT_QUINT);
                lastPanelPosAnimator.set(0).animateTo(-panelHeight, duration).ease(Easing.EASE_OUT_QUINT);
            } else {
                currentPanelPosAnimator.set(-panelHeight).animateTo(0, duration).ease(Easing.EASE_OUT_QUINT);
                lastPanelPosAnimator.set(0).animateTo(panelHeight, duration).ease(Easing.EASE_OUT_QUINT);
            }

            panelAlphaAnimator.set(1).animateTo(0, duration).ease(Easing.EASE_OUT_QUINT);
            sliderY.animateTo(getSliderTargetY(), 250).ease(Easing.EASE_OUT_QUINT);
        }
    }

    @Override
    public void render(float mouseX, float mouseY, float delta) {
        nvg.save();
        lastPanelPosAnimator.update();
        currentPanelPosAnimator.update();
        panelAlphaAnimator.update();
        updateCurrentTab();
        arrangeTabs();
        super.render(mouseX, mouseY, delta);
        renderSlider(delta);
        /////////
        nvg.transform(Transform.fromPosition(
                rectangle.x + 47,
                rectangle.y + 2
        ));
        if (getCurrentPanel() != null) {
            TabPanel tabPanel = getCurrentPanel();
            tabPanel.getPanelRenderer().renderPanel(
                    tabPanel.getRectangle().width,
                    tabPanel.getRectangle().height,
                    delta
            );
        } else if (getLastPanel() != null) {
            TabPanel tabPanel = getLastPanel();
            tabPanel.getPanelRenderer().renderPanel(
                    tabPanel.getRectangle().width,
                    tabPanel.getRectangle().height,
                    delta
            );
        }
        nvg.resetTransform();
        /////////
        nvg.scissor(
                (rectangle.x + 47) / nvg.globalScale(),
                rectangle.y + 2 / nvg.globalScale(),
                rectangle.width - 47,
                rectangle.height - 2
        );
        if (getLastPanel() != null && (panelAlphaAnimator.getFloat()) > 0) {
            TabPanel lastPanel = getLastPanel();
            nvg.globalAlpha(panelAlphaAnimator.getFloat());
            lastPanel.setRectangle(new Rectangle(
                    rectangle.x + 47,
                    rectangle.y + 2 + (int) lastPanelPosAnimator.getFloat(),
                    rectangle.width - 47,
                    rectangle.height - 2
            ));
            lastPanel.render(mouseX, mouseY, delta);
            nvg.globalAlpha(1);
        }
        if (getCurrentPanel() != null && (1 - panelAlphaAnimator.getFloat()) > 0) {
            TabPanel currentPanel = getCurrentPanel();
            nvg.globalAlpha(1 - panelAlphaAnimator.getFloat());
            currentPanel.setRectangle(new Rectangle(
                    rectangle.x + 47,
                    rectangle.y + 2 + (int) currentPanelPosAnimator.getFloat(),
                    rectangle.width - 47,
                    rectangle.height - 2
            ));
            currentPanel.render(mouseX, mouseY, delta);
            nvg.globalAlpha(1);

        }
        nvg.resetScissor();

        nvg.restore();
    }

    private void updateCurrentTab() {
        if (currentTab == null && !tabMap.isEmpty()) {
            setCurrentTab(tabsOrder.get(0));
        }
    }

    private void arrangeTabs() {
        List<Tab> leftTabs = new ArrayList<>();
        List<Tab> centerTabs = new ArrayList<>();
        List<Tab> rightTabs = new ArrayList<>();

        for (Tab tab : tabMap.keySet()) {
            tab.setBar(this);
            switch (tab.getPos()) {
                case Tab.TOP -> leftTabs.add(tab);
                case Tab.CENTER -> centerTabs.add(tab);
                case Tab.BOTTOM -> rightTabs.add(tab);
            }
        }
        layoutVerticalColumn(leftTabs, 0);
        layoutVerticalColumn(centerTabs, rectangle.getCenterY() - centerTabs.size() * 42);
        layoutVerticalColumn(rightTabs, rectangle.height - rightTabs.size() * 42);
    }

    private void layoutVerticalColumn(List<Tab> tabs, float yPos) {
        if (tabs.isEmpty()) return;
        float currentY = yPos;

        for (Tab tab : tabs) {
            tab.setRectangle(new Rectangle(3, (int) currentY, 42, 42));
            currentY += 42 + TAB_SPACING;
        }
    }

    private void renderSlider(float delta) {
        if (currentTab == null) return;
        sliderY.update();
        Rectangle tabRect = currentTab.getRectangle();
        float sliderHeight = 42 * SLIDER_HEIGHT_RATIO;
        nvg.save();
        nvg.transform(Transform.fromPosition(rectangle.x, rectangle.y));
        nvg.drawRoundedRect(
                3,
                sliderY.getFloat() + (tabRect.height - sliderHeight) / 2,
                SLIDER_WIDTH,
                sliderHeight,
                1.5f,
                GrapheneUI.THEME.THEME,
                true
        );
        nvg.restore();
    }

    public TabPanel getCurrentPanel() {
        return currentTab != null ? tabMap.get(currentTab) : null;
    }

    public TabPanel getLastPanel() {
        return lastTab != null ? tabMap.get(lastTab) : null;
    }

    private float getSliderTargetY() {
        return currentTab != null ? currentTab.getRectangle().y : 0;
    }

    public TabBar addTab(Tab tab, TabPanel panel) {
        tabMap.put(tab, panel);
        tabsOrder.add(tab);
        addChild(tab);
        return this;
    }


    @Override
    public void mouseMove(float x, float y) {
        super.mouseMove(x, y);
        x = x - getRectangle().x;
        y = y - getRectangle().y;
        float finalX = x;
        float finalY = y;
        if (getCurrentPanel() != null) {
            getCurrentPanel().mouseMove(finalX, finalY);
        }
    }

    @Override
    public void mousePress(float x, float y, int button) {
        super.mousePress(x, y, button);
        x = x - getRectangle().x;
        y = y - getRectangle().y;
        float finalX = x;
        float finalY = y;
        if (getCurrentPanel() != null) {
            getCurrentPanel().mousePress(finalX, finalY, button);
        }
    }

    @Override
    public void mouseRelease(float x, float y, int button) {
        super.mouseRelease(x, y, button);
        x = x - getRectangle().x;
        y = y - getRectangle().y;
        float finalX = x;
        float finalY = y;
        if (getCurrentPanel() != null) {
            getCurrentPanel().mouseRelease(finalX, finalY, button);
        }
    }

    @Override
    public void mouseDrag(float mouseX, float mouseY, float dragX, float dragY, int button) {
        super.mouseDrag(mouseX, mouseY, dragX, dragY, button);
        mouseX = mouseX - getRectangle().x;
        mouseY = mouseY - getRectangle().y;
        float finalX = mouseX;
        float finalY = mouseY;
        if (getCurrentPanel() != null) {
            getCurrentPanel().mouseDrag(finalX, finalY, dragX, dragY, button);
        }
    }

    @Override
    public void mouseScroll(float x, float y, double scrollX) {
        super.mouseScroll(x, y, scrollX);
        x = x - getRectangle().x;
        y = y - getRectangle().y;
        float finalX = x;
        float finalY = y;
        if (getCurrentPanel() != null) {
            getCurrentPanel().mouseScroll(finalX, finalY, scrollX);
        }
    }

    @Override
    public void keyPress(int keyCode, int scancode, int modifiers) {
        super.keyPress(keyCode, scancode, modifiers);
        if (getCurrentPanel() != null) {
            getCurrentPanel().keyPress(keyCode, scancode, modifiers);
        }
    }

    @Override
    public void keyRelease(int keyCode, int scancode, int modifiers) {
        super.keyRelease(keyCode, scancode, modifiers);
        if (getCurrentPanel() != null) {
            getCurrentPanel().keyRelease(keyCode, scancode, modifiers);
        }
    }

    @Override
    public void charTyped(char codePoint, int modifiers) {
        super.charTyped(codePoint, modifiers);
        if (getCurrentPanel() != null) {
            getCurrentPanel().charTyped(codePoint, modifiers);
        }
    }

    @Override
    public void renderTooltip(float delta) {
        super.renderTooltip(delta);
        if (getCurrentPanel() != null) {
            getCurrentPanel().renderTooltip(delta);
        }
    }
}