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
        vista.getBtnCargarImagen().addActionListener(e -> {
            vista.mostrarSegundoLabel(false);
            cargarImagen();
        });

        vista.getBtnCargarImagen2().addActionListener(e -> {
            vista.mostrarSegundoLabel(true);
            cargarImagen2();
        });

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

        // ==========================================
        // EVENTOS PESTAÑA: GRUPALES
        // ==========================================
        RasterizacionZBuffer rz = new RasterizacionZBuffer();
        TexturasWBuffer tw = new TexturasWBuffer();
        MultisampleAlphaTest ma = new MultisampleAlphaTest(800, 600);
        StencilBlendingOp sbo = new StencilBlendingOp();
        BufferAcumulacion ba = new BufferAcumulacion();
        EcualizadorHistograma eh = new EcualizadorHistograma();

        vista.getBtnRasterizacion().addActionListener(e -> {
            vista.mostrarControlesZ();
            imagenModificada = rz.generarImagen();
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        });

        vista.getBtnTextura().addActionListener(e -> {
            vista.mostrarControlesW();
            imagenModificada = tw.generarImagen();
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        });

        vista.getBtnMultisample().addActionListener(e -> {
            vista.mostrarControlesAlpha();
            imagenModificada = ma.generarImagen();
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        });

        vista.getBtnStencil().addActionListener(e -> {
            if (imagenOriginal == null) {
                JOptionPane.showMessageDialog(vista, "Primero debe cargar la imagen base.");
                cargarImagen();
                if (imagenOriginal == null) return;
            }
            
            if (imagenOriginal2 == null) {
                JOptionPane.showMessageDialog(vista, "Este proceso requiere una segunda imagen cargada.");
                cargarImagen2();
                if (imagenOriginal2 == null) return;
            }
            
            vista.mostrarControlesStencil();
            imagenModificada = sbo.procesar(imagenOriginal, imagenOriginal2);
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        });

        vista.getBtnBufferAcumulacion().addActionListener(e -> {
            if (imagenOriginal == null) {
                JOptionPane.showMessageDialog(vista, "Primero debe cargar una imagen.");
                cargarImagen();
                if (imagenOriginal == null) return;
            }
            vista.mostrarControlesBuffer();
            float factor = vista.getSliderBuffer().getValue() / 100f;
            imagenModificada = ba.aplicarAtenuacion(imagenOriginal, factor);
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        });

        vista.getBtnEcualizador().addActionListener(e -> {
            if (imagenOriginal == null) {
                JOptionPane.showMessageDialog(vista, "Primero debe cargar una imagen.");
                cargarImagen();
                if (imagenOriginal == null) return;
            }
            vista.mostrarControlesEcualizador();
            double factor = vista.getSliderEcualizador().getValue() / 100.0;
            imagenModificada = eh.procesar(imagenOriginal, factor);
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
            
            // Mostrar histogramas
            vista.setHistoOriginal(new ImageIcon(eh.generarHistograma(imagenOriginal)));
            vista.setHistoEcualizado(new ImageIcon(eh.generarHistograma(imagenModificada)));
        });

        vista.getBtnModoStencil().addActionListener(e -> {
            sbo.setModo(StencilBlendingOp.Modo.STENCIL);
            imagenModificada = sbo.procesar(imagenOriginal, imagenOriginal2);
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        });

        vista.getBtnModoBlending().addActionListener(e -> {
            sbo.setModo(StencilBlendingOp.Modo.BLENDING);
            imagenModificada = sbo.procesar(imagenOriginal, imagenOriginal2);
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        });

        vista.getBtnModoFinal().addActionListener(e -> {
            sbo.setModo(StencilBlendingOp.Modo.FINAL);
            imagenModificada = sbo.procesar(imagenOriginal, imagenOriginal2);
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        });

        JButton[] btnsModosW = vista.getBtnsModosW();
        for (int i = 0; i < btnsModosW.length; i++) {
            final int modo = i + 1;
            btnsModosW[i].addActionListener(e -> {
                tw.setModo(modo);
                imagenModificada = tw.generarImagen();
                vista.setImagenProcesada(new ImageIcon(imagenModificada));
            });
        }

        vista.getSliderZA().addChangeListener(e -> {
            rz.setZA(vista.getSliderZA().getValue() / 100f);
            imagenModificada = rz.generarImagen();
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        });

        vista.getSliderZB().addChangeListener(e -> {
            rz.setZB(vista.getSliderZB().getValue() / 100f);
            imagenModificada = rz.generarImagen();
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        });

        vista.getBtnZTest().addActionListener(e -> {
            boolean activo = vista.getBtnZTest().getText().contains("ON");
            rz.setzTestActivo(!activo);
            vista.getBtnZTest().setText(!activo ? "Z-Test: ON" : "Z-Test: OFF");
            imagenModificada = rz.generarImagen();
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        });

        vista.getSliderAlpha().addChangeListener(e -> {
            ma.setAlphaThreshold(vista.getSliderAlpha().getValue() / 100f);
            imagenModificada = ma.generarImagen();
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        });

        vista.getSliderBuffer().addChangeListener(e -> {
            if (imagenOriginal != null) {
                float factor = vista.getSliderBuffer().getValue() / 100f;
                imagenModificada = ba.aplicarAtenuacion(imagenOriginal, factor);
                vista.setImagenProcesada(new ImageIcon(imagenModificada));
            }
        });

        vista.getSliderEcualizador().addChangeListener(e -> {
            if (imagenOriginal != null) {
                double factor = vista.getSliderEcualizador().getValue() / 100.0;
                imagenModificada = eh.procesar(imagenOriginal, factor);
                vista.setImagenProcesada(new ImageIcon(imagenModificada));
                vista.setHistoEcualizado(new ImageIcon(eh.generarHistograma(imagenModificada)));
            }
        });

    }

    // --- MÉTODOS DE APOYO ---

    // Método general que valida si hay foto antes de llamar al filtro para no repetir código
    private void aplicarFiltro(Runnable filtroMatematico) {
        vista.mostrarSegundoLabel(false);
        if (imagenOriginal != null) {
            filtroMatematico.run(); // Ejecuta la línea de código del Modelo que le enviamos
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        } else {
            JOptionPane.showMessageDialog(vista, "¡Primero debes abrir una imagen!", "Aviso", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void aplicarFiltroDosImagenes(Runnable filtroMatematico) {
        vista.mostrarSegundoLabel(true);
        if (imagenOriginal == null) {
            JOptionPane.showMessageDialog(vista, "¡Primero debes abrir la primera imagen!", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (imagenOriginal2 == null) {
            cargarImagen2();
        }

        if (imagenOriginal2 != null) {
            filtroMatematico.run();
            vista.setImagenProcesada(new ImageIcon(imagenModificada));
        }
    }

    private void cargarImagen2() {
        // Aviso para seleccionar la segunda imagen
        JOptionPane.showMessageDialog(vista, "Por favor, seleccione la segunda imagen.", "Seleccionar Imagen", JOptionPane.INFORMATION_MESSAGE);

        // Cargar la segunda imagen
        JFileChooser explorador = new JFileChooser();
        explorador.setDialogTitle("Selecciona la segunda imagen");
        int resultado = explorador.showOpenDialog(vista);

        if (resultado == JFileChooser.APPROVE_OPTION) {
            File archivoSeleccionado = explorador.getSelectedFile();
            try {
                imagenOriginal2 = ImageIO.read(archivoSeleccionado);
                vista.setImagenOriginal2(new ImageIcon(imagenOriginal2));
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