package org.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Blending {

    @FunctionalInterface
    private interface BlendingOp {
        int combine(int c1, int c2);
    }

    private static BufferedImage prepararSegundaImagen(BufferedImage img1, BufferedImage img2) {
        int ancho = img1.getWidth();
        int alto = img1.getHeight();

        Image imgTemp = img2.getScaledInstance(ancho, alto, Image.SCALE_FAST);
        BufferedImage bufferTemp = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_ARGB);

        Graphics2D grTemp = bufferTemp.createGraphics();
        grTemp.drawImage(imgTemp, 0, 0, null);
        grTemp.dispose();

        return bufferTemp;
    }

    private static BufferedImage aplicarBlending(BufferedImage img1, BufferedImage img2, BlendingOp op) {
        img2 = prepararSegundaImagen(img1, img2);
        int ancho = img1.getWidth();
        int alto = img1.getHeight();
        BufferedImage bufferBlend = new BufferedImage(ancho, alto, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < alto; y++) {
            for (int x = 0; x < ancho; x++) {
                int p1 = img1.getRGB(x, y);
                int p2 = img2.getRGB(x, y);

                int r1 = (p1 >> 16) & 0xFF;
                int g1 = (p1 >> 8) & 0xFF;
                int b1 = p1 & 0xFF;

                int r2 = (p2 >> 16) & 0xFF;
                int g2 = (p2 >> 8) & 0xFF;
                int b2 = p2 & 0xFF;

                int r = Math.clamp(op.combine(r1, r2), 0, 255);
                int g = Math.clamp(op.combine(g1, g2), 0, 255);
                int b = Math.clamp(op.combine(b1, b2), 0, 255);

                bufferBlend.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        return bufferBlend;
    }

    public static BufferedImage alphaBlending(BufferedImage img1, BufferedImage img2) {
        float alpha = 0.5f;
        return aplicarBlending(img1, img2, (c1, c2) -> (int) ((1 - alpha) * c1 + alpha * c2));
    }

    public static BufferedImage additiveBlending(BufferedImage img1, BufferedImage img2) {
        return aplicarBlending(img1, img2, Integer::sum);
    }

    public static BufferedImage multiplicativeBlending(BufferedImage img1, BufferedImage img2) {
        return aplicarBlending(img1, img2, (c1, c2) -> (c1 * c2) / 255);
    }
}
