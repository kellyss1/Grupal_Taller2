package org.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class StencilBlendingOp {

    public enum Modo {
        STENCIL, BLENDING, FINAL
    }

    private Modo modoActual = Modo.FINAL;

    public BufferedImage procesar(BufferedImage img1, BufferedImage img2) {
        if (img1 == null || img2 == null) return null;

        int w = Math.min(img1.getWidth(), img2.getWidth());
        int h = Math.min(img1.getHeight(), img2.getHeight());

        // 1. Crear la máscara circular
        BufferedImage mascara = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = mascara.createGraphics();
        g.setColor(Color.WHITE);
        // Usando dimensiones proporcionales si las imágenes varían
        int ovalSize = Math.min(w, h) / 2;
        g.fillOval((w - ovalSize) / 2, (h - ovalSize) / 2, ovalSize, ovalSize);
        g.dispose();

        BufferedImage resultado = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                int p1 = img1.getRGB(x, y);
                int p2 = img2.getRGB(x, y);
                int pM = mascara.getRGB(x, y);

                // Operación Stencil
                int pStencil = ((pM & 0xFF) > 128) ? p1 : 0;

                if (modoActual == Modo.STENCIL) {
                    resultado.setRGB(x, y, pStencil);
                    continue;
                }

                // Desempaquetar
                int rS = (pStencil >> 16) & 0xFF;
                int gS = (pStencil >> 8) & 0xFF;
                int bS = pStencil & 0xFF;

                int r2 = (p2 >> 16) & 0xFF;
                int g2 = (p2 >> 8) & 0xFF;
                int b2 = p2 & 0xFF;

                // Operación Blending (Alpha = 0.6f)
                int rB = (int) (rS * 0.6f + r2 * 0.4f);
                int gB = (int) (gS * 0.6f + g2 * 0.4f);
                int bB = (int) (bS * 0.6f + b2 * 0.4f);
                int pBlending = (rB << 16) | (gB << 8) | bB;

                if (modoActual == Modo.BLENDING) {
                    resultado.setRGB(x, y, pBlending);
                    continue;
                }

                // Operación Logic Op (XOR)
                int rF = rB ^ r2;
                int gF = gB ^ g2;
                int bF = bB ^ b2;
                resultado.setRGB(x, y, (rF << 16) | (gF << 8) | bF);
            }
        }

        return resultado;
    }

    public void setModo(Modo modo) {
        this.modoActual = modo;
    }
}
