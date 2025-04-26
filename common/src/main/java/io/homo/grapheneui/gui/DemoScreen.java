package io.homo.grapheneui.gui;

import io.homo.grapheneui.core.icon.FluentIconPack;
import io.homo.grapheneui.core.icon.Icon;
import io.homo.grapheneui.core.renderer.TooltipRenderer;
import io.homo.grapheneui.gui.widgets.buttons.builder.ButtonBuilder;
import io.homo.grapheneui.gui.widgets.sliders.builder.SliderBuilder;
import io.homo.grapheneui.gui.widgets.switchs.builder.SwitchBuilder;
import io.homo.grapheneui.gui.widgets.tabs.Tab;
import io.homo.grapheneui.gui.widgets.tabs.TabBar;
import io.homo.grapheneui.gui.widgets.tabs.TabPanel;
import io.homo.grapheneui.impl.Rectangle;
import io.homo.grapheneui.utils.MinecraftUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class DemoScreen extends NanoVGScreen<DemoScreen> {
    protected TabPanel container;
    protected TabPanel container2;

    protected Tab tab;
    protected TabBar tabBar;


    public DemoScreen() {
        super(Component.empty());
        setTransparent(true);
    }

    @Override
    protected void buildWidgets() {
        container = new TabPanel();
        container.setRectangle(new Rectangle(
                10,
                10,
                400,
                300
        ));
        container2 = new TabPanel();
        container2.setRectangle(new Rectangle(
                10,
                10,
                400,
                300
        ));

        container.addChild(
                ButtonBuilder.flatButton()
                        .withRectangle(new Rectangle(90, 10, 70, 40))
                        .withRoundedSize(8f)
                        .withText("扁平按钮")
                        .withTooltip("345534534534\n5645645646")
                        .withVisibility(true)
                        .build()
        );
        container.addChild(
                ButtonBuilder.pressButton()
                        .withRectangle(new Rectangle(170, 10, 70, 40))
                        .withRoundedSize(8f)
                        .withText("按压按钮")
                        .withTooltip("345534534534\n5645645646345534534534\n5645645646")
                        .withTooltipPosition(TooltipRenderer.RIGHT)
                        .withVisibility(true)
                        .build()
        );
        container.addChild(
                ButtonBuilder.iconButton()
                        .withRectangle(new Rectangle(250, 10, 70, 40))
                        .withRoundedSize(8f)
                        .withIcon(FluentIconPack.regular().get("ic_fluent_alert_off_regular"))
                        .withTooltip("图标按钮")
                        .withTooltipPosition(TooltipRenderer.RIGHT)
                        .withVisibility(true)
                        .build()
        );
        container.addChild(
                new SliderBuilder()
                        .withRectangle(new Rectangle(10, 60, 250, 20))
                        .withTooltip("滑块")
                        .withTooltipPosition(TooltipRenderer.RIGHT)
                        .withMax(10)
                        .withMin(0)
                        .withStep(1.0f)
                        .withValue(2)
                        .build()
        );
        container.addChild(
                new SwitchBuilder()
                        .withRectangle(new Rectangle(10, 100, 40, 20))
                        .withTooltip("开关")
                        .withTooltipPosition(TooltipRenderer.RIGHT)
                        .withChecked(false)
                        .withZoom(false)
                        .build()
        );
        container.addChild(
                new SwitchBuilder()
                        .withRectangle(new Rectangle(60, 100, 40, 20))
                        .withTooltip("开关")
                        .withTooltipPosition(TooltipRenderer.RIGHT)
                        .withChecked(true)
                        .withZoom(false)
                        .build()
        );
        container.addChild(
                new SwitchBuilder()
                        .withRectangle(new Rectangle(110, 100, 40, 20))
                        .withTooltip("开关")
                        .withTooltipPosition(TooltipRenderer.RIGHT)
                        .withChecked(false)
                        .withZoom(true)
                        .build()
        );
        container2.addChild(
                new SwitchBuilder()
                        .withRectangle(new Rectangle(60, 100, 40, 20))
                        .withTooltip("开关")
                        .withTooltipPosition(TooltipRenderer.RIGHT)
                        .withChecked(true)
                        .withZoom(false)
                        .build()
        );
        container2.addChild(
                new SwitchBuilder()
                        .withRectangle(new Rectangle(110, 100, 40, 20))
                        .withTooltip("开关")
                        .withTooltipPosition(TooltipRenderer.RIGHT)
                        .withChecked(false)
                        .withZoom(true)
                        .build()
        );
        this.tab = new Tab().setTabIcon((Icon) FluentIconPack.regular().values().toArray()[0]);

        tabBar = new TabBar();
        tabBar.setRectangle(
                new Rectangle(
                        0,
                        0,
                        (int) MinecraftUtil.getScreenSize().x,
                        (int) MinecraftUtil.getScreenSize().y
                )
        );
        tabBar.addTab(tab, container);
        tabBar.addTab(
                new Tab()
                        .setTabText("fhfghfghfgh")
                        .setTabIcon((Icon) FluentIconPack.regular().values().toArray()[20]),
                container2
        );
        tabBar.addTab(
                new Tab()
                        .setTabText("sdfsdfsdfsdf")
                        .setTabIcon((Icon) FluentIconPack.regular().values().toArray()[8]),
                container
        );
        tabBar.addTab(
                new Tab()
                        .setTabText("345634534534")
                        .setTabIcon((Icon) FluentIconPack.regular().values().toArray()[1]),
                container2
        );
        tabBar.addTab(
                new Tab()
                        .setTabText("dfhfgjfj568")
                        .setTabIcon((Icon) FluentIconPack.regular().values().toArray()[40]),
                container
        );
        addWidget(tabBar);
        addEventListener("resize", (screen) -> {
            tabBar.setRectangle(
                    new Rectangle(
                            0,
                            0,
                            (int) MinecraftUtil.getScreenSize().x,
                            (int) MinecraftUtil.getScreenSize().y
                    )
            );
        });
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void draw(int mouseX, int mouseY, float delta) {
        super.draw(mouseX, mouseY, delta);
    }

    @Override
    public void drawBefore(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        super.drawBefore(guiGraphics, mouseX, mouseY, delta);
    }
}
