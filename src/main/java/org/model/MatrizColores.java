package org.model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class MatrizColores {

    public static BufferedImage aplicarEscalaGrises(BufferedImage original) {
        float[][] matrizGrises = {
                {0.299f, 0.587f, 0.114f, 0.0f},
                {0.299f, 0.587f, 0.114f, 0.0f},
                {0.299f, 0.587f, 0.114f, 0.0f},
                {0.0f, 0.0f, 0.0f, 1.0f},
        };
        return aplicarMatriz(original, matrizGrises);
    }

    public static BufferedImage aplicarSepia(BufferedImage original) {
        float[][] matrizSepia = {
                {0.393f, 0.769f, 0.189f, 0.0f},
                {0.349f, 0.686f, 0.168f, 0.0f},
                {0.272f, 0.534f, 0.131f, 0.0f},
                {0.0f, 0.0f, 0.0f, 1.0f},
        };
        return aplicarMatriz(original, matrizSepia);
    }

    private static BufferedImage aplicarMatriz(BufferedImage original, float[][] matriz) {
        int ancho = original.getWidth();
        int alto = original.getHeight();

        int r, g, b, pixel, pixelNuevo;
        int r1, g1, b1;

        BufferedImage buffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                pixel = original.getRGB(x, y);

                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = pixel & 0xFF;

                r1 = (int) (matriz[0][0] * r + matriz[0][1] * g + matriz[0][2] * b);
                g1 = (int) (matriz[1][0] * r + matriz[1][1] * g + matriz[1][2] * b);
                b1 = (int) (matriz[2][0] * r + matriz[2][1] * g + matriz[2][2] * b);

                r1 = Math.clamp(r1, 0, 255);
                g1 = Math.clamp(g1, 0, 255);
                b1 = Math.clamp(b1, 0, 255);

                pixelNuevo = (r1 << 16) | (g1 << 8) | (b1);
                buffer.setRGB(x, y, pixelNuevo);
            }
        }
        return buffer;
    }
}
