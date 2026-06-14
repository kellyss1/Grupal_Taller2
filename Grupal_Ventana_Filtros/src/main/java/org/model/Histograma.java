package org.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class Histograma {
    public static BufferedImage histograma(BufferedImage original) {

        int ancho = original.getWidth();
        int alto = original.getHeight();

        int r,g,b, pixel;
        int maxR, maxG, maxB;
        float escalaYR, escalaYG, escalaYB;

        // Configuración del lienzo del histograma
        int anchoHisto = 500;
        int altoHisto = 400;

        BufferedImage bufferHisto = new BufferedImage(anchoHisto, altoHisto, BufferedImage.TYPE_INT_RGB);

        int[] histoR = new int[256];
        int[] histoG = new int[256];
        int[] histoB = new int[256];

        Graphics2D gr = bufferHisto.createGraphics();
        gr.setStroke(new BasicStroke(2));

        gr.setColor(Color.WHITE);
        gr.fillRect(0,0,anchoHisto, altoHisto);

        // recorrer la imagen
        for (int y =0; y < alto; y++) {
            for (int x=0; x < ancho; x++) {
                pixel = original.getRGB(x, y);

                r = (pixel >> 16) & 0xFF;
                g = (pixel >> 8) & 0xFF;
                b = pixel & 0xFF;

                histoR[r]++;
                histoG[g]++;
                histoB[b]++;
            }
        }

        // escala
        float escalaX = anchoHisto / 256.0f;

        // dibujar histogramas, dibujar rojo
        maxR = maximo(histoR);
        escalaYR = (float) altoHisto / maxR;
        gr.setColor(Color.RED);
        for(int i=1; i < histoR.length; i++) {
            int x1 = (int) (escalaX * (i-1));
            int y1 = altoHisto - (int) (escalaYR * histoR[i-1]);

            int x2 = (int) (escalaX * i);
            int y2 = altoHisto - (int) (escalaYR * histoR[i]);

            gr.drawLine(x1, y1, x2, y2);
        }

        //dibujar verde
        maxG = maximo(histoG);
        escalaYG = (float) altoHisto / maxG;
        gr.setColor(Color.GREEN);
        for(int i=1; i < histoG.length; i++) {
            int x1 = (int) (escalaX * (i-1));
            int y1 = altoHisto - (int) (escalaYG * histoG[i-1]);

            int x2 = (int) (escalaX * i);
            int y2 = altoHisto - (int) (escalaYG * histoG[i]);

            gr.drawLine(x1, y1, x2, y2);
        }

        //dibujar azul
        maxB = maximo(histoB);
        escalaYB = (float) altoHisto / maxB;
        gr.setColor(Color.BLUE);
        for(int i=1; i < histoB.length; i++) {
            int x1 = (int) (escalaX * (i-1));
            int y1 = altoHisto - (int) (escalaYB * histoB[i-1]);

            int x2 = (int) (escalaX * i);
            int y2 = altoHisto - (int) (escalaYB * histoB[i]);

            gr.drawLine(x1, y1, x2, y2);
        }

        gr.dispose();
        return bufferHisto;
    }

    public static int maximo(int[] h) {
        int max =0;
        for (int val : h) if (val > max) max = val;
        System.out.println(Arrays.stream(h).max().getAsInt());
        return max;
    }
}
