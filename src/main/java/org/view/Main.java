import com.formdev.flatlaf.FlatDarkLaf;
import org.controller.ControladorImagenes;
import org.view.VentanaPrincipal;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // 1. Activar FlatLaf (El "Look and Feel" moderno)
        FlatDarkLaf.setup();

        SwingUtilities.invokeLater(() -> {
            // 2. Creamos la Vista (La pantalla)
            VentanaPrincipal vista = new VentanaPrincipal();

            // 3. Creamos el Controlador y le pasamos la Vista para que la maneje
            ControladorImagenes controlador = new ControladorImagenes(vista);

            // 4. Encendemos la ventana
            vista.setVisible(true);
        });
    }
}