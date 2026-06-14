package org.view;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private JLabel lblOriginal;
    private JLabel lblOriginal2;
    private JScrollPane scrollOriginal2; // Referencia para ocultar/mostrar
    private JLabel lblProcesada;
    private JTabbedPane tabs;

    private JButton btnCargarImagen;

    private JButton btnFiltroGrises, btnFiltroNegativo, btnVidrioEsmerilado;
    private JButton btnGradLineal, btnGradRadial;
    private JButton btnPosterizacion, btnExtraerBits;

    private JButton btnFantasmal, btnRotacionTono;

    private JButton btnMatrizGrises, btnMatrizSepia, btnHistograma;

    private JButton btnAlphaBlending, btnAdditiveBlending, btnMultiplicativeBlending;

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
        tabs = new JTabbedPane();
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

        JPanel panelHSV = crearPanelGrid(1,2);
        btnFantasmal = new JButton("Fantasmal");
        btnRotacionTono = new JButton("Rotar Tono");
        panelHSV.add(btnFantasmal);
        panelHSV.add(btnRotacionTono);

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

        JPanel panelMatrices = crearPanelGrid(1,3);
        btnMatrizGrises = new JButton("Escala de Grises");
        btnMatrizSepia = new JButton("Sepia");
        btnHistograma = new JButton("Histograma");
        panelMatrices.add(btnMatrizGrises);
        panelMatrices.add(btnMatrizSepia);
        panelMatrices.add(btnHistograma);
        tabs.addTab("Colores", panelMatrices);

        JPanel panelBlending = crearPanelGrid(1,3);
        btnAlphaBlending = new JButton("Alpha Blending");
        btnAdditiveBlending = new JButton("Additive Blending");
        btnMultiplicativeBlending = new JButton("Multiplicative Blending");
        panelBlending.add(btnAlphaBlending);
        panelBlending.add(btnAdditiveBlending);
        panelBlending.add(btnMultiplicativeBlending);
        tabs.addTab("Blending", panelBlending);

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
        if (icon != null) {
            lblOriginal.setIcon(escalarImagen(icon, lblOriginal));
        } else {
            lblOriginal.setIcon(null);
        }
        lblOriginal.setText(icon == null ? "Original" : "");
    }

    public void setImagenProcesada(ImageIcon icon) {
        if (icon != null) {
            lblProcesada.setIcon(escalarImagen(icon, lblProcesada));
        } else {
            lblProcesada.setIcon(null);
        }
        lblProcesada.setText(icon == null ? "Procesada" : "");
    }

    private ImageIcon escalarImagen(ImageIcon icon, JLabel label) {
        // Obtenemos el tamaño del contenedor (el JScrollPane o el panel)
        // Como el label está en un scrollpane, el tamaño puede ser engañoso.
        // Vamos a usar un tamaño máximo razonable basado en la ventana si no tiene tamaño aún.
        int maxW = label.getParent().getWidth();
        int maxH = label.getParent().getHeight();

        if (maxW <= 0) maxW = 500;
        if (maxH <= 0) maxH = 500;

        Image img = icon.getImage();
        int imgW = img.getWidth(null);
        int imgH = img.getHeight(null);

        double ratio = Math.min((double) maxW / imgW, (double) maxH / imgH);
        
        // Solo escalar si la imagen es más grande que el espacio disponible
        if (ratio < 1.0) {
            int newW = (int) (imgW * ratio);
            int newH = (int) (imgH * ratio);
            return new ImageIcon(img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH));
        }
        
        return icon;
    }

    public void mostrarSegundoLabel(boolean visible) {
        // Eliminado por petición
    }

    // ===== GETTERS =====
    public JTabbedPane getTabs() { return tabs; }
    public JButton getBtnCargarImagen() { return btnCargarImagen; }

    public JButton getBtnFiltroGrises() { return btnFiltroGrises; }
    public JButton getBtnFiltroNegativo() { return btnFiltroNegativo; }
    public JButton getBtnVidrioEsmerilado() { return btnVidrioEsmerilado; }
    public JButton getBtnGradLineal() { return btnGradLineal; }
    public JButton getBtnGradRadial() { return btnGradRadial; }
    public JButton getBtnPosterizacion() { return btnPosterizacion; }
    public JButton getBtnExtraerBits() { return btnExtraerBits; }

    public JButton getBtnFantasmal() { return btnFantasmal; }
    public JButton getBtnRotacionTono() { return btnRotacionTono; }

    public JButton getBtnMatrizGrises() { return btnMatrizGrises; }
    public JButton getBtnMatrizSepia() { return btnMatrizSepia; }
    public JButton getBtnHistograma() { return btnHistograma; }

    public JButton getBtnAlphaBlending() { return btnAlphaBlending; }
    public JButton getBtnAdditiveBlending() { return btnAdditiveBlending; }
    public JButton getBtnMultiplicativeBlending() { return btnMultiplicativeBlending; }

    public JButton getBtnConvolucion3x3() { return btnConvolucion3x3; }
    public JButton getBtnConvolucion9x9() { return btnConvolucion9x9; }

    public JButton getBtnBordes() { return btnBordes; }
    public JButton getBtnEnfoque() { return btnEnfoque; }
    public JButton getBtnAclarar() { return btnAclarar; }
    public JButton getBtnOscurecer() { return btnOscurecer; }
    public JButton getBtnEnfoque9x9() { return btnEnfoque9x9; }
    public JButton getBtnDesenfoque() { return btnDesenfoque; }
}