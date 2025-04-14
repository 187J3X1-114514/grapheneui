package io.homo.grapheneui.utils;


import io.homo.grapheneui.core.icon.FluentIconPack;

import java.io.*;
import java.util.zip.GZIPInputStream;

public abstract class GZipUtils {

    public static final int BUFFER = 1024;
    public static final String EXT = ".gz";

    public static byte[] decompress(byte[] data) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        decompress(bais, baos);
        data = baos.toByteArray();
        baos.flush();
        baos.close();
        bais.close();
        return data;
    }

    public static void decompress(File file) throws Exception {
        decompress(file, true);
    }

    public static void decompress(File file, boolean delete) throws Exception {
        FileInputStream fis = new FileInputStream(file);
        FileOutputStream fos = new FileOutputStream(file.getPath().replace(EXT,
                ""));
        decompress(fis, fos);
        fis.close();
        fos.flush();
        fos.close();
        if (delete) {
            file.delete();
        }
    }

    public static void decompress(InputStream is, OutputStream os)
            throws Exception {

        GZIPInputStream gis = new GZIPInputStream(is);
        int count;
        byte data[] = new byte[BUFFER];
        while ((count = gis.read(data, 0, BUFFER)) != -1) {
            os.write(data, 0, count);
        }
        gis.close();
    }

    public static void decompress(String path) throws Exception {
        decompress(path, true);
    }

    public static void decompress(String path, boolean delete) throws Exception {
        File file = new File(path);
        decompress(file, delete);
    }


    public static void main(String[] args) throws Exception {
        FluentIconPack.load();
    }
}