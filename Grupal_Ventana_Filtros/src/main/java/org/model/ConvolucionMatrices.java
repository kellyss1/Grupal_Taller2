package org.model;

import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;

public class ConvolucionMatrices {


    //enfoque(sharken)
    public static final float[] kEnfoque = {
            0f,-1f,0f,
            -1f,5f,-1f,
            0f,-1f,0f,

    };
    //enfoque(sharken)
    public static final float[] kEnfoque9x9 = {
            1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,
            1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,
            1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,
            1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,
            1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,
            1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,
            1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,
            1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,
            1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,1f/81,

    };
    //desenfoque
    public static final float[] kBlur = {
            1f/9,1f/9,1f/9,
            1f/9,1f/9,1f/9,
            1f/9,1f/9,1f/9,

    };
    //deteccion de bordes
    public static final float[] kBordes = {
            -0.5f,-0.5f,-0.5f,
            -0.5f,4f,-0.5f,
            -0.5f,-0.5f,-0.5f

    };
    //Aclaracion
    public static final float[] kAclaracion = {
            0.1f,0.1f,0.1f,
            0.1f,1.0f,0.1f,
            0.1f,0.1f,0.1f,

    };
    //Oscureser
    public static final float[] kOscurecer =  {
            0.01f,0.01f,0.01f,
            0.01f,0.5f,0.01f,
            0.01f,0.01f,0.01f,

    };
    public static BufferedImage aplicarKernel(BufferedImage original, float[] kernelData, int size) {

        Kernel kernel = new Kernel(size, size, kernelData);

        ConvolveOp op = new ConvolveOp(
                kernel,
                ConvolveOp.EDGE_NO_OP, // no modifica bordes
                null
        );

        return op.filter(original, null);
    }
}
