package org.model;

import java.awt.image.BufferedImage;

public class GeneradorGradientes {

    // 1. Gradiente Lineal (Izquierda a Derecha - Modifica el Alpha de la imagen original)
    public static BufferedImage generarLinealIzquierdaDerecha(BufferedImage original) {
        int ancho = original.getWidth();
        int alto = original.getHeight();
        BufferedImage nuevoBuffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixelOriginal = original.getRGB(x, y);
                
                // Extraer componentes RGB originales
                int r = (pixelOriginal >> 16) & 0xFF;
                int g = (pixelOriginal >> 8) & 0xFF;
                int b = pixelOriginal & 0xFF;

                // La intensidad define la opacidad (Alpha) de 0 a 255
                int alpha = (x * 255) / ancho;

                int pixel = (alpha << 24) | (r << 16) | (g << 8) | b;
                nuevoBuffer.setRGB(x, y, pixel);
            }
        }
        return nuevoBuffer;
    }

    // 2. Gradiente Radial (Centro a Extremos - Modifica el Alpha de la imagen original)
    public static BufferedImage generarRadial(BufferedImage original) {
        int ancho = original.getWidth();
        int alto = original.getHeight();
        BufferedImage nuevoBuffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        // Coordenadas del centro exacto
        int centroX = ancho / 2;
        int centroY = alto / 2;

        // Máxima distancia calculada con Pitágoras (del centro a la esquina 0,0)
        double distanciaMaxima = Math.sqrt((centroX * centroX) + (centroY * centroY));

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixelOriginal = original.getRGB(x, y);
                
                // Extraer componentes RGB originales
                int r = (pixelOriginal >> 16) & 0xFF;
                int g = (pixelOriginal >> 8) & 0xFF;
                int b = pixelOriginal & 0xFF;

                // Calcular tamaño de los catetos
                int dx = centroX - x;
                int dy = centroY - y;

                // Distancia real en línea recta del píxel actual al centro
                double distanciaActual = Math.sqrt((dx * dx) + (dy * dy));

                // Convertir distancia a transparencia (Alpha)
                // En el centro (distancia 0) es opaco, en los bordes es transparente
                int alpha = 255 - (int) ((distanciaActual * 255) / distanciaMaxima);
                alpha = Math.clamp(alpha, 0, 255);

                int pixel = (alpha << 24) | (r << 16) | (g << 8) | b;
                nuevoBuffer.setRGB(x, y, pixel);
            }
        }
        return nuevoBuffer;
    }
}