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
    }

    @FXML
    void abrirMenuAdmin(ActionEvent event) {
        // Navigate to the main admin menu screen
        // Assuming "menuAdmin.fxml" is the correct FXML file for this.
        // Adjust width and height as needed for that scene.
        co.edu.uniquindio.poo.bookyourstary.internalControllers.MainController.loadScene("menuAdmin", 900, 600);
    }
}
