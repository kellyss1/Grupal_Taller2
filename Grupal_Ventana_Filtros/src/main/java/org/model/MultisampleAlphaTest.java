package org.model;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Arrays;

public class MultisampleAlphaTest {
    private final int width;
    private final int height;
    private final BufferedImage colorBuffer;
    private final int[] pixelArray;
    private final float[] depthBuffer;
    private float alphaThreshold = 0.4f;

    public MultisampleAlphaTest(int width, int height) {
        this.width = width;
        this.height = height;
        this.colorBuffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        this.pixelArray = ((DataBufferInt) colorBuffer.getRaster().getDataBuffer()).getData();
        this.depthBuffer = new float[width * height];
        clearBuffers();
    }

    public void clearBuffers() {
        Arrays.fill(pixelArray, 0x00000000); // Fondo transparente
        Arrays.fill(depthBuffer, 1.0f);
    }

    public static class SoftwareFragment {
        public int x, y;
        public float depth;
        public float r, g, b, a;

        public SoftwareFragment(int x, int y, float depth, float r, float g, float b, float a) {
            this.x = x;
            this.y = y;
            this.depth = depth;
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
        }
    }

    public void processFragment(SoftwareFragment fragment) {
        if (fragment.x < 0 || fragment.x >= width || fragment.y < 0 || fragment.y >= height) {
            return;
        }

        // --- ALPHA TEST ---
        if (fragment.a < alphaThreshold) {
            return; // Se descarta el píxel si es más transparente que el umbral
        }

        int index = fragment.y * width + fragment.x;

        // --- DEPTH TEST ---
        if (fragment.depth <= depthBuffer[index]) {
            depthBuffer[index] = fragment.depth;

            int a = Math.max(0, Math.min(255, Math.round(fragment.a * 255.0f)));
            int r = Math.max(0, Math.min(255, Math.round(fragment.r * 255.0f)));
            int g = Math.max(0, Math.min(255, Math.round(fragment.g * 255.0f)));
            int b = Math.max(0, Math.min(255, Math.round(fragment.b * 255.0f)));

            int packedColor = (a << 24) | (r << 16) | (g << 8) | b;
            pixelArray[index] = packedColor;
        }
    }

    public BufferedImage generarImagen() {
        clearBuffers();
        int h = width / 2;
        int k = height / 2;
        float rMax = Math.min(width, height) / 4.0f;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                float distancia = (float) Math.sqrt(Math.pow(x - h, 2) + Math.pow(y - k, 2));

                if (distancia <= rMax) {
                    float alpha = 1.0f - (distancia / rMax);
                    SoftwareFragment fragment = new SoftwareFragment(x, y, 0.5f, 1.0f, 0.0f, 1.0f, alpha);
                    processFragment(fragment);
                }
            }
        }
        return colorBuffer;
    }

    public void setAlphaThreshold(float alphaThreshold) {
        this.alphaThreshold = alphaThreshold;
    }

    public float getAlphaThreshold() {
        return alphaThreshold;
    }
}
