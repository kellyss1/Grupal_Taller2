package com.imagen.procesamientoimagen;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL;

public class Main {

        private long window;

        private int textureGris;

        private float posZ = -5.0f;

        public void run() {

                if (!glfwInit()) {
                        throw new IllegalStateException(
                                        "No se pudo iniciar GLFW");
                }

                window = glfwCreateWindow(
                                800,
                                600,
                                "Procesamiento de Imagenes",
                                0,
                                0);

                if (window == 0) {
                        throw new RuntimeException(
                                        "No se pudo crear la ventana");
                }

                glfwMakeContextCurrent(window);

                glfwSwapInterval(1);

                GL.createCapabilities();

                init();

                loop();

                glfwDestroyWindow(window);
                glfwTerminate();
        }

        private void init() {

                glMatrixMode(GL_PROJECTION);
                glLoadIdentity();

                glFrustum(
                                -1,
                                1,
                                -1,
                                1,
                                1,
                                100);

                glMatrixMode(GL_MODELVIEW);

                glEnable(GL_TEXTURE_2D);

                try {

                        BufferedImage original = ImageIO.read(
                                        new File(
                                                        "src/main/resources/images.jpg"));

                        BufferedImage gris = ImageProcessor.escalaGrises(
                                        original);

                        textureGris = TextureLoader.loadTexture(
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

                        // Acercar la imagen
                        if (glfwGetKey(window, GLFW_KEY_UP) == GLFW_PRESS) {

                                posZ += 0.05f;

                                // Evitar pasar el plano cercano
                                if (posZ > -1.1f) {
                                        posZ = -1.1f;
                                }
                        }

                        // Alejar la imagen
                        if (glfwGetKey(window, GLFW_KEY_DOWN) == GLFW_PRESS) {

                                posZ -= 0.05f;

                                // Límite máximo de alejamiento
                                if (posZ < -30.0f) {
                                        posZ = -30.0f;
                                }
                        }

                        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

                        dibujar();

                        glfwSwapBuffers(window);
                        glfwPollEvents();
                }
        }

        private void dibujar() {

                glLoadIdentity();

                glTranslatef(
                                0.0f,
                                0.0f,
                                posZ);

                glBindTexture(
                                GL_TEXTURE_2D,
                                textureGris);

                glBegin(GL_QUADS);

                glTexCoord2f(0, 1);
                glVertex3f(-1.0f, -1.0f, 0.0f);

                glTexCoord2f(1, 1);
                glVertex3f(1.0f, -1.0f, 0.0f);

                glTexCoord2f(1, 0);
                glVertex3f(1.0f, 1.0f, 0.0f);

                glTexCoord2f(0, 0);
                glVertex3f(-1.0f, 1.0f, 0.0f);

                glEnd();
        }

        public static void main(String[] args) {

                new Main().run();
        }
}