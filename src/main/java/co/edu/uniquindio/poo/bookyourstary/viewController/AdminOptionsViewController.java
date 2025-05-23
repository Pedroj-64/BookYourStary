package co.edu.uniquindio.poo.bookyourstary.viewController;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController;

/**
 * Controlador para la vista de opciones de administrador
 */
public class AdminOptionsViewController {

    @FXML
    private Button btnCreacion;

    @FXML
    private Button btnEstadisticas;

    @FXML
    private Button btnHabitaciones;

    @FXML
    void creacionHosting(ActionEvent event) {
        MainController.loadScene("CreationAndEditingForm", 900, 600);
    }

    @FXML
    void openStatistics(ActionEvent event) {
        MainController.loadScene("stadisticsMenu", 800, 600);
    }

    @FXML
    void manageRooms(ActionEvent event) {
        MainController.loadScene("ManageRooms", 800, 600);
    }

    @FXML
    void salir(ActionEvent event) {
        MainController.loadScene("home", 600, 400);
    }
}
