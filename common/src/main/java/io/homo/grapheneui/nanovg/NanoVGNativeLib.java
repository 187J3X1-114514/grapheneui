package io.homo.grapheneui.nanovg;

import net.minecraft.client.Minecraft;
import org.lwjgl.system.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class NanoVGNativeLib {
    private static final List<String> LIB_CANDIDATES = Arrays.asList(
            "libGrapheneUILib.dll",
            "GrapheneUILib.dll",
            "libGrapheneUILib.so",
            "libGrapheneUILib.dylib"
    );

    private static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }

    public static void init() {
        Stream.of(Configuration.SHARED_LIBRARY_EXTRACT_PATH.get(),
                        Configuration.LIBRARY_PATH.get(),
                        System.getProperty("java.library.path")
                )
                .filter(path -> path != null && !path.isEmpty())
                .findFirst()
                .ifPresent(path -> {
                    if (!extractAndLoad(Path.of(path), "lwjgl_nanovg.dll", false)) {
                        throw new RuntimeException("库解压失败! " + path);
                    }
                });
        String libPath = null;
        boolean loaded = false;
        for (String libName : LIB_CANDIDATES) {
            if (tryLoadFromLibraryPath(libName)) {
                loaded = true;
                break;
            }
        }
        if (!loaded) {
            Path gameDir = Minecraft.getInstance().gameDirectory.toPath();
            for (String libName : LIB_CANDIDATES) {
                if (extractAndLoad(gameDir, libName, true)) {
                    loaded = true;
                    libPath = String.valueOf(Path.of(String.valueOf(gameDir), libName));
                    break;
                }
            }
        }

        if (!loaded) {
            throw new RuntimeException("Failed to load native library after all attempts");
        }
        NanoVGNative.load(libPath);
    }

    private static boolean tryLoadFromLibraryPath(String libName) {
        try {
            System.loadLibrary(libName.replaceFirst("[.][^.]*$", "")); // 移除扩展名
            return true;
        } catch (UnsatisfiedLinkError e) {
            return false;
        }
    }

    private static boolean extractAndLoad(Path targetDir, String libName, boolean load) {
        try {
            Files.createDirectories(targetDir);
            String resourcePath = "libs/" + libName;
            try (InputStream in = getResourceStream(resourcePath)) {
                if (in == null) return false;

                Path outputPath = targetDir.resolve(libName);
                Files.copy(in, outputPath, StandardCopyOption.REPLACE_EXISTING);

                if (!isWindows()) {
                    setExecutablePermission(outputPath);
                }

                if (load) System.load(outputPath.toString());
                return true;
            }
        } catch (IOException | UnsatisfiedLinkError e) {
            return false;
        }
    }

    private static InputStream getResourceStream(String resourcePath) {
        InputStream in = NanoVGNativeLib.class.getClassLoader().getResourceAsStream(resourcePath);
        if (in == null) {
            in = NanoVGNativeLib.class.getResourceAsStream("/" + resourcePath);
        }
        return in;
    }

    private static void setExecutablePermission(Path path) throws IOException {
        if (!path.toFile().setExecutable(true)) {
            throw new IOException("Failed to set executable permission for: " + path);
        }
        if (!path.toFile().setReadable(true)) {
            throw new IOException("Failed to set readable permission for: " + path);
        }
    }
}