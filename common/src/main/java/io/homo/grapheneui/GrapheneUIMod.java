package io.homo.grapheneui;

import com.mojang.blaze3d.systems.RenderSystem;
import io.homo.grapheneui.core.icon.FluentIconPack;
import io.homo.grapheneui.nanovg.NanoVG;
import io.homo.grapheneui.nanovg.NanoVGNativeLib;
import io.homo.grapheneui.opengl.GlUtils;
import net.minecraft.client.Minecraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public final class GrapheneUIMod {
    public static final String MOD_ID = "grapheneui";
    public static final Logger LOGGER = LoggerFactory.getLogger("GrapheneUI");
    private static final Minecraft minecraft = Minecraft.getInstance();
    public static boolean isInit;
    private static GrapheneUIMod instance;

    public GrapheneUIMod() {

    }

    public static GrapheneUIMod getInstance() {
        return instance;
    }

    public void init() {
        if (isInit)
            return;
        RenderSystem.assertOnRenderThread();
        instance = this;
        NanoVGNativeLib.init();
        GlUtils.init();
        NanoVG.init();
        FluentIconPack.load();
        isInit = true;
    }
}
