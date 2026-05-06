package org.view;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private JLabel lblOriginal;
    private JLabel lblProcesada;

    private JButton btnCargarImagen;

    private JButton btnFiltroGrises, btnFiltroNegativo, btnVidrioEsmerilado;
    private JButton btnGradLineal, btnGradRadial;
    private JButton btnPosterizacion, btnExtraerBits;

    private JButton btnFantasmal;

    private JButton btnConvolucion3x3, btnConvolucion9x9;
    private JButton btnBordes, btnEnfoque, btnAclarar, btnOscurecer, btnEnfoque9x9, btnDesenfoque;

    public VentanaPrincipal() {

        setTitle("Procesador de Imágenes");
        setSize(1200, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // 🎨 FONDO GRADIENTE AZUL ELÉCTRICO
        JPanel fondo = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                GradientPaint gp = new GradientPaint(
                        0, 0, new Color(255,255,255),
                        getWidth(), getHeight(), new Color(0,120,255)
                );

                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        fondo.setLayout(new BorderLayout(10,10));
        setContentPane(fondo);

        // ================== TOP ==================
        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        top.setOpaque(false);

        btnCargarImagen = new JButton("Abrir Imagen");
        btnCargarImagen.setFocusable(false);
        top.add(btnCargarImagen);

        add(top, BorderLayout.NORTH);

        // ================== CENTER ==================
        JPanel panelImagenes = new JPanel(new GridLayout(1,2,10,10));
        panelImagenes.setOpaque(false);
        panelImagenes.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        lblOriginal = new JLabel("Original", SwingConstants.CENTER);
        lblProcesada = new JLabel("Procesada", SwingConstants.CENTER);

        estilizarLabel(lblOriginal);
        estilizarLabel(lblProcesada);

        panelImagenes.add(new JScrollPane(lblOriginal));
        panelImagenes.add(new JScrollPane(lblProcesada));

        add(panelImagenes, BorderLayout.CENTER);

        // ================== TABS ==================
        JTabbedPane tabs = new JTabbedPane();
        tabs.setOpaque(false);

        JPanel panelRGB = crearPanelGrid(2,4);
        btnFiltroGrises = new JButton("Grises");
        btnFiltroNegativo = new JButton("Negativo");
        btnVidrioEsmerilado = new JButton("Vidrio");
        btnGradLineal = new JButton("Gradiente L");
        btnGradRadial = new JButton("Gradiente R");
        btnPosterizacion = new JButton("Poster");
        btnExtraerBits = new JButton("Bits");

        panelRGB.add(btnFiltroGrises);
        panelRGB.add(btnFiltroNegativo);
        panelRGB.add(btnVidrioEsmerilado);
        panelRGB.add(btnGradLineal);
        panelRGB.add(btnGradRadial);
        panelRGB.add(btnPosterizacion);
        panelRGB.add(btnExtraerBits);

        JPanel panelHSV = new JPanel(new FlowLayout());
        panelHSV.setOpaque(false);
        btnFantasmal = new JButton("Fantasmal");
        panelHSV.add(btnFantasmal);

        JPanel panelConv = crearPanelGrid(1,2);
        btnConvolucion3x3 = new JButton("Blur 3x3");
        btnConvolucion9x9 = new JButton("Blur 9x9");
        panelConv.add(btnConvolucion3x3);
        panelConv.add(btnConvolucion9x9);

        JPanel panelConv2 = crearPanelGrid(2,3);
        btnBordes = new JButton("Bordes");
        btnEnfoque = new JButton("Enfoque");
        btnAclarar = new JButton("Aclarar");
        btnOscurecer = new JButton("Oscurecer");
        btnEnfoque9x9 = new JButton("Enfoque 9x9");
        btnDesenfoque = new JButton("Blur");

        panelConv2.add(btnBordes);
        panelConv2.add(btnEnfoque);
        panelConv2.add(btnAclarar);
        panelConv2.add(btnOscurecer);
        panelConv2.add(btnEnfoque9x9);
        panelConv2.add(btnDesenfoque);

        tabs.addTab("RGB", panelRGB);
        tabs.addTab("HSV", panelHSV);
        tabs.addTab("Conv Básica", panelConv);
        tabs.addTab("Conv Avanzada", panelConv2);

        add(tabs, BorderLayout.SOUTH);
    }

    private JPanel crearPanelGrid(int filas, int cols){
        JPanel p = new JPanel(new GridLayout(filas, cols, 10,10));
        p.setOpaque(false);
        p.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        return p;
    }

    private void estilizarLabel(JLabel lbl){
        lbl.setOpaque(true);
        lbl.setBackground(Color.WHITE);
        lbl.setBorder(BorderFactory.createLineBorder(new Color(0,120,255),2));
    }

    // ===== SETTERS =====
    public void setImagenOriginal(ImageIcon icon) {
        lblOriginal.setIcon(icon);
        lblOriginal.setText("");
    }

    public void setImagenProcesada(ImageIcon icon) {
        lblProcesada.setIcon(icon);
        lblProcesada.setText("");
    }

    // ===== GETTERS =====
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

    public JButton getBtnBordes() { return btnBordes; }
    public JButton getBtnEnfoque() { return btnEnfoque; }
    public JButton getBtnAclarar() { return btnAclarar; }
    public JButton getBtnOscurecer() { return btnOscurecer; }
    public JButton getBtnEnfoque9x9() { return btnEnfoque9x9; }
    public JButton getBtnDesenfoque() { return btnDesenfoque; }
}