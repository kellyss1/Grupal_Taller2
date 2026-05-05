package org.controller;

import org.model.FiltrosBasicos;
import org.model.FiltrosRetro;
import org.model.GeneradorGradientes;
import org.model.Convolucion;
import org.view.VentanaPrincipal;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ControladorImagenes {

    private VentanaPrincipal vista;
    private BufferedImage imagenOriginal;
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

        // Los gradientes pueden generarse sin necesidad de cargar una foto previa
        vista.getBtnGradLineal().addActionListener(e -> {
            int w = (imagenOriginal != null) ? imagenOriginal.getWidth() : 800;
            int h = (imagenOriginal != null) ? imagenOriginal.getHeight() : 600;
            imagenModificada = GeneradorGradientes.generarLinealIzquierdaDerecha(w, h);
            vista.setImagenPantalla(new ImageIcon(imagenModificada));
        });

        vista.getBtnGradRadial().addActionListener(e -> {
            int w = (imagenOriginal != null) ? imagenOriginal.getWidth() : 800;
            int h = (imagenOriginal != null) ? imagenOriginal.getHeight() : 600;
            imagenModificada = GeneradorGradientes.generarRadial(w, h);
            vista.setImagenPantalla(new ImageIcon(imagenModificada));
        });

        // ==========================================
        // EVENTOS PESTAÑA 2: HSV
        // ==========================================
        vista.getBtnFantasmal().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = FiltrosBasicos.aplicarFantasmal(imagenOriginal)));

        // ==========================================
        // EVENTOS PESTAÑA 3: CONVOLUCIÓN
        // ==========================================
        vista.getBtnConvolucion3x3().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = Convolucion.convolucionBi(imagenOriginal)));

        vista.getBtnConvolucion9x9().addActionListener(e -> aplicarFiltro(() ->
                imagenModificada = Convolucion.convolucion9x9(imagenOriginal)));
    }

    // --- MÉTODOS DE APOYO ---

    // Método general que valida si hay foto antes de llamar al filtro para no repetir código
    private void aplicarFiltro(Runnable filtroMatematico) {
        if (imagenOriginal != null) {
            filtroMatematico.run(); // Ejecuta la línea de código del Modelo que le enviamos
            vista.setImagenPantalla(new ImageIcon(imagenModificada));
        } else {
            JOptionPane.showMessageDialog(vista, "⚠️ ¡Primero debes abrir una imagen!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cargarImagen() {
        JFileChooser explorador = new JFileChooser();
        int resultado = explorador.showOpenDialog(vista);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = explorador.getSelectedFile();
            try {
                imagenOriginal = ImageIO.read(archivoSeleccionado);
                vista.setImagenPantalla(new ImageIcon(imagenOriginal));
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(vista, "Error al leer la imagen: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}