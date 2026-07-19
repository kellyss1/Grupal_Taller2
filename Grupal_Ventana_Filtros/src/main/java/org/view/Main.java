package org.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.controller.ControladorImagenes;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        FlatLightLaf.setup();

        // PALETA BLANCO + MORADO
        UIManager.put("Component.arc", 15);
        UIManager.put("Button.arc", 15);
        UIManager.put("TextComponent.arc", 10);

        UIManager.put("Component.focusWidth", 1);

        UIManager.put("Component.accentColor", new Color(128, 0, 255));
        UIManager.put("Button.background", new Color(128, 0, 255));
        UIManager.put("Button.foreground", Color.WHITE);
        UIManager.put("Button.hoverBackground", new Color(150, 50, 255));
        UIManager.put("Button.pressedBackground", new Color(100, 0, 200));
        UIManager.put("Button.selectedBackground", new Color(100, 0, 200));
        UIManager.put("Button.selectedForeground", Color.WHITE);

        // Forzar colores para botones en reposo (evitar que se vean blancos)
        UIManager.put("Button.startBackground", new Color(128, 0, 255));
        UIManager.put("Button.endBackground", new Color(128, 0, 255));
        UIManager.put("Button.focusedBackground", new Color(128, 0, 255));
        UIManager.put("Button.default.background", new Color(128, 0, 255));
        UIManager.put("Button.default.foreground", Color.WHITE);
        UIManager.put("Button.default.startBackground", new Color(128, 0, 255));
        UIManager.put("Button.default.endBackground", new Color(128, 0, 255));
        UIManager.put("Button.default.hoverBackground", new Color(150, 50, 255));
        UIManager.put("Button.default.pressedBackground", new Color(100, 0, 200));
        UIManager.put("Button.default.focusedBackground", new Color(128, 0, 255));

        UIManager.put("Panel.background", new Color(250, 245, 255));
        UIManager.put("TabbedPane.background", new Color(250, 245, 255));

        UIManager.put("defaultFont", new Font("Segoe UI", Font.PLAIN, 13));

        SwingUtilities.invokeLater(() -> {
            VentanaPrincipal vista = new VentanaPrincipal();
            new ControladorImagenes(vista);
            vista.setVisible(true);
        });
    }
}