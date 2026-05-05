package org.view;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private JLabel lblImagen;
    private JButton btnCargarImagen;

    // Botones Pestaña 1: RGB y Bits
    private JButton btnFiltroGrises, btnFiltroNegativo, btnVidrioEsmerilado;
    private JButton btnGradLineal, btnGradRadial;
    private JButton btnPosterizacion, btnExtraerBits;

    // Botones Pestaña 2: HSV
    private JButton btnFantasmal;

    // Botones Pestaña 3: Convolución
    private JButton btnConvolucion3x3, btnConvolucion9x9;

    public VentanaPrincipal() {
        setTitle("Procesador de Imágenes - Proyecto Final");
        setSize(1050, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- PANEL SUPERIOR ---
        JPanel panelNorte = new JPanel();
        btnCargarImagen = new JButton("Abrir Imagen 📁");
        panelNorte.add(btnCargarImagen);
        add(panelNorte, BorderLayout.NORTH);

        // --- PANEL CENTRAL (IMAGEN) ---
        lblImagen = new JLabel("Carga una imagen para empezar o genera un gradiente...", SwingConstants.CENTER);
        JScrollPane scrollImagen = new JScrollPane(lblImagen);
        add(scrollImagen, BorderLayout.CENTER);

        // --- PANEL INFERIOR (PESTAÑAS) ---
        JTabbedPane pestanas = new JTabbedPane();

        // 1. Pestaña RGB y Bits
        JPanel panelRGB = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnFiltroGrises = new JButton("Escala de Grises");
        btnFiltroNegativo = new JButton("Negativo");
        btnVidrioEsmerilado = new JButton("Vidrio Esmerilado");
        btnGradLineal = new JButton("Gradiente Lineal");
        btnGradRadial = new JButton("Gradiente Radial");
        btnPosterizacion = new JButton("Retro (4 Colores)");
        btnExtraerBits = new JButton("Extraer 3 Bits");

        panelRGB.add(btnFiltroGrises);
        panelRGB.add(btnFiltroNegativo);
        panelRGB.add(btnVidrioEsmerilado);
        panelRGB.add(btnGradLineal);
        panelRGB.add(btnGradRadial);
        panelRGB.add(btnPosterizacion);
        panelRGB.add(btnExtraerBits);

        // 2. Pestaña HSV
        JPanel panelHSV = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnFantasmal = new JButton("Efecto Fantasmal (HSB)");
        panelHSV.add(btnFantasmal);

        // 3. Pestaña Convolución
        JPanel panelConvolucion = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        btnConvolucion3x3 = new JButton("Desenfoque (Matriz 3x3)");
        btnConvolucion9x9 = new JButton("Efecto Acuarela (Matriz 9x9)");
        panelConvolucion.add(btnConvolucion3x3);
        panelConvolucion.add(btnConvolucion9x9);

        // Agregamos todo al TabbedPane
        pestanas.addTab("🎨 RGB & Bits", panelRGB);
        pestanas.addTab("👻 Espacio HSV", panelHSV);
        pestanas.addTab("🌀 Convolución (For)", panelConvolucion);

        add(pestanas, BorderLayout.SOUTH);
    }

    // --- GETTERS (Puertas para el Controlador) ---
    public JButton getBtnCargarImagen() { return btnCargarImagen; }

    public JButton getBtnFiltroGrises() { return btnFiltroGrises; }
    public JButton getBtnFiltroNegativo() { return btnFiltroNegativo; }
    public JButton getBtnVidrioEsmerilado() { return btnVidrioEsmerilado; }
    public JButton getBtnGradLineal() { return btnGradLineal; }
    public JButton getBtnGradRadial() { return btnGradRadial; }
    public JButton getBtnPosterizacion() { return btnPosterizacion; }
    public JButton getBtnExtraerBits() { return btnExtraerBits; }

    public JButton getBtnFantasmal() { return btnFantasmal; }

    public JButton getBtnConvolucion3x3() { return btnConvolucion3x3; }
    public JButton getBtnConvolucion9x9() { return btnConvolucion9x9; }

    public void setImagenPantalla(ImageIcon icono) {
        lblImagen.setIcon(icono);
        lblImagen.setText("");
    }
}