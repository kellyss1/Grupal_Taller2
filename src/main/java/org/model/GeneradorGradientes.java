package org.model;

import java.awt.image.BufferedImage;

public class GeneradorGradientes {

    // 1. Gradiente Lineal (Izquierda a Derecha - Blanco a Negro)
    public static BufferedImage generarLinealIzquierdaDerecha(int ancho, int alto) {
        BufferedImage nuevoBuffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                // Regla de 3 para calcular la intensidad según la posición horizontal 'x' [cite: 1334, 1392]
                int intensidad = (x * 255) / ancho;

                int pixel = (intensidad << 16) | (intensidad << 8) | intensidad;
                nuevoBuffer.setRGB(x, y, pixel);
            }
        }
        return nuevoBuffer;
    }

    // 2. Gradiente Radial (Centro a Extremos)
    public static BufferedImage generarRadial(int ancho, int alto) {
        BufferedImage nuevoBuffer = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        // Coordenadas del centro exacto [cite: 1412]
        int centroX = ancho / 2;
        int centroY = alto / 2;

        // Máxima distancia calculada con Pitágoras (del centro a la esquina 0,0) [cite: 1445, 1859]
        double distanciaMaxima = Math.sqrt((centroX * centroX) + (centroY * centroY));

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                // Calcular tamaño de los catetos [cite: 1846]
                int dx = centroX - x;
                int dy = centroY - y;

                // Distancia real en línea recta del píxel actual al centro [cite: 1427, 1842]
                double distanciaActual = Math.sqrt((dx * dx) + (dy * dy));

                // Convertir distancia a color usando Regla de 3 [cite: 1432]
                int intensidad = (int) ((distanciaActual * 255) / distanciaMaxima);
                intensidad = Math.min(255, intensidad); // Seguro por si los decimales se pasan [cite: 1435]

                int pixel = (intensidad << 16) | (intensidad << 8) | intensidad;
                nuevoBuffer.setRGB(x, y, pixel);
            }
        }
        return nuevoBuffer;
    }
}