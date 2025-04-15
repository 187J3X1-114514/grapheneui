package io.homo.grapheneui.nanovg;

import java.nio.file.Path;

public class NanoVGNative {

    public static NanoVGNative INSTANCE;

    private NanoVGNative(Path path) {
        System.load(String.valueOf(path));
    }

    public native static long unsafeCalloc(long num, long size);

    public native static void unsafeFree(long address);
}
