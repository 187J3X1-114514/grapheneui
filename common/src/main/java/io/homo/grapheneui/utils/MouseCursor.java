package io.homo.grapheneui.utils;

import net.minecraft.client.Minecraft;
import org.lwjgl.glfw.GLFW;

public class MouseCursor {
    public static final MouseCursor HAND = new MouseCursor(GLFW.GLFW_HAND_CURSOR);
    public static final MouseCursor CROSSHAIR = new MouseCursor(GLFW.GLFW_CROSSHAIR_CURSOR);
    public static final MouseCursor IBEAM = new MouseCursor(GLFW.GLFW_IBEAM_CURSOR);
    public static final MouseCursor NOT_ALLOWED = new MouseCursor(GLFW.GLFW_NOT_ALLOWED_CURSOR);
    public static final MouseCursor ARROW = new MouseCursor(GLFW.GLFW_ARROW_CURSOR);

    public final int id;
    private long glfwCursor = -1;

    private MouseCursor(int id) {
        this.id = id;
    }

    public void use() {
        if (glfwCursor == -1 || glfwCursor == 0) {
            glfwCursor = GLFW.glfwCreateStandardCursor(id);
        } else {
            GLFW.glfwSetCursor(
                    Minecraft.getInstance().getWindow().getWindow(),
                    glfwCursor
            );
        }
    }
}
