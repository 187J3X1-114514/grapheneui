package io.homo.grapheneui.fabric;

import com.mojang.blaze3d.systems.RenderSystem;
import io.homo.grapheneui.GrapheneUIMod;
import net.fabricmc.api.ClientModInitializer;

public final class GrapheneUIFabricClient implements ClientModInitializer {
    public static GrapheneUIMod mod;

    @Override
    public void onInitializeClient() {
        RenderSystem.recordRenderCall(() -> {
            mod = new GrapheneUIMod();
            mod.init();
        });
    }
}
