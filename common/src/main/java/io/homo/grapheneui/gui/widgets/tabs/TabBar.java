package io.homo.grapheneui.gui.widgets.tabs;

import io.homo.grapheneui.GrapheneUI;
import io.homo.grapheneui.animator.Easing;
import io.homo.grapheneui.animator.NumberAnimator;
import io.homo.grapheneui.core.Transform;
import io.homo.grapheneui.gui.widgets.containers.Container;
import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.utils.Color;

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
        panelRenderer.shadow(false).panelColor(Color.rgba(0, 0, 0, 0));
    }

    public void setCurrentTab(Tab currentTab) {
        if (this.currentTab != null) {
            this.currentTab.getPanelRenderer().setChecked(false);
        }
        if (this.currentTab != currentTab) {
            currentTab.getPanelRenderer().setChecked(true);
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

    private float getSliderTargetY() {
        return currentTab != null ? currentTab.getRectangle().y : 0;
    }

    public TabBar addTab(Tab tab, TabPanel panel) {
        tabMap.put(tab, panel);
        addChild(tab);
        return this;
    }
}