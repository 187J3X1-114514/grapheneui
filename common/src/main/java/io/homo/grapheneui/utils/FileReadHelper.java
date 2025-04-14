package io.homo.grapheneui.utils;


import io.homo.grapheneui.GrapheneUIMod;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import static org.lwjgl.BufferUtils.createByteBuffer;

public class FileReadHelper {
    public static ArrayList<String> readText(String path) {
        InputStream inputStream = FileReadHelper.class.getResourceAsStream(path);
        ArrayList<String> lines = new ArrayList<>();
        if (inputStream != null) {
            try (InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                 BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (IOException e) {
                GrapheneUIMod.LOGGER.error(e.toString());
            } finally {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    GrapheneUIMod.LOGGER.error(e.toString());
                }
            }
        }
        return lines;
    }

    public static ByteBuffer readResourceToBuffer(String path) {
        try (InputStream is = FileReadHelper.class.getResourceAsStream(path)) {
            if (is == null) {
                GrapheneUIMod.LOGGER.error("Resource not found: " + path);
                return ByteBuffer.allocate(0);
            }
            ReadableByteChannel channel = Channels.newChannel(is);
            ByteBuffer buffer = ByteBuffer.allocateDirect(8192); // 初始8KB
            while (channel.read(buffer) != -1) {
                if (buffer.remaining() == 0) {
                    buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                }
            }
            buffer.flip();
            return buffer;
        } catch (IOException e) {
            GrapheneUIMod.LOGGER.error("Failed to read resource: " + path, e);
            return ByteBuffer.allocate(0);
        }
    }

    public static ByteBuffer readFileToBuffer(String path) {
        try (InputStream is = new FileInputStream(path)) {
            if (is == null) {
                GrapheneUIMod.LOGGER.error("Resource not found: " + path);
                return ByteBuffer.allocate(0);
            }
            ReadableByteChannel channel = Channels.newChannel(is);
            ByteBuffer buffer = ByteBuffer.allocateDirect(8192); // 初始8KB
            while (channel.read(buffer) != -1) {
                if (buffer.remaining() == 0) {
                    buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                }
            }
            buffer.flip();
            return buffer;
        } catch (IOException e) {
            GrapheneUIMod.LOGGER.error("Failed to read resource: " + path, e);
            return ByteBuffer.allocate(0);
        }
    }

    public static ByteBuffer ioResourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer buffer;

        Path path = Paths.get(resource);
        if (Files.isReadable(path)) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                buffer = createByteBuffer((int) fc.size() + 1);
                while (fc.read(buffer) != -1) ;
            }
        } else {
            try (
                    InputStream source = FileReadHelper.class.getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)) {
                buffer = createByteBuffer(bufferSize);

                while (true) {
                    int bytes = rbc.read(buffer);
                    if (bytes == -1) {
                        break;
                    }
                    if (buffer.remaining() == 0) {
                        buffer = resizeBuffer(buffer, buffer.capacity() * 2);
                    }
                }
            }
        }

        buffer.flip();
        return buffer;
    }

    private static ByteBuffer resizeBuffer(ByteBuffer original, int newSize) {
        ByteBuffer newBuffer = ByteBuffer.allocateDirect(newSize);
        original.flip();
        newBuffer.put(original);
        return newBuffer;
    }

    public static ByteBuffer readResourceToBufferSimple(String path) {

        try (InputStream is = FileReadHelper.class.getResourceAsStream(path)) {
            if (is == null) {
                GrapheneUIMod.LOGGER.error("Resource not found: " + path);
                return ByteBuffer.allocate(0);
            }

            byte[] bytes = new byte[is.available()];
            is.read(bytes);
            return ByteBuffer.wrap(bytes);
        } catch (IOException e) {
            GrapheneUIMod.LOGGER.error("Failed to read resource: " + path, e);
            return ByteBuffer.allocate(0);
        }
    }

    public static byte[] byteBuffer2ByteArray(ByteBuffer buffer) {
        int len = buffer.limit() - buffer.position();
        byte[] bytes = new byte[len];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = buffer.get();

        }
        return bytes;
    }
}
