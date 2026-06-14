package org.model;

import java.awt.image.BufferedImage;

public class FiltrosRetro {

    // 1. Posterización (Efecto Retro N Colores)
    public static BufferedImage aplicarPosterizacion(BufferedImage original, int N) {
        int ancho = original.getWidth();
        int alto = original.getHeight();
        BufferedImage nuevoBuffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        // Fórmula para calcular la distancia de los saltos [cite: 2028]
        float salto = 255.0f / (N - 1);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = original.getRGB(x, y);
                int a = (pixel >> 24) & 0xFF;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                // Convertir a porcentaje, multiplicar por (N-1), redondear y volver a la escala [cite: 2094, 2096]
                r = (int) (Math.round((r / 255.0f) * (N - 1)) * salto);
                g = (int) (Math.round((g / 255.0f) * (N - 1)) * salto);
                b = (int) (Math.round((b / 255.0f) * (N - 1)) * salto);

                int pixelNuevo = (a << 24) | (r << 16) | (g << 8) | b;
                nuevoBuffer.setRGB(x, y, pixelNuevo);
            }
        }
        return nuevoBuffer;
    }

    // 2. Esteganografía: Recortar y Estirar Bits
    public static BufferedImage extraerBits(BufferedImage original, int mascaraHex, int valorMaximo) {
        int ancho = original.getWidth();
        int alto = original.getHeight();
        BufferedImage nuevoBuffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = original.getRGB(x, y);
                int a = (pixel >> 24) & 0xFF;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                // 1. Recortar aplicando el operador AND bit a bit (&) con la máscara recibida (ej. 0x07) [cite: 2120]
                int rRecortado = r & mascaraHex;
                int gRecortado = g & mascaraHex;
                int bRecortado = b & mascaraHex;

                // 2. Estirar aplicando la Regla de 3 [cite: 2124]
                r = Math.min(255, (rRecortado * 255) / valorMaximo);
                g = Math.min(255, (gRecortado * 255) / valorMaximo);
                b = Math.min(255, (bRecortado * 255) / valorMaximo);

                int pixelNuevo = (a << 24) | (r << 16) | (g << 8) | b;
                nuevoBuffer.setRGB(x, y, pixelNuevo);
            }
        }
        return nuevoBuffer;
    }
}