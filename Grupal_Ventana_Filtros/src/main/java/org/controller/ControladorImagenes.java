package org.controller;

import org.model.*;
import org.view.VentanaPrincipal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static org.model.ConvolucionMatrices.kBordes;
import static org.model.ConvolucionMatrices.kEnfoque;

public class ControladorImagenes {

    private final VentanaPrincipal vista;
    private BufferedImage imagenOriginal;
    private BufferedImage imagenOriginal2; // Segunda imagen para blending
    private BufferedImage imagenModificada;

    public ControladorImagenes(VentanaPrincipal vista) {
        this.vista = vista;
        inicializarEventos();
    }

    private void inicializarEventos() {

        // --- EVENTO PRINCIPAL: CARGAR IMAGEN ---
        vista.getBtnCargarImagen().addActionListener(e -> cargarImagen());

        // ==========================================
        // EVENTOS PESTAÑA 1: RGB & BITS
        // ==========================================
        vista.getBtnFiltroGrises().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = FiltrosBasicos.aplicarEscalaGrises(imagenOriginal)));

        vista.getBtnFiltroNegativo().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = FiltrosBasicos.aplicarNegativo(imagenOriginal)));

        vista.getBtnVidrioEsmerilado().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = FiltrosBasicos.aplicarVidrioEsmerilado(imagenOriginal)));

        vista.getBtnPosterizacion().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = FiltrosRetro.aplicarPosterizacion(imagenOriginal, 4))); // 4 colores fijos

        vista.getBtnExtraerBits().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = FiltrosRetro.extraerBits(imagenOriginal, 0x07, 7))); // Máscara 3 bits (0x07)

        // Los gradientes ahora modifican el alpha de la imagen cargada
        vista.getBtnGradLineal().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = GeneradorGradientes.generarLinealIzquierdaDerecha(imagenOriginal)));

        vista.getBtnGradRadial().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = GeneradorGradientes.generarRadial(imagenOriginal)));

        // ==========================================
        // EVENTOS PESTAÑA 2: HSV
        // ==========================================
        vista.getBtnFantasmal().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = FiltrosBasicos.aplicarFantasmal(imagenOriginal)));

        vista.getBtnRotacionTono().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = FiltrosBasicos.aplicarRotacionTono(imagenOriginal)));

        // ==========================================
        // EVENTOS PESTAÑA: COLORES
        // ==========================================
        vista.getBtnMatrizGrises().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = MatrizColores.aplicarEscalaGrises(imagenOriginal)));

        vista.getBtnMatrizSepia().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = MatrizColores.aplicarSepia(imagenOriginal)));

        vista.getBtnHistograma().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = Histograma.histograma(imagenOriginal)));

        // ==========================================
        // EVENTOS PESTAÑA: BLENDING
        // ==========================================
        vista.getBtnAlphaBlending().addActionListener(e -> aplicarFiltroDosImagenes(() ->
                imagenModificada = Blending.alphaBlending(imagenOriginal, imagenOriginal2)));

        vista.getBtnAdditiveBlending().addActionListener(e -> aplicarFiltroDosImagenes(() ->
                imagenModificada = Blending.additiveBlending(imagenOriginal, imagenOriginal2)));

        vista.getBtnMultiplicativeBlending().addActionListener(e -> aplicarFiltroDosImagenes(() ->
                imagenModificada = Blending.multiplicativeBlending(imagenOriginal, imagenOriginal2)));

        // ==========================================
        // EVENTOS PESTAÑA 3: CONVOLUCIÓN
        // ==========================================
        vista.getBtnConvolucion3x3().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = Convolucion.convolucionBi(imagenOriginal)));

        vista.getBtnConvolucion9x9().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = Convolucion.convolucion9x9(imagenOriginal)));


        // ==========================================
        // EVENTOS PESTAÑA 4: CONVOLUCIÓN CON MATRICES PREDEFINIDAS
        vista.getBtnBordes().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = ConvolucionMatrices.aplicarKernel(imagenOriginal, kBordes, 3)
        ));

        vista.getBtnEnfoque().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = ConvolucionMatrices.aplicarKernel(imagenOriginal, kEnfoque, 3)
        ));
            vista.getBtnAclarar().addActionListener(e -> aplicarFiltro(() ->
                    imagenModificada = ConvolucionMatrices.aplicarKernel(imagenOriginal, ConvolucionMatrices.kAclaracion, 3)
            ));
            vista.getBtnOscurecer().addActionListener(e -> aplicarFiltro(() ->
                    imagenModificada = ConvolucionMatrices.aplicarKernel(imagenOriginal, ConvolucionMatrices.kOscurecer, 3)
            ));
                vista.getBtnEnfoque9x9().addActionListener(e -> aplicarFiltro(() ->
                        imagenModificada = ConvolucionMatrices.aplicarKernel(imagenOriginal, ConvolucionMatrices.kEnfoque9x9, 9)
                ));
                vista.getBtnDesenfoque().addActionListener(e -> aplicarFiltro(() ->
                        imagenModificada = ConvolucionMatrices.aplicarKernel(imagenOriginal, ConvolucionMatrices.kBlur, 3)
                ));

                // Evento para cambiar visibilidad del segundo label según la pestaña
                // Removido por petición del usuario



    }

    // --- MÉTODOS DE APOYO ---

    // Método general que valida si hay foto antes de llamar al filtro para no repetir código
    private void aplicarFiltro(Runnable filtroMatematico) {
        if (imagenOriginal != null) {
            filtroMatematico.run(); // Ejecuta la línea de código del Modelo que le enviamos
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        } else {
            JOptionPane.showMessageDialog(vista, "¡Primero debes abrir una imagen!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarFiltroDosImagenes(Runnable filtroMatematico) {
        if (imagenOriginal == null) {
            JOptionPane.showMessageDialog(vista, "¡Primero debes abrir la primera imagen!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Aviso para seleccionar la segunda imagen
        JOptionPane.showMessageDialog(vista, "Por favor, seleccione la segunda imagen para realizar el blending.", "Seleccionar Imagen", JOptionPane.INFORMATION_MESSAGE);

        // Cargar la segunda imagen
        JFileChooser explorador = new JFileChooser();
        explorador.setDialogTitle("Selecciona la segunda imagen para Blending");
        int resultado = explorador.showOpenDialog(vista);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = explorador.getSelectedFile();
            try {
                imagenOriginal2 = ImageIO.read(archivoSeleccionado);
                filtroMatematico.run();
                vista.setImagenProcesada(new ImageIcon(imagenModificada));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al leer la segunda imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void cargarImagen() {
        JFileChooser explorador = new JFileChooser();
        int resultado = explorador.showOpenDialog(vista);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = explorador.getSelectedFile();
            try {
                imagenOriginal = ImageIO.read(archivoSeleccionado);

                vista.setImagenOriginal(new ImageIcon(imagenOriginal));
                vista.setImagenProcesada(null); // limpiar derecha
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al leer la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}