package io.homo.grapheneui.opengl;

import org.apache.commons.lang3.StringUtils;
import org.lwjgl.opengl.KHRDebug;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.lwjgl.opengl.GL30.*;

public class GlUtils {
    private static Set<String> glExtensions;
    private static int[] glVersion;

    public static int[] getGlVersion() {
        return glVersion;
    }

    public static Set<String> getGlExtensions() {
        return glExtensions;
    }

    public static void init() {
        glVersion = detectGLVersion();
        glExtensions = detectGLExtensions();
    }

    private static int[] detectGLVersion() {
        int[] version = new int[2];
        int major = glGetInteger(GL_MAJOR_VERSION);
        int minor = glGetInteger(GL_MINOR_VERSION);
        version[0] = major;
        version[1] = minor;
        return version;
    }

    private static Set<String> detectGLExtensions() {
        int count = glGetInteger(GL_NUM_EXTENSIONS);
        return Collections.unmodifiableSet(
                IntStream.range(0, count)
                        .mapToObj(i -> glGetStringi(GL_EXTENSIONS, i))
                        .collect(Collectors.toCollection(() ->
                                new TreeSet<>(String.CASE_INSENSITIVE_ORDER)))
        );
    }

    public static void glSafeObjectLabel(int type, int id, String label) {
        if (glExtensions.contains("GL_KHR_debug")) {
            KHRDebug.glObjectLabel(type, id, StringUtils.abbreviate(label, 255));
        }
    }
}
