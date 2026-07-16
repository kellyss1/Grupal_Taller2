package org.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class EcualizadorHistograma {

    public BufferedImage procesar(BufferedImage original, double factor) {
        if (original == null) return null;

        int ancho = original.getWidth();
        int alto = original.getHeight();
        int totalPixeles = ancho * alto;

        BufferedImage salida = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        int[] histoR = new int[256];
        int[] histoG = new int[256];
        int[] histoB = new int[256];

        // PASO 1: Calcular histograma original
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = original.getRGB(x, y);
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF; 
                int b = pixel & 0xFF;        

                histoR[r]++; histoG[g]++; histoB[b]++;
            }
        }

        // PASO 2: Calcular Acumulado (CDF)
        int[] cdfR = new int[256]; int[] cdfG = new int[256]; int[] cdfB = new int[256];
        cdfR[0] = histoR[0]; cdfG[0] = histoG[0]; cdfB[0] = histoB[0];
        for (int i = 1; i < 256; i++) {
            cdfR[i] = cdfR[i - 1] + histoR[i];
            cdfG[i] = cdfG[i - 1] + histoG[i];
            cdfB[i] = cdfB[i - 1] + histoB[i];
        }

        // PASO 3: Calcular tablas de mapeo
        int[] mapaR = new int[256]; int[] mapaG = new int[256]; int[] mapaB = new int[256];
        for (int i = 0; i < 256; i++) {
            mapaR[i] = (int) Math.round(((double) cdfR[i] / totalPixeles) * 255);
            mapaG[i] = (int) Math.round(((double) cdfG[i] / totalPixeles) * 255);
            mapaB[i] = (int) Math.round(((double) cdfB[i] / totalPixeles) * 255);
        }

        // PASO 4: Mapear aplicando el % del Slider mediante interpolación lineal
        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int pixel = original.getRGB(x, y);
                int a = (pixel >> 24) & 0xFF;
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF; 
                int b = pixel & 0xFF;        

                int nuevoR = (int) Math.round(r + (mapaR[r] - r) * factor);
                int nuevoG = (int) Math.round(g + (mapaG[g] - g) * factor);
                int nuevoB = (int) Math.round(b + (mapaB[b] - b) * factor);

                nuevoR = Math.max(0, Math.min(255, nuevoR));
                nuevoG = Math.max(0, Math.min(255, nuevoG));
                nuevoB = Math.max(0, Math.min(255, nuevoB));

                int nuevoPixel = (a << 24) | (nuevoR << 16) | (nuevoG << 8) | nuevoB;
                salida.setRGB(x, y, nuevoPixel);
            }
        }
        return salida;
    }

    public BufferedImage generarHistograma(BufferedImage img) {
        if (img == null) return null;
        
        int[] r = new int[256];
        int[] g = new int[256];
        int[] b = new int[256];
        
        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                int pixel = img.getRGB(x, y);
                r[(pixel >> 16) & 0xFF]++;
                g[(pixel >> 8) & 0xFF]++;
                b[pixel & 0xFF]++;
            }
        }

        int anchoHisto = 300;
        int altoHisto = 200;
        
        BufferedImage lienzo = new BufferedImage(anchoHisto, altoHisto, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = lienzo.createGraphics();
        
        g2d.setColor(new Color(235, 235, 235));
        g2d.fillRect(0, 0, anchoHisto, altoHisto);

        int maxVal = 0;
        for (int i = 0; i < 256; i++) {
            if (r[i] > maxVal) maxVal = r[i];
            if (g[i] > maxVal) maxVal = g[i];
            if (b[i] > maxVal) maxVal = b[i];
        }
        if (maxVal == 0) maxVal = 1;

        for (int i = 0; i < 256; i++) {
            int xFisica = (int) (((double) i / 255) * (anchoHisto - 1));

            int hR = (int) (((double) r[i] / maxVal) * (altoHisto - 10));
            int hG = (int) (((double) g[i] / maxVal) * (altoHisto - 10));
            int hB = (int) (((double) b[i] / maxVal) * (altoHisto - 10));

            g2d.setColor(new Color(255, 0, 0, 130));
            g2d.drawLine(xFisica, altoHisto, xFisica, altoHisto - hR);

            g2d.setColor(new Color(0, 255, 0, 130));
            g2d.drawLine(xFisica, altoHisto, xFisica, altoHisto - hG);

            g2d.setColor(new Color(0, 0, 255, 130));
            g2d.drawLine(xFisica, altoHisto, xFisica, altoHisto - hB);
        }

        g2d.dispose();
        return lienzo;
    }
}
