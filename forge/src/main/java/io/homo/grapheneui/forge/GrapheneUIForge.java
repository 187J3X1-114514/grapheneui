package io.homo.grapheneui.forge;

import io.homo.grapheneui.GrapheneUI;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;

import net.minecraftforge.network.NetworkConstants;
import net.minecraftforge.fml.IExtensionPoint;

@Mod(value = GrapheneUI.MOD_ID)
public final class GrapheneUIForge {
    public static GrapheneUI mod;

    public GrapheneUIForge() {
        ModLoadingContext.get().registerExtensionPoint(IExtensionPoint.DisplayTest.class, () -> new IExtensionPoint.DisplayTest(() -> NetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        RenderSystem.recordRenderCall(()->{
            mod = new GrapheneUI();
            mod.init();
        });
    }
}
