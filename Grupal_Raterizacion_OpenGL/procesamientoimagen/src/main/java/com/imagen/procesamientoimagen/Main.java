package com.imagen.procesamientoimagen;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL;

public class Main {

    private long window;

    private int textureOriginal;
    private int textureGris;

    // Imagen original móvil
    private float zOriginal = -5.0f;

    public void run() {

        if (!glfwInit()) {
            throw new IllegalStateException(
                    "No se pudo iniciar GLFW");
        }

        window = glfwCreateWindow(
                800,
                600,
                "Rasterizacion - ZBuffer - Bitmaps",
                0,
                0);

        if (window == 0) {
            throw new RuntimeException(
                    "No se pudo crear la ventana");
        }

        glfwMakeContextCurrent(window);

        // Callback para ajustar el viewport cuando se cambia el tamaño de la ventana
        glfwSetFramebufferSizeCallback(window, (window, width, height) -> {
            glViewport(0, 0, width, height);
            
            glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            float aspectRatio = (float) width / (float) height;
            if (width >= height) {
                // Ventana ancha
                glFrustum(-aspectRatio, aspectRatio, -1, 1, 1, 100);
            } else {
                // Ventana alta
                glFrustum(-1, 1, -1/aspectRatio, 1/aspectRatio, 1, 100);
            }
            glMatrixMode(GL_MODELVIEW);
        });

        glfwSwapInterval(1);

        GL.createCapabilities();

        init();

        loop();

        glfwDestroyWindow(window);

        glfwTerminate();
    }

    private void init() {

        // Configuración inicial del viewport
        int[] width = new int[1];
        int[] height = new int[1];
        glfwGetFramebufferSize(window, width, height);
        glViewport(0, 0, width[0], height[0]);

        // Configuración de la proyección 3D
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        float aspectRatio = (float) width[0] / (float) height[0];
        if (width[0] >= height[0]) {
            glFrustum(-aspectRatio, aspectRatio, -1, 1, 1, 100);
        } else {
            glFrustum(-1, 1, -1/aspectRatio, 1/aspectRatio, 1, 100);
        }

        glMatrixMode(GL_MODELVIEW);

        glEnable(GL_TEXTURE_2D);

        // Activar Z-Buffer
        glEnable(GL_DEPTH_TEST);

        glDepthFunc(GL_LESS);

        try {

            BufferedImage original =
                    ImageIO.read(
                            Main.class.getResourceAsStream("/images.jpg"));

            if (original == null) {
                throw new Exception("No se pudo encontrar la imagen en resources/images.jpg");
            }

            BufferedImage gris =
                    ImageProcessor.escalaGrises(
                            original);

            textureOriginal =
                    TextureLoader.loadTexture(
                            original);

            textureGris =
                    TextureLoader.loadTexture(
                            gris);

        } catch (Exception e) {

            e.printStackTrace();
        }

        glClearColor(
                0f,
                0f,
                0f,
                1f);
    }

    private void loop() {

        while (!glfwWindowShouldClose(window)) {

            // Acercar imagen original
            if (glfwGetKey(
                    window,
                    GLFW_KEY_UP) == GLFW_PRESS) {

                zOriginal += 0.05f;

                if (zOriginal > -1.1f) {
                    zOriginal = -1.1f;
                }
            }

            // Alejar imagen original
            if (glfwGetKey(
                    window,
                    GLFW_KEY_DOWN) == GLFW_PRESS) {

                zOriginal -= 0.05f;

                if (zOriginal < -30.0f) {
                    zOriginal = -30.0f;
                }
            }

            glClear(
                    GL_COLOR_BUFFER_BIT
                            | GL_DEPTH_BUFFER_BIT);

            dibujar();

            glfwSwapBuffers(window);

            glfwPollEvents();
        }
    }

    private void dibujar() {

        // Imagen procesada (escala de grises)
        // permanece fija en el fondo
        // Imagen gris fija
        float zGris = -8.0f;
        dibujarTextura(
                textureGris,
                zGris,
                1.2f);

        // Imagen original se mueve
        dibujarTextura(
                textureOriginal,
                zOriginal,
                1.0f);
    }

    private void dibujarTextura(
            int texture,
            float z,
            float escala) {

        glLoadIdentity();

        glTranslatef(
                0.0f,
                0.0f,
                z);

        glBindTexture(
                GL_TEXTURE_2D,
                texture);

        glBegin(GL_QUADS);

        // Coordenadas corregidas para evitar imagen invertida

        glTexCoord2f(0, 1);
        glVertex3f(
                -escala,
                -escala,
                0.0f);

        glTexCoord2f(1, 1);
        glVertex3f(
                escala,
                -escala,
                0.0f);

        glTexCoord2f(1, 0);
        glVertex3f(
                escala,
                escala,
                0.0f);

        glTexCoord2f(0, 0);
        glVertex3f(
                -escala,
                escala,
                0.0f);

        glEnd();
    }

    public static void main(String[] args) {

        new Main().run();
    }
}