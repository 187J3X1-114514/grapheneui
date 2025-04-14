package io.homo.grapheneui.nanovg;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

import static org.lwjgl.nanovg.NanoVG.nvgCreateFont;

public class NanoVGFont {
    public int id = -1;
    public String name;
    public String path;

    public NanoVGFont(String name, String path) {
        this.name = name;
        this.path = path;
    }

    public void load() {
        try {
            Path tempFile = Files.createTempFile("grapheneui-font-" + UUID.randomUUID(), ".ttf");
            InputStream fontStream = getClass().getResourceAsStream(path);
            if (fontStream == null) {
                throw new RuntimeException("字体路径不存在: " + path);
            }
            Files.copy(fontStream, tempFile, StandardCopyOption.REPLACE_EXISTING);
            fontStream.close();
            id = nvgCreateFont(NanoVG.context.contextPtr, name, tempFile.toString());
            tempFile.toFile().delete();
        } catch (Exception e) {
            throw new RuntimeException("字体加载失败: " + name, e);
        }
    }
}