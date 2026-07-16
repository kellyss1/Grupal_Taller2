package org.model;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

public class TexturasWBuffer {

    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;

    public static final int MODO_TEXTURA = 1;
    public static final int MODO_COLOR = 2;
    public static final int MODO_PROFUNDIDAD = 3;
    public static final int MODO_WBUFFER = 4;
    public static final int MODO_COMPLETO = 5;

    private int modoActual = MODO_COMPLETO;

    private final BufferedImage textura;
    private final float[][] wBuffer;

    static class Vertex {
        float x, y;
        float w; // Profundidad (distancia a la cámara)
        float r, g, b;
        float u, v;

        Vertex(float x, float y, float w, float r, float g, float b, float u, float v) {
            this.x = x; this.y = y; this.w = w;
            this.r = r; this.g = g; this.b = b;
            this.u = u; this.v = v;
        }
    }

    public TexturasWBuffer() {
        textura = new BufferedImage(256, 256, BufferedImage.TYPE_INT_RGB);
        wBuffer = new float[WIDTH][HEIGHT];
        crearTextura();
    }

    public void setModo(int modo) {
        this.modoActual = modo;
    }

    private void crearTextura() {
        for (int y = 0; y < textura.getHeight(); y++) {
            for (int x = 0; x < textura.getWidth(); x++) {
                boolean cuadro = ((x / 32) + (y / 32)) % 2 == 0;
                textura.setRGB(x, y, cuadro ? new Color(240, 240, 240).getRGB() : new Color(30, 30, 30).getRGB());
            }
        }
    }

    private void limpiarWBuffer() {
        for (int i = 0; i < WIDTH; i++) {
            Arrays.fill(wBuffer[i], Float.NEGATIVE_INFINITY);
        }
    }

    private float edge(float ax, float ay, float bx, float by, float cx, float cy) {
        return (cx - ax) * (by - ay) - (cy - ay) * (bx - ax);
    }

    private boolean dentroTriangulo(float w0, float w1, float w2, float area) {
        if (area > 0) {
            return w0 >= 0 && w1 >= 0 && w2 >= 0;
        }
        return w0 <= 0 && w1 <= 0 && w2 <= 0;
    }

    public BufferedImage generarImagen() {
        BufferedImage canvas = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        limpiarWBuffer();

        Graphics2D g = canvas.createGraphics();
        g.setColor(new Color(40, 42, 48));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        switch (modoActual) {
            case MODO_TEXTURA -> renderTextura(g, canvas);
            case MODO_COLOR -> renderColor(g, canvas);
            case MODO_PROFUNDIDAD -> renderProfundidad(g, canvas);
            case MODO_WBUFFER -> renderWBuffer(g, canvas);
            default -> renderCompleto(g, canvas);
        }

        g.dispose();
        return canvas;
    }

    private void renderTextura(Graphics2D g, BufferedImage canvas) {
        Vertex a = new Vertex(140, 120, 1.2f, 1, 1, 1, 0, 0);
        Vertex b = new Vertex(620, 150, 0.8f, 1, 1, 1, 1, 0);
        Vertex c = new Vertex(600, 470, 0.5f, 1, 1, 1, 1, 1);
        Vertex d = new Vertex(160, 440, 0.9f, 1, 1, 1, 0, 1);

        rasterizarTriangulo(canvas, a, b, c, true, false, true, false);
        rasterizarTriangulo(canvas, a, c, d, true, false, true, false);
    }

    private void renderColor(Graphics2D g, BufferedImage canvas) {
        Vertex a = new Vertex(140, 120, 1.2f, 1, 0, 0, 0, 0); // Rojo
        Vertex b = new Vertex(620, 150, 0.8f, 0, 1, 0, 1, 0); // Verde
        Vertex c = new Vertex(600, 470, 0.5f, 0, 0, 1, 1, 1); // Azul
        Vertex d = new Vertex(160, 440, 0.9f, 1, 1, 0, 0, 1); // Amarillo

        rasterizarTriangulo(canvas, a, b, c, false, true, true, false);
        rasterizarTriangulo(canvas, a, c, d, false, true, true, false);
    }

    private void renderProfundidad(Graphics2D g, BufferedImage canvas) {
        g.setColor(Color.GRAY);
        g.drawLine(WIDTH / 2, 70, WIDTH / 2, HEIGHT - 40);

        // Cartel izquierdo (Lineal)
        Vertex a1 = new Vertex(50, 150, 2.0f,  1, 1, 1, 0, 0);
        Vertex b1 = new Vertex(350, 180, 0.4f, 1, 1, 1, 1, 0);
        Vertex c1 = new Vertex(330, 450, 0.4f, 1, 1, 1, 1, 1);
        Vertex d1 = new Vertex(70, 420, 2.0f,  1, 1, 1, 0, 1);

        rasterizarTriangulo(canvas, a1, b1, c1, true, false, false, false);
        rasterizarTriangulo(canvas, a1, c1, d1, true, false, false, false);

        // Cartel derecho (Corregido con 1/W)
        Vertex a2 = new Vertex(450, 150, 2.0f,  1, 1, 1, 0, 0);
        Vertex b2 = new Vertex(750, 180, 0.4f,  1, 1, 1, 1, 0);
        Vertex c2 = new Vertex(730, 450, 0.4f,  1, 1, 1, 1, 1);
        Vertex d2 = new Vertex(470, 420, 2.0f,  1, 1, 1, 0, 1);

        rasterizarTriangulo(canvas, a2, b2, c2, true, false, true, false);
        rasterizarTriangulo(canvas, a2, c2, d2, true, false, true, false);
    }

    private void renderWBuffer(Graphics2D g, BufferedImage canvas) {
        g.setColor(Color.GRAY);
        g.drawLine(WIDTH / 2, 70, WIDTH / 2, HEIGHT - 40);

        // Escena Izquierda (Sin Buffer)
        Vertex fondoA1 = new Vertex(60, 120, 4.0f, 0, 0.4f, 0.8f, 0, 0);
        Vertex fondoB1 = new Vertex(320, 150, 4.0f, 0, 0.4f, 0.8f, 1, 0);
        Vertex fondoC1 = new Vertex(300, 480, 4.0f, 0, 0.4f, 0.8f, 1, 1);
        Vertex fondoD1 = new Vertex(80, 450, 4.0f, 0, 0.4f, 0.8f, 0, 1);

        Vertex frenteA1 = new Vertex(120, 180, 1.5f, 1, 0.5f, 0, 0, 0);
        Vertex frenteB1 = new Vertex(380, 210, 1.5f, 1, 0.5f, 0, 1, 0);
        Vertex frenteC1 = new Vertex(360, 520, 1.5f, 1, 0.5f, 0, 1, 1);
        Vertex frenteD1 = new Vertex(140, 490, 1.5f, 1, 0.5f, 0, 0, 1);

        rasterizarTriangulo(canvas, frenteA1, frenteB1, frenteC1, false, true, true, false);
        rasterizarTriangulo(canvas, frenteA1, frenteC1, frenteD1, false, true, true, false);
        rasterizarTriangulo(canvas, fondoA1, fondoB1, fondoC1, false, true, true, false);

        // Escena Derecha (Con W-Buffer activado)
        Vertex fondoA2 = new Vertex(460, 120, 4.0f, 0, 0.4f, 0.8f, 0, 0);
        Vertex fondoB2 = new Vertex(720, 150, 4.0f, 0, 0.4f, 0.8f, 1, 0);
        Vertex fondoC2 = new Vertex(700, 480, 4.0f, 0, 0.4f, 0.8f, 1, 1);
        Vertex fondoD2 = new Vertex(480, 450, 4.0f, 0, 0.4f, 0.8f, 0, 1);

        Vertex frenteA2 = new Vertex(520, 180, 1.5f, 1, 0.5f, 0, 0, 0);
        Vertex frenteB2 = new Vertex(780, 210, 1.5f, 1, 0.5f, 0, 1, 0);
        Vertex frenteC2 = new Vertex(760, 520, 1.5f, 1, 0.5f, 0, 1, 1);
        Vertex frenteD2 = new Vertex(540, 490, 1.5f, 1, 0.5f, 0, 0, 1);

        rasterizarTriangulo(canvas, frenteA2, frenteB2, frenteC2, false, true, true, true);
        rasterizarTriangulo(canvas, frenteA2, frenteC2, frenteD2, false, true, true, true);
        rasterizarTriangulo(canvas, fondoA2, fondoB2, fondoC2, false, true, true, true);
        rasterizarTriangulo(canvas, fondoA2, fondoC2, fondoD2, false, true, true, true);
    }

    private void renderCompleto(Graphics2D g, BufferedImage canvas) {
        Vertex a = new Vertex(140, 100, 1.5f, 1f, 0.2f, 0.2f, 0, 0);
        Vertex b = new Vertex(660, 140, 0.7f, 0.2f, 1f, 0.2f, 1, 0);
        Vertex c = new Vertex(630, 500, 0.4f, 0.2f, 0.2f, 1f, 1, 1);
        Vertex d = new Vertex(170, 460, 1.1f, 1f, 1f, 0.2f, 0, 1);

        rasterizarTriangulo(canvas, a, b, c, true, true, true, true);
        rasterizarTriangulo(canvas, a, c, d, true, true, true, true);
    }

    private void rasterizarTriangulo(BufferedImage canvas, Vertex v0, Vertex v1, Vertex v2,
                                     boolean usarTextura, boolean usarColor,
                                     boolean persCorrecta, boolean usarWBuffer) {

        int minX = (int) Math.max(0, Math.min(v0.x, Math.min(v1.x, v2.x)));
        int maxX = (int) Math.min(WIDTH - 1, Math.max(v0.x, Math.max(v1.x, v2.x)));
        int minY = (int) Math.max(0, Math.min(v0.y, Math.min(v1.y, v2.y)));
        int maxY = (int) Math.min(HEIGHT - 1, Math.max(v0.y, Math.max(v1.y, v2.y)));

        float area = edge(v0.x, v0.y, v1.x, v1.y, v2.x, v2.y);
        if (area == 0) return;

        for (int y = minY; y <= maxY; y++) {
            for (int x = minX; x <= maxX; x++) {

                float w0 = edge(v1.x, v1.y, v2.x, v2.y, x, y);
                float w1 = edge(v2.x, v2.y, v0.x, v0.y, x, y);
                float w2 = edge(v0.x, v0.y, v1.x, v1.y, x, y);

                if (!dentroTriangulo(w0, w1, w2, area)) continue;

                w0 /= area; w1 /= area; w2 /= area;

                float invW = (w0 * (1f / v0.w)) + (w1 * (1f / v1.w)) + (w2 * (1f / v2.w));

                if (usarWBuffer) {
                    if (invW <= wBuffer[x][y]) continue;
                    wBuffer[x][y] = invW;
                }

                float r, g, b, u, v;

                if (persCorrecta) {
                    r = (w0 * v0.r / v0.w + w1 * v1.r / v1.w + w2 * v2.r / v2.w) / invW;
                    g = (w0 * v0.g / v0.w + w1 * v1.g / v1.w + w2 * v2.g / v2.w) / invW;
                    b = (w0 * v0.b / v0.w + w1 * v1.b / v1.w + w2 * v2.b / v2.w) / invW;
                    u = (w0 * v0.u / v0.w + w1 * v1.u / v1.w + w2 * v2.u / v2.w) / invW;
                    v = (w0 * v0.v / v0.w + w1 * v1.v / v1.w + w2 * v2.v / v2.w) / invW;
                } else {
                    r = w0 * v0.r + w1 * v1.r + w2 * v2.r;
                    g = w0 * v0.g + w1 * v1.g + w2 * v2.g;
                    b = w0 * v0.b + w1 * v1.b + w2 * v2.b;
                    u = w0 * v0.u + w1 * v1.u + w2 * v2.u;
                    v = w0 * v0.v + w1 * v1.v + w2 * v2.v;
                }

                Color colorFinal;

                if (usarTextura && usarColor) {
                    int tx = Math.max(0, Math.min(textura.getWidth() - 1, (int) (u * (textura.getWidth() - 1))));
                    int ty = Math.max(0, Math.min(textura.getHeight() - 1, (int) (v * (textura.getHeight() - 1))));
                    Color texColor = new Color(textura.getRGB(tx, ty));
                    colorFinal = mezclarColor(texColor, r, g, b);
                } else if (usarTextura) {
                    int tx = Math.max(0, Math.min(textura.getWidth() - 1, (int) (u * (textura.getWidth() - 1))));
                    int ty = Math.max(0, Math.min(textura.getHeight() - 1, (int) (v * (textura.getHeight() - 1))));
                    colorFinal = new Color(textura.getRGB(tx, ty));
                } else if (usarColor) {
                    colorFinal = new Color(
                            Math.min(255, Math.max(0, (int) (r * 255))),
                            Math.min(255, Math.max(0, (int) (g * 255))),
                            Math.min(255, Math.max(0, (int) (b * 255)))
                    );
                } else {
                    colorFinal = Color.WHITE;
                }

                canvas.setRGB(x, y, colorFinal.getRGB());
            }
        }
    }

    private Color mezclarColor(Color tex, float r, float g, float b) {
        int fr = Math.min(255, Math.max(0, (int) (tex.getRed() * r)));
        int fg = Math.min(255, Math.max(0, (int) (tex.getGreen() * g)));
        int fb = Math.min(255, Math.max(0, (int) (tex.getBlue() * b)));
        return new Color(fr, fg, fb);
    }
}
