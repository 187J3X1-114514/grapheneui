#include "io_homo_grapheneui_nanovg_NanoVGNative.h"
#include <cstdlib>

JNIEXPORT jlong JNICALL Java_io_homo_grapheneui_nanovg_NanoVGNative_unsafeCalloc(JNIEnv *, jclass, jlong num, jlong size)
{
    void *ptr = calloc(num, size);
    return reinterpret_cast<jlong>(ptr);
};
JNIEXPORT void JNICALL Java_io_homo_grapheneui_nanovg_NanoVGNative_unsafeFree(JNIEnv *, jclass, jlong address)
{
    void *ptr = reinterpret_cast<void *>(address);
    free(ptr);
};