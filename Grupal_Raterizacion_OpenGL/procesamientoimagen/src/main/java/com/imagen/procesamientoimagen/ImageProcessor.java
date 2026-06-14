package com.imagen.procesamientoimagen;

import java.awt.image.BufferedImage;

public class ImageProcessor {

    public static BufferedImage escalaGrises(
            BufferedImage img) {

        int width =
                img.getWidth();

        int height =
                img.getHeight();

        BufferedImage salida =
                new BufferedImage(
                        width,
                        height,
                        BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {

                int rgb =
                        img.getRGB(x, y);

                int a =
                        (rgb >> 24) & 255;

                int r =
                        (rgb >> 16) & 255;

                int g =
                        (rgb >> 8) & 255;

                int b =
                        rgb & 255;

                int gris =
                        (r + g + b) / 3;

                int nuevoRGB =
                        (a << 24)
                                | (gris << 16)
                                | (gris << 8)
                                | gris;

                salida.setRGB(
                        x,
                        y,
                        nuevoRGB);
            }
        }

        return salida;
    }
}