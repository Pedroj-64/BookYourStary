package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.AdminHeaderController;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

/**
 * ViewController responsable de la interacción con la vista AdminHeader.fxml.
 * Siguiendo el patrón MVC, este controlador de vista se encarga principalmente
 * de manejar eventos de la interfaz y delegar las operaciones lógicas a su
 * respectivo controlador.
 */
public class AdminHeaderViewController {

    @FXML
    private Button btn_CreationHosting;

    @FXML
    private ImageView img_Carrito;

    @FXML
    private Label lbl_Nombre;

    @FXML
    private Label lbl_Saldo;
    
    // Referencia al controlador de lógica de negocio
    private final AdminHeaderController adminHeaderController = AdminHeaderController.getInstance();

    /**
     * Inicializa el controlador de la vista.
     * Este método es llamado automáticamente por JavaFX después de cargar el FXML.
     */
    @FXML
    public void initialize() {
        // Configurar los elementos UI con datos del controlador
        lbl_Nombre.setText("User: " + adminHeaderController.getCurrentAdminName());
        lbl_Saldo.setText("Saldo: $" + adminHeaderController.getCurrentAdminBalance());
    }

    /**
     * Maneja el evento de clic en el botón de creación de alojamiento.
     * Delega la operación lógica al controlador y maneja posibles errores en la UI.
     * 
     * @param event Evento de acción del botón
     */
    @FXML
    void OpenCreationHosting(ActionEvent event) {
        System.out.println("Vista: Solicitando apertura del menú de opciones de administrador");
        adminHeaderController.navigateToAdminOptions();
    }

}
