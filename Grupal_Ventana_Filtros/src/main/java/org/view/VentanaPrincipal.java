package org.view;

import javax.swing.*;
import java.awt.*;

public class VentanaPrincipal extends JFrame {

    private JLabel lblOriginal;
    private JLabel lblOriginal2;
    private JScrollPane scrollOriginal2; // Referencia para ocultar/mostrar
    private JScrollPane scrollOriginal;
    private JLabel lblProcesada;
    private JTabbedPane tabs;
    
    private JSlider sliderZA, sliderZB, sliderAlpha;
    private JButton btnZTest;
    private JPanel panelControlesZ;
    private JPanel panelControlesW; // Panel para botones de modo W
    private JPanel panelControlesAlpha; // Panel para el Alpha Test
    private JPanel panelControlesStencil; // Panel para Stencil/Blending/Op
    private JPanel panelControlesBuffer; // Panel para Buffer de Acumulación
    private JPanel panelControlesEcualizador; // Panel para Ecualizador
    private JLabel lblHistoOriginal, lblHistoEcualizado;
    private JPanel panelContenedorDerecho; // Contenedor para los paneles laterales
    private JButton[] btnsModosW; // Botones para los 5 modos
    private JButton btnModoStencil, btnModoBlending, btnModoFinal;
    private JSlider sliderBuffer, sliderEcualizador;

    private JButton btnCargarImagen, btnCargarImagen2;

    private JButton btnFiltroGrises, btnFiltroNegativo, btnVidrioEsmerilado;
    private JButton btnGradLineal, btnGradRadial;
    private JButton btnPosterizacion, btnExtraerBits;

    private JButton btnFantasmal, btnRotacionTono;

    private JButton btnMatrizGrises, btnMatrizSepia, btnHistograma;

    private JButton btnAlphaBlending, btnAdditiveBlending, btnMultiplicativeBlending;

    private JButton btnConvolucion3x3, btnConvolucion9x9;
    private JButton btnBordes, btnEnfoque, btnAclarar, btnOscurecer, btnEnfoque9x9, btnDesenfoque;
    private JButton btnRasterizacion, btnTextura, btnMultisample, btnStencil, btnBufferAcumulacion, btnEcualizador;

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

        btnCargarImagen = new JButton("Agregar Imagen 1");
        btnCargarImagen.setFocusable(false);
        top.add(btnCargarImagen);

        btnCargarImagen2 = new JButton("Agregar Imagen 2");
        btnCargarImagen2.setFocusable(false);
        top.add(btnCargarImagen2);

        add(top, BorderLayout.NORTH);

        // ================== CENTER ==================
        JPanel panelImagenes = new JPanel(new GridLayout(1,2,10,10));
        panelImagenes.setOpaque(false);
        panelImagenes.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        lblOriginal = new JLabel("Original 1", SwingConstants.CENTER);
        lblOriginal2 = new JLabel("Original 2", SwingConstants.CENTER);
        lblProcesada = new JLabel("Procesada", SwingConstants.CENTER);

        estilizarLabel(lblOriginal);
        estilizarLabel(lblOriginal2);
        estilizarLabel(lblProcesada);

        scrollOriginal = new JScrollPane(lblOriginal);
        scrollOriginal2 = new JScrollPane(lblOriginal2);
        
        panelImagenes.add(scrollOriginal);
        panelImagenes.add(scrollOriginal2);
        panelImagenes.add(new JScrollPane(lblProcesada));

        // Por defecto, ocultar la segunda imagen si no es necesaria
        scrollOriginal2.setVisible(false);

        add(panelImagenes, BorderLayout.CENTER);

        // ================== CONTENEDOR DERECHO ==================
        panelContenedorDerecho = new JPanel(new CardLayout());
        panelContenedorDerecho.setOpaque(false);

        // ================== CONTROLES Z (Ocultos por defecto) ==================
        panelControlesZ = new JPanel();
        panelControlesZ.setLayout(new BoxLayout(panelControlesZ, BoxLayout.Y_AXIS));
        panelControlesZ.setOpaque(false);
        panelControlesZ.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        sliderZA = new JSlider(10, 90, 40);
        sliderZB = new JSlider(10, 90, 60);
        btnZTest = new JButton("Z-Test: ON");

        panelControlesZ.add(new JLabel("Profundidad Textura"));
        panelControlesZ.add(sliderZA);
        panelControlesZ.add(new JLabel("Profundidad Magenta"));
        panelControlesZ.add(sliderZB);
        panelControlesZ.add(btnZTest);

        panelContenedorDerecho.add(panelControlesZ, "Z");

        // ================== CONTROLES W (Ocultos por defecto) ==================
        panelControlesW = new JPanel();
        panelControlesW.setLayout(new BoxLayout(panelControlesW, BoxLayout.Y_AXIS));
        panelControlesW.setOpaque(false);
        panelControlesW.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));

        btnsModosW = new JButton[5];
        String[] nombresModos = {"Textura", "Color", "Profundidad 1/W", "W-Buffer", "Completo"};
        for (int i = 0; i < 5; i++) {
            btnsModosW[i] = new JButton(nombresModos[i]);
            btnsModosW[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            btnsModosW[i].setMaximumSize(new Dimension(200, 40));
            panelControlesW.add(btnsModosW[i]);
            panelControlesW.add(Box.createRigidArea(new Dimension(0, 5)));
        }

        panelContenedorDerecho.add(panelControlesW, "W");

        // ================== CONTROLES ALPHA (Ocultos por defecto) ==================
        panelControlesAlpha = new JPanel();
        panelControlesAlpha.setLayout(new BoxLayout(panelControlesAlpha, BoxLayout.Y_AXIS));
        panelControlesAlpha.setOpaque(false);
        panelControlesAlpha.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sliderAlpha = new JSlider(0, 100, 40);
        panelControlesAlpha.add(new JLabel("Alpha Threshold"));
        panelControlesAlpha.add(sliderAlpha);

        panelContenedorDerecho.add(panelControlesAlpha, "ALPHA");

        // ================== CONTROLES STENCIL (Ocultos por defecto) ==================
        panelControlesStencil = new JPanel();
        panelControlesStencil.setLayout(new BoxLayout(panelControlesStencil, BoxLayout.Y_AXIS));
        panelControlesStencil.setOpaque(false);
        panelControlesStencil.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnModoStencil = new JButton("Modo Stencil");
        btnModoBlending = new JButton("Modo Blending");
        btnModoFinal = new JButton("Resultado Final");

        btnModoStencil.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnModoBlending.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnModoFinal.setAlignmentX(Component.CENTER_ALIGNMENT);

        panelControlesStencil.add(btnModoStencil);
        panelControlesStencil.add(Box.createRigidArea(new Dimension(0, 5)));
        panelControlesStencil.add(btnModoBlending);
        panelControlesStencil.add(Box.createRigidArea(new Dimension(0, 5)));
        panelControlesStencil.add(btnModoFinal);

        panelContenedorDerecho.add(panelControlesStencil, "STENCIL");

        // ================== CONTROLES BUFFER (Ocultos por defecto) ==================
        panelControlesBuffer = new JPanel();
        panelControlesBuffer.setLayout(new BoxLayout(panelControlesBuffer, BoxLayout.Y_AXIS));
        panelControlesBuffer.setOpaque(false);
        panelControlesBuffer.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sliderBuffer = new JSlider(0, 100, 100);
        panelControlesBuffer.add(new JLabel("Factor de Atenuación"));
        panelControlesBuffer.add(sliderBuffer);

        panelContenedorDerecho.add(panelControlesBuffer, "BUFFER");

        // ================== CONTROLES ECUALIZADOR (Ocultos por defecto) ==================
        panelControlesEcualizador = new JPanel();
        panelControlesEcualizador.setLayout(new BoxLayout(panelControlesEcualizador, BoxLayout.Y_AXIS));
        panelControlesEcualizador.setOpaque(false);
        panelControlesEcualizador.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        sliderEcualizador = new JSlider(0, 100, 100);
        panelControlesEcualizador.add(new JLabel("Porcentaje Ecualización"));
        panelControlesEcualizador.add(sliderEcualizador);

        panelControlesEcualizador.add(Box.createRigidArea(new Dimension(0, 10)));
        panelControlesEcualizador.add(new JLabel("Histograma Original:"));
        lblHistoOriginal = new JLabel();
        lblHistoOriginal.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelControlesEcualizador.add(lblHistoOriginal);

        panelControlesEcualizador.add(Box.createRigidArea(new Dimension(0, 10)));
        panelControlesEcualizador.add(new JLabel("Histograma Resultante:"));
        lblHistoEcualizado = new JLabel();
        lblHistoEcualizado.setAlignmentX(Component.CENTER_ALIGNMENT);
        panelControlesEcualizador.add(lblHistoEcualizado);

        panelContenedorDerecho.add(panelControlesEcualizador, "ECUALIZADOR");

        // Panel vacío para cuando no hay controles activos
        JPanel panelVacio = new JPanel();
        panelVacio.setOpaque(false);
        panelContenedorDerecho.add(panelVacio, "VACIO");

        add(panelContenedorDerecho, BorderLayout.EAST);
        ((CardLayout) panelContenedorDerecho.getLayout()).show(panelContenedorDerecho, "VACIO");

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

        JPanel panelGrupales = crearPanelGrid(2, 3);
        btnRasterizacion = new JButton("Rasterizacion/Z-Buffer/Bitmaps");
        btnTextura = new JButton("Textura/Interpolacion/W-Buffereing");
        btnMultisample = new JButton("Multisample/AlphaTest");
        btnStencil = new JButton("Stencil/Blending/Op");
        btnBufferAcumulacion = new JButton("BufferAcumulacion");

        panelGrupales.add(btnRasterizacion);
        panelGrupales.add(btnTextura);
        panelGrupales.add(btnMultisample);
        panelGrupales.add(btnStencil);
        panelGrupales.add(btnBufferAcumulacion);
        tabs.addTab("Grupales", panelGrupales);

        JPanel panelEcua = crearPanelGrid(1, 1);
        btnEcualizador = new JButton("Ecualizar Histograma");
        panelEcua.add(btnEcualizador);
        tabs.addTab("Ecualizador", panelEcua);

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
        lblOriginal.setText(icon == null ? "Original 1" : "");
    }

    public void setImagenOriginal2(ImageIcon icon) {
        if (icon != null) {
            lblOriginal2.setIcon(escalarImagen(icon, lblOriginal2));
        } else {
            lblOriginal2.setIcon(null);
        }
        lblOriginal2.setText(icon == null ? "Original 2" : "");
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
        scrollOriginal.setVisible(true);
        scrollOriginal2.setVisible(visible);
        ((CardLayout) panelContenedorDerecho.getLayout()).show(panelContenedorDerecho, "VACIO");
        revalidate();
        repaint();
    }

    public void mostrarControlesZ() {
        scrollOriginal.setVisible(false);
        scrollOriginal2.setVisible(false);
        ((CardLayout) panelContenedorDerecho.getLayout()).show(panelContenedorDerecho, "Z");
        revalidate();
        repaint();
    }

    public void mostrarControlesW() {
        scrollOriginal.setVisible(false);
        scrollOriginal2.setVisible(false);
        ((CardLayout) panelContenedorDerecho.getLayout()).show(panelContenedorDerecho, "W");
        revalidate();
        repaint();
    }

    public void mostrarControlesAlpha() {
        scrollOriginal.setVisible(false);
        scrollOriginal2.setVisible(false);
        ((CardLayout) panelContenedorDerecho.getLayout()).show(panelContenedorDerecho, "ALPHA");
        revalidate();
        repaint();
    }

    public void mostrarControlesStencil() {
        scrollOriginal.setVisible(true);
        scrollOriginal2.setVisible(true);
        ((CardLayout) panelContenedorDerecho.getLayout()).show(panelContenedorDerecho, "STENCIL");
        revalidate();
        repaint();
    }

    public void mostrarControlesBuffer() {
        scrollOriginal.setVisible(true);
        scrollOriginal2.setVisible(false);
        ((CardLayout) panelContenedorDerecho.getLayout()).show(panelContenedorDerecho, "BUFFER");
        revalidate();
        repaint();
    }

    public void mostrarControlesEcualizador() {
        scrollOriginal.setVisible(true);
        scrollOriginal2.setVisible(false);
        ((CardLayout) panelContenedorDerecho.getLayout()).show(panelContenedorDerecho, "ECUALIZADOR");
        revalidate();
        repaint();
    }

    public void setHistoOriginal(ImageIcon icon) {
        lblHistoOriginal.setIcon(icon);
        lblHistoOriginal.setText(icon == null ? "Sin histograma" : "");
    }

    public void setHistoEcualizado(ImageIcon icon) {
        lblHistoEcualizado.setIcon(icon);
        lblHistoEcualizado.setText(icon == null ? "Sin histograma" : "");
    }

    // ===== GETTERS =====
    public JTabbedPane getTabs() { return tabs; }
    public JButton getBtnCargarImagen() { return btnCargarImagen; }
    public JButton getBtnCargarImagen2() { return btnCargarImagen2; }

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

    public JButton getBtnRasterizacion() { return btnRasterizacion; }
    public JButton getBtnTextura() { return btnTextura; }
    public JButton getBtnMultisample() { return btnMultisample; }
    public JButton getBtnStencil() { return btnStencil; }
    public JButton getBtnBufferAcumulacion() { return btnBufferAcumulacion; }
    public JButton getBtnEcualizador() { return btnEcualizador; }

    public JSlider getSliderZA() { return sliderZA; }
    public JSlider getSliderZB() { return sliderZB; }
    public JSlider getSliderAlpha() { return sliderAlpha; }
    public JSlider getSliderBuffer() { return sliderBuffer; }
    public JSlider getSliderEcualizador() { return sliderEcualizador; }
    public JButton getBtnZTest() { return btnZTest; }

    public JButton getBtnModoStencil() { return btnModoStencil; }
    public JButton getBtnModoBlending() { return btnModoBlending; }
    public JButton getBtnModoFinal() { return btnModoFinal; }

    public JButton[] getBtnsModosW() { return btnsModosW; }
}