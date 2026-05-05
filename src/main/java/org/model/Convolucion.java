package org.model;

import java.awt.image.BufferedImage;

public class Convolucion {

    // --- Filtro de Desenfoque (Blur) con Matriz 3x3 ---
    public static BufferedImage convolucionBi(BufferedImage original) {
        int ancho = original.getWidth();
        int alto = original.getHeight();
        BufferedImage buffer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        // Matriz de convolución 3x3 (1/9)
        float[][] matrizConv = {
                {1f / 9, 1f / 9, 1f / 9},
                {1f / 9, 1f / 9, 1f / 9},
                {1f / 9, 1f / 9, 1f / 9}
        };

        // Recorremos la imagen evitando los bordes (1 píxel de margen)
        for (int y = 1; y < alto - 1; y++) {
            for (int x = 1; x < ancho - 1; x++) {
                float sumaR = 0, sumaG = 0, sumaB = 0;

                // Aplicar la matriz 3x3 a los vecinos del píxel actual
                for (int i = -1; i < 2; i++) {
                    for (int j = -1; j < 2; j++) {
                        int pixel = original.getRGB(x + j, y + i);

                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = pixel & 0xFF;

                        sumaR += r * matrizConv[i + 1][j + 1];
                        sumaG += g * matrizConv[i + 1][j + 1];
                        sumaB += b * matrizConv[i + 1][j + 1];
                    }
                }

                // Math.clamp está disponible desde Java 21 (Perfecto para tu JDK 25)
                int rF = Math.clamp(Math.round(sumaR), 0, 255);
                int gF = Math.clamp(Math.round(sumaG), 0, 255);
                int bF = Math.clamp(Math.round(sumaB), 0, 255);

                int pixelNuevo = (rF << 16) | (gF << 8) | bF;
                buffer2.setRGB(x, y, pixelNuevo);
            }
        }
        return buffer2;
    }

    // --- Efecto Acuarela / Desenfoque Fuerte con Matriz 9x9 ---
    public static BufferedImage convolucion9x9(BufferedImage original) {
        int ancho = original.getWidth();
        int alto = original.getHeight();
        BufferedImage buffer2 = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        // Generar matriz 9x9 dinámicamente (1/81)
        float[][] matrizConv = new float[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                matrizConv[i][j] = 1f / 81f;
            }
        }

        // Evitamos 4 píxeles de margen debido al radio de la matriz 9x9
        for (int y = 4; y < alto - 4; y++) {
            for (int x = 4; x < ancho - 4; x++) {
                float sumaR = 0, sumaG = 0, sumaB = 0;

                // Aplicar la matriz 9x9
                for (int i = -4; i <= 4; i++) {
                    for (int j = -4; j <= 4; j++) {
                        int pixel = original.getRGB(x + j, y + i);

                        int r = (pixel >> 16) & 0xFF;
                        int g = (pixel >> 8) & 0xFF;
                        int b = pixel & 0xFF;

                        sumaR += r * matrizConv[i + 4][j + 4];
                        sumaG += g * matrizConv[i + 4][j + 4];
                        sumaB += b * matrizConv[i + 4][j + 4];
                    }
                }

                int rF = Math.clamp(Math.round(sumaR), 0, 255);
                int gF = Math.clamp(Math.round(sumaG), 0, 255);
                int bF = Math.clamp(Math.round(sumaB), 0, 255);

                int pixelNuevo = (rF << 16) | (gF << 8) | bF;
                buffer2.setRGB(x, y, pixelNuevo);
            }
        }
        return buffer2;
    }
}