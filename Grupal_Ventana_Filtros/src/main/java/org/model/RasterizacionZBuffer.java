package org.model;

import java.awt.*;
import java.awt.image.BufferedImage;

public class RasterizacionZBuffer {

    private static final int ANCHO = 800;
    private static final int ALTO = 600;

    private Color[][] colorBuffer;
    private float[][] zBuffer;

    private final int[] tAx = { 50, 550, 300 };
    private final int[] tAy = { 150, 150, 500 };
    private float zA = 0.4f;

    private final int[] tBx = { 150, 550, 150 };
    private final int[] tBy = { 450, 450, 150 };
    private float zB = 0.6f;

    private boolean zTestActivo = true;
    private final boolean[][] texturaBitmap = new boolean[32][32];

    public RasterizacionZBuffer() {
        generarTexturaBitmap();
    }

    private void generarTexturaBitmap() {
        for (int y = 0; y < 32; y++) {
            for (int x = 0; x < 32; x++) {
                boolean cuadro = ((x / 8) + (y / 8)) % 2 == 0;
                texturaBitmap[x][y] = cuadro;
            }
        }
    }

    private Color pintarConBitmap(int x, int y) {
        int tx = Math.abs(x) % 32;
        int ty = Math.abs(y) % 32;
        return texturaBitmap[tx][ty] ? Color.WHITE : Color.BLACK;
    }

    public BufferedImage generarImagen() {
        colorBuffer = new Color[ANCHO][ALTO];
        zBuffer = new float[ANCHO][ALTO];
        
        prepararLienzo();
        renderizarEscena();

        BufferedImage imagen = new BufferedImage(ANCHO, ALTO, BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < ANCHO; x++) {
            for (int y = 0; y < ALTO; y++) {
                imagen.setRGB(x, y, colorBuffer[x][y].getRGB());
            }
        }
        return imagen;
    }

    private void renderizarEscena() {
        for (int y = 0; y < ALTO; y++) {
            for (int x = 0; x < ANCHO; x++) {
                float px = x + 0.5f;
                float py = y + 0.5f;

                // Triángulo A (Usa la textura de ajedrez)
                if (evaluacion(tAx, tAy, px, py)) {
                    if (!zTestActivo || zA < zBuffer[x][y]) {
                        zBuffer[x][y] = zA;
                        colorBuffer[x][y] = pintarConBitmap(x, y);
                    }
                }

                // Triángulo B (MAGENTA sólido)
                if (evaluacion(tBx, tBy, px, py)) {
                    if (!zTestActivo || zB < zBuffer[x][y]) {
                        zBuffer[x][y] = zB;
                        colorBuffer[x][y] = Color.MAGENTA;
                    }
                }
            }
        }
    }

    private void prepararLienzo() {
        for (int x = 0; x < ANCHO; x++) {
            for (int y = 0; y < ALTO; y++) {
                colorBuffer[x][y] = Color.GRAY;
                zBuffer[x][y] = 1.0f;
            }
        }
    }

    private float funcionBorde(float ax, float ay, float bx, float by, float px, float py) {
        return (px - ax) * (by - ay) - (py - ay) * (bx - ax);
    }

    private boolean evaluacion(int[] tx, int[] ty, float px, float py) {
        float ladoAB = funcionBorde(tx[0], ty[0], tx[1], ty[1], px, py);
        float ladoBC = funcionBorde(tx[1], ty[1], tx[2], ty[2], px, py);
        float ladoCA = funcionBorde(tx[2], ty[2], tx[0], ty[0], px, py);
        return (ladoAB > 0 && ladoBC > 0 && ladoCA > 0) || (ladoAB < 0 && ladoBC < 0 && ladoCA < 0);
    }

    public void setZA(float zA) { this.zA = zA; }
    public void setZB(float zB) { this.zB = zB; }
    public void setzTestActivo(boolean zTestActivo) { this.zTestActivo = zTestActivo; }
}
