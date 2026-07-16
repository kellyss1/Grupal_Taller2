package org.model;

import java.awt.image.BufferedImage;

public class BufferAcumulacion {

    public BufferedImage aplicarAtenuacion(BufferedImage imagen, float factor) {
        if (imagen == null) return null;

        int ancho = imagen.getWidth();
        int alto = imagen.getHeight();
        int totalPixeles = ancho * alto;

        // Crear los buffers flotantes simulando el Buffer de Acumulación
        float[] bufR = new float[totalPixeles];
        float[] bufG = new float[totalPixeles];
        float[] bufB = new float[totalPixeles];

        // Paso 1: GL_LOAD -> Cargar los píxeles de la imagen al buffer de acumulación
        for (int i = 0; i < totalPixeles; i++) {
            int rgb = imagen.getRGB(i % ancho, i / ancho);
            bufR[i] = (rgb >> 16) & 0xFF;
            bufG[i] = (rgb >> 8) & 0xFF;
            bufB[i] = rgb & 0xFF;
        }

        // Paso 2: GL_MULT -> Multiplicar cada celda por el factor de atenuación de luz
        for (int i = 0; i < totalPixeles; i++) {
            bufR[i] *= factor;
            bufG[i] *= factor;
            bufB[i] *= factor;
        }

        // Paso 3: GL_RETURN -> Convertir los valores del buffer de vuelta a la imagen de salida
        BufferedImage resultado = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);
        for (int i = 0; i < totalPixeles; i++) {
            int r = (int) Math.min(255, Math.max(0, bufR[i]));
            int g = (int) Math.min(255, Math.max(0, bufG[i]));
            int b = (int) Math.min(255, Math.max(0, bufB[i]));

            int pixelNuevo = (r << 16) | (g << 8) | b;
            resultado.setRGB(i % ancho, i / ancho, pixelNuevo);
        }

        return resultado;
    }
}
