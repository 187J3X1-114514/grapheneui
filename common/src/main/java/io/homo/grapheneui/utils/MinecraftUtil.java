package io.homo.grapheneui.utils;

import io.homo.grapheneui.impl.Vec2;
import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class MinecraftUtil {
    public static Vec2 getScreenSize() {
        int[] w = new int[1];
        int[] h = new int[1];
        GLFW.glfwGetWindowSize(Minecraft.getInstance().getWindow().getWindow(), w, h);
        return new Vec2(Math.max(1, w[0]), Math.max(1, h[0]));
    }
}
