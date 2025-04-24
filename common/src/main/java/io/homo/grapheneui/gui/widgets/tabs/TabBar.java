package io.homo.grapheneui.gui.widgets.tabs;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.animator.Easing;
import io.homo.grapheneui.animator.NumberAnimator;
import io.homo.grapheneui.core.Transform;
import io.homo.grapheneui.gui.widgets.containers.Container;
import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.utils.Color;
import io.homo.grapheneui.utils.ColorUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TabBar extends Container {
    private static float TAB_SPACING = 4;
    private static float SLIDER_WIDTH = 3;
    private static float SLIDER_HEIGHT_RATIO = 0.45f;
    private final NumberAnimator sliderY = NumberAnimator.create();
    public Map<Tab, TabPanel> tabMap = new HashMap<>();
    protected Tab currentTab;
    private float targetSliderY;

    public TabBar() {
        panelRenderer.shadow(false).panelColor(
                ColorUtil.mix(
                        GrapheneUI.THEME.INTERFACE_BG_C,
                        Color.rgb(0xFFFFFF),
                        0.95
                )
        );
    }

    public void setCurrentTab(Tab currentTab) {
        if (this.currentTab != null && this.currentTab != currentTab) {
            this.currentTab.getPanelRenderer().setChecked(false);
            this.currentTab.getPanelRenderer().setHover(false);
        }
        if (this.currentTab != currentTab) {
            currentTab.getPanelRenderer().setChecked(true);
            currentTab.getPanelRenderer().setHover(false);
            this.currentTab = currentTab;
            sliderY.animateTo(getSliderTargetY(), 250)
                    .ease(Easing.EASE_OUT_QUINT);
        }
    }

    @Override
    public void render(float mouseX, float mouseY, float delta) {
        nvg.save();
        updateCurrentTab();
        arrangeTabs();
        super.render(mouseX, mouseY, delta);
        renderSlider(delta);
        if (getCurrentPanel() != null) {
            TabPanel tabPanel = getCurrentPanel();
            tabPanel.setRectangle(new Rectangle(
                    rectangle.x + 47,
                    rectangle.y + 2,
                    rectangle.width - 47,
                    rectangle.height - 2
            ));
            tabPanel.render(mouseX, mouseY, delta);
        }
        nvg.restore();
    }

    private void updateCurrentTab() {
        if (currentTab == null && !tabMap.isEmpty()) {

            setCurrentTab(tabMap.keySet().iterator().next());
        }
    }

    private void arrangeTabs() {
        ArrayList<Tab> leftTabs = new ArrayList<>();
        ArrayList<Tab> centerTabs = new ArrayList<>();
        ArrayList<Tab> rightTabs = new ArrayList<>();

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

    private void layoutVerticalColumn(ArrayList<Tab> tabs, float yPos) {
        if (tabs.isEmpty()) return;
        float currentY = yPos;

        for (Tab tab : tabs) {
            tab.setRectangle(new Rectangle(
                    3,
                    (int) currentY,
                    42,
                    42
            ));
            currentY += 42 + TAB_SPACING;
        }
    }

    private void renderSlider(float delta) {
        if (currentTab == null) return;
        sliderY.update();
        Rectangle tabRect = currentTab.getRectangle();
        float sliderHeight = 42 * SLIDER_HEIGHT_RATIO;
        float sliderYPos = (float) (sliderY.getValue() + (tabRect.height - sliderHeight) / 2);
        nvg.save();
        nvg.transform(Transform.fromPosition(rectangle.x, rectangle.y));
        nvg.drawRoundedRect(
                3,
                sliderYPos,
                4,
                sliderHeight,
                1.5f,
                GrapheneUI.THEME.THEME,
                true
        );
        nvg.restore();
    }

    public TabPanel getCurrentPanel() {
        if (currentTab != null) {
            if (tabMap.get(currentTab) != null) {
                return tabMap.get(currentTab);
            }
        }
        return null;
    }

    private float getSliderTargetY() {
        return currentTab != null ? currentTab.getRectangle().y : 0;
    }

    public TabBar addTab(Tab tab, TabPanel panel) {
        tabMap.put(tab, panel);
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