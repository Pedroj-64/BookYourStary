package co.edu.uniquindio.poo.bookyourstary.viewController;

import co.edu.uniquindio.poo.bookyourstary.controller.UserHeaderController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

public class UserHeaderViewController {

    @FXML
    private Button btn_Carrito;

    @FXML
    private Label lbl_Nombre;

    @FXML
    private ImageView img_Carrito;

    @FXML
    private Label lbl_Saldo;

    UserHeaderController controller = new UserHeaderController();

    @FXML
    void abrirCarrito(ActionEvent event) {
        // Navigate to the screen where orders/cart are managed
        // Assuming "ManageOrder.fxml" is the correct FXML file for this.
        // Adjust width and height as needed for that scene.
        co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController.loadScene("ManageOrder", 900, 600);
    }

    @FXML
    void initialize() {
        updateHeader();

    }

    private void updateHeader() {
        controller.updateUserHeader(lbl_Nombre, lbl_Saldo);
        // controller.updateHeaderImageAndButton(img_Carrito, btn_Carrito); // Removed this call
    }    @FXML
    void abrirMenuAdmin(ActionEvent event) {
        try {
            System.out.println("Intentando cargar la pantalla del MenuAdmin...");
            // Navigate to the main admin menu screen
            // La ruta correcta es "MenuAdmin" (con M mayúscula)
            co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController.loadScene("MenuAdmin", 900, 600);
            System.out.println("Pantalla de MenuAdmin cargada exitosamente");
        } catch (Exception e) {
            System.err.println("Error al cargar MenuAdmin: " + e.getMessage());
            e.printStackTrace();
            co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController.showAlert(
                "Error", 
                "No se pudo abrir el menú de administrador: " + e.getMessage(),
                javafx.scene.control.Alert.AlertType.ERROR
            );
        }
    }
}
