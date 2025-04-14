package io.homo.grapheneui.neoforge;

import com.mojang.blaze3d.systems.RenderSystem;
import io.homo.grapheneui.GrapheneUIMod;
import io.homo.grapheneui.gui.DemoScreen;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;


@Mod(value = GrapheneUIMod.MOD_ID, dist = Dist.CLIENT)
public final class GrapheneUINeoForge {
    public static GrapheneUIMod mod;

    public GrapheneUINeoForge(ModContainer container) {
        ModLoadingContext.get().registerExtensionPoint(IConfigScreenFactory.class, () -> (mc, screen) -> new DemoScreen());

        RenderSystem.recordRenderCall(() -> {
            mod = new GrapheneUIMod();
            mod.init();
        });
    }
}