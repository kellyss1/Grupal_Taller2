package org.model;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class FiltrosBasicos {

    // 1. Escala de Grises
    public static BufferedImage aplicarEscalaGrises(BufferedImage original) {
        int ancho = original.getWidth();
        int alto = original.getHeight();
        // Usamos TYPE_INT_ARGB para conservar la transparencia si existe
        BufferedImage nuevoBuffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = original.getRGB(x, y);
                // Extraemos colores con desplazamientos (>>) y la máscara estricta & 0xFF
                int a = (pixel >> 24) & 0xFF;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                // Calculamos el gris con el promedio
                int gris = (r + g + b) / 3;

                // Rearmamos el píxel usando desplazamientos (<<) y el pegamento lógico OR (|)
                int pixelNuevo = (a << 24) | (gris << 16) | (gris << 8) | gris;
                nuevoBuffer.setRGB(x, y, pixelNuevo);
            }
        }
        return nuevoBuffer;
    }

    // 2. Filtro Negativo / Inverso
    public static BufferedImage aplicarNegativo(BufferedImage original) {
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

                // Invertimos restando el valor original a 255
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;

                int pixelNuevo = (a << 24) | (r << 16) | (g << 8) | b;
                nuevoBuffer.setRGB(x, y, pixelNuevo);
            }
        }
        return nuevoBuffer;
    }

    // 3. Vidrio Esmerilado (Transparencia por Brillo)
    public static BufferedImage aplicarVidrioEsmerilado(BufferedImage original) {
        int ancho = original.getWidth();
        int alto = original.getHeight();
        BufferedImage nuevoBuffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = original.getRGB(x, y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;

                // El brillo mismo del píxel se convierte en su nivel de opacidad (Alpha)
                int a = (r + g + b) / 3;

                int pixelNuevo = (a << 24) | (r << 16) | (g << 8) | b;
                nuevoBuffer.setRGB(x, y, pixelNuevo);
            }
        }
        return nuevoBuffer;
    }

    // 4. Efecto Fantasmal (Uso de espacio HSV)
    public static BufferedImage aplicarFantasmal(BufferedImage original) {
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

                // Convertimos a HSB para analizar la saturación
                float[] hsb = Color.RGBtoHSB(r, g, b, null);
                float s = hsb[1];

                // Si la saturación es muy baja (grisáceo), hacemos el píxel invisible
                if (s < 0.3f) {
                    a = 0;
                }

                int pixelNuevo = (a << 24) | (r << 16) | (g << 8) | b;
                nuevoBuffer.setRGB(x, y, pixelNuevo);
            }
        }
        return nuevoBuffer;
    }

    // 5. Rotación de Tono (Hue Shift)
    public static BufferedImage aplicarRotacionTono(BufferedImage original) {
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

                float[] hsb = Color.RGBtoHSB(r, g, b, null);
                
                // Rotamos el tono (Hue) 180 grados (0.5f en escala 0-1)
                hsb[0] = (hsb[0] + 0.5f) % 1.0f;

                int rgb = Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
                int rN = (rgb >> 16) & 0xFF;
                int gN = (rgb >> 8) & 0xFF;
                int bN = rgb & 0xFF;

                int pixelNuevo = (a << 24) | (rN << 16) | (gN << 8) | bN;
                nuevoBuffer.setRGB(x, y, pixelNuevo);
            }
        }
        return nuevoBuffer;
    }
}
