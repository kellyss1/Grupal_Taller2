package com.imagen.procesamientoimagen;

import org.lwjgl.BufferUtils;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class TextureLoader {

    public static int loadTexture(BufferedImage image) {

        try {

            int width = image.getWidth();
            int height = image.getHeight();

            int[] pixels =
                    new int[width * height];

            image.getRGB(
                    0,
                    0,
                    width,
                    height,
                    pixels,
                    0,
                    width);

            ByteBuffer buffer =
                    BufferUtils.createByteBuffer(
                            width * height * 4);

            for (int y = 0; y < height; y++) {

                for (int x = 0; x < width; x++) {

                    int pixel = pixels[y * width + x];

                    buffer.put((byte) ((pixel >> 16) & 0xFF));
                    buffer.put((byte) ((pixel >> 8) & 0xFF));
                    buffer.put((byte) (pixel & 0xFF));
                    buffer.put((byte) ((pixel >> 24) & 0xFF));
                }
            }

            buffer.flip();

            int textureID =
                    glGenTextures();

            glBindTexture(
                    GL_TEXTURE_2D,
                    textureID);

            glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MIN_FILTER,
                    GL_LINEAR);

            glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MAG_FILTER,
                    GL_LINEAR);

            glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA,
                    width,
                    height,
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    buffer);

            glBindTexture(
                    GL_TEXTURE_2D,
                    0);

            return textureID;

        } catch (Exception e) {

            e.printStackTrace();
        }

        return -1;
    }
}