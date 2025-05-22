package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;

/**
 * Controlador de vista para la ventana de opciones de administrador.
 * Gestiona la navegación a las diferentes funcionalidades administrativas
 * como la creación de alojamientos y visualización de estadísticas.
 */
public class AdminOptionsViewController {

    @FXML
    private Button btnCreacion;

    @FXML
    private Button btnEstadisticas;

    /**
     * Maneja el evento de clic en el botón de creación de alojamientos.
     * Abre el formulario de creación de nuevos alojamientos.
     * 
     * @param event El evento que disparó esta acción
     */
    @FXML
    void creacionHosting(ActionEvent event) {
        try {
            // Redirigir a la vista de creación de alojamientos
            MainController.loadScene("CreationAndEditingForm", 900, 600);
        } catch (Exception e) {
            MainController.showAlert(
                "Error",
                "No se pudo abrir el formulario de creación: " + e.getMessage(),
                AlertType.ERROR);
        }
    }

    /**
     * Maneja el evento de clic en el botón de estadísticas.
     * Abre el menú de estadísticas del sistema.
     * 
     * @param event El evento que disparó esta acción
     */
    @FXML
    void openStatistics(ActionEvent event) {
        try {
            // Cargar la vista de estadísticas
            MainController.loadScene("stadisticsMenu", 900, 600);
        } catch (Exception e) {
            MainController.showAlert(
                "Error",
                "No se pudo abrir el menú de estadísticas: " + e.getMessage(),
                AlertType.ERROR);
        }
    }
}
